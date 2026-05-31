package com.hotelvista.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelvista.exception.BadRequestException;
import com.hotelvista.exception.ExternalServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RoomTypeValidationClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String roomServiceBaseUrl;
    private final String roomTypesPath;

    public RoomTypeValidationClient(RestTemplate restTemplate,
            ObjectMapper objectMapper,
            @Value("${room.service.base-url:lb://room-service}") String roomServiceBaseUrl,
            @Value("${room.service.room-types-path:/api/room-types}") String roomTypesPath) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.roomServiceBaseUrl = roomServiceBaseUrl;
        this.roomTypesPath = roomTypesPath;
    }

    public void validateRoomTypeIds(Collection<String> roomTypeIds) {
        if (roomTypeIds == null || roomTypeIds.isEmpty()) {
            throw new BadRequestException("roomTypeIds must not be empty");
        }

        Set<String> availableRoomTypeIds = fetchAvailableRoomTypeIds();
        List<String> invalidRoomTypeIds = roomTypeIds.stream()
                .filter(id -> id == null || id.isBlank() || !availableRoomTypeIds.contains(id.trim()))
                .map(id -> id == null ? null : id.trim())
                .toList();

        if (!invalidRoomTypeIds.isEmpty()) {
            throw new BadRequestException("Invalid roomTypeIds: " + invalidRoomTypeIds);
        }
    }

    private Set<String> fetchAvailableRoomTypeIds() {
        String url = UriComponentsBuilder.fromUriString(roomServiceBaseUrl)
                .path(roomTypesPath)
                .toUriString();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null
                    || response.getBody().isBlank()) {
                throw new ExternalServiceUnavailableException("Room service returned an empty or invalid response");
            }
            return extractRoomTypeIds(objectMapper.readTree(response.getBody()));
        } catch (RestClientException ex) {
            throw new ExternalServiceUnavailableException(
                    "Cannot reach room-service at " + url + ": " + ex.getMessage());
        } catch (Exception ex) {
            throw new ExternalServiceUnavailableException("Invalid response from room-service: " + ex.getMessage());
        }
    }

    private Set<String> extractRoomTypeIds(JsonNode root) {
        Set<String> roomTypeIds = new HashSet<>();
        if (root == null) {
            return roomTypeIds;
        }

        JsonNode dataNode = unwrapArrayNode(root);
        if (dataNode.isArray()) {
            for (JsonNode node : dataNode) {
                extractRoomTypeId(node).ifPresent(roomTypeIds::add);
            }
            return roomTypeIds;
        }

        if (dataNode.isObject()) {
            extractRoomTypeId(dataNode).ifPresent(roomTypeIds::add);
        }
        return roomTypeIds;
    }

    private JsonNode unwrapArrayNode(JsonNode root) {
        for (String fieldName : List.of("data", "content", "items", "result")) {
            JsonNode child = root.get(fieldName);
            if (child != null && child.isArray()) {
                return child;
            }
        }
        return root;
    }

    private java.util.Optional<String> extractRoomTypeId(JsonNode node) {
        if (node == null || !node.isObject()) {
            return java.util.Optional.empty();
        }

        // room-service serializes the JPA field `roomTypeID` as JSON key `roomTypeID`
        for (String fieldName : List.of("roomTypeID", "roomTypeId", "id", "room_type_id")) {
            JsonNode value = node.get(fieldName);
            if (value != null && !value.isNull() && !value.asText().isBlank()) {
                return java.util.Optional.of(value.asText().trim());
            }
        }
        return java.util.Optional.empty();
    }
}
