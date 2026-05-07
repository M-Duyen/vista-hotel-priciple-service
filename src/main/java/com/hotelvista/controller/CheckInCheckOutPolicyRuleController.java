package com.hotelvista.controller;

import com.hotelvista.dto.CheckInCheckOutPolicyRuleDTO;
import com.hotelvista.service.CheckInCheckOutPolicyRuleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkin-checkout-policy-rules")
public class CheckInCheckOutPolicyRuleController {
    private final CheckInCheckOutPolicyRuleService service;

    public CheckInCheckOutPolicyRuleController(CheckInCheckOutPolicyRuleService service) {
        this.service = service;
    }

    @GetMapping
    public List<CheckInCheckOutPolicyRuleDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public CheckInCheckOutPolicyRuleDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/policy/{policyId}")
    public List<CheckInCheckOutPolicyRuleDTO> findByPolicyId(@PathVariable Long policyId) {
        return service.findByPolicyId(policyId);
    }

    @PostMapping
    public ResponseEntity<CheckInCheckOutPolicyRuleDTO> save(@Valid @RequestBody CheckInCheckOutPolicyRuleDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    public CheckInCheckOutPolicyRuleDTO update(@PathVariable Long id, @Valid @RequestBody CheckInCheckOutPolicyRuleDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

