package com.project.contractflow.controller;

import com.project.contractflow.dto.QuestionRequest;
import com.project.contractflow.dto.StatusUpdateRequest;
import com.project.contractflow.entity.Contract;
import com.project.contractflow.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
@Tag(name = "Contract APIs", description = "APIs for contract management")
public class ContractController {

    private final ContractService contractService;

    @Operation(summary = "Upload a new contract")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createContract(@RequestParam("title") String title, @RequestPart("file") MultipartFile file) throws IOException {
        Contract contract = contractService.createContract(title, file);
        return ResponseEntity.ok(contract);
    }

    @Operation(summary = "Fetch contract details")
    @GetMapping("/{id}")
    public ResponseEntity<?> getContract(@PathVariable Long id) {
        Contract contract = contractService.getContract(id);
        return ResponseEntity.ok(contract);
    }

    @Operation(summary = "Update contract status")
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        Contract updatedContract = contractService.updateStatus(id, request);
        return ResponseEntity.ok(updatedContract);
    }

    @Operation(summary = "Ask questions about contract")
    @PostMapping("/{id}/ask")
    public ResponseEntity<?> getAsk(@PathVariable Long id, @Valid @RequestBody QuestionRequest request) {
        String answer = contractService.askQuestion(id, request);
        return ResponseEntity.ok(answer);
    }

}
