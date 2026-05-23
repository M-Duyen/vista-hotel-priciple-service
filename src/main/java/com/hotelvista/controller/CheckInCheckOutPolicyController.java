package com.hotelvista.controller;

import com.hotelvista.dto.CheckInCheckOutPolicyDTO;
import com.hotelvista.dto.CheckInCheckOutPolicyRuleDTO;
import com.hotelvista.service.CheckInCheckOutPolicyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkin-checkout-policies")
public class CheckInCheckOutPolicyController {
    private final CheckInCheckOutPolicyService service;

    public CheckInCheckOutPolicyController(CheckInCheckOutPolicyService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('pricing_manage')")
    public List<CheckInCheckOutPolicyDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('pricing_manage')")
    public CheckInCheckOutPolicyDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/{id}/rules")
    @PreAuthorize("hasAuthority('pricing_manage')")
    public List<CheckInCheckOutPolicyRuleDTO> findRules(@PathVariable Long id) {
        return service.findRules(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('pricing_manage')")
    public ResponseEntity<CheckInCheckOutPolicyDTO> save(@Valid @RequestBody CheckInCheckOutPolicyDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('pricing_manage')")
    public CheckInCheckOutPolicyDTO update(@PathVariable Long id, @Valid @RequestBody CheckInCheckOutPolicyDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('pricing_manage')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

