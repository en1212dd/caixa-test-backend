package com.caixa.test.caixatest.controller;

import com.caixa.test.caixatest.dto.CreateLoanRequestDTO;
import com.caixa.test.caixatest.dto.LoanRequestDTO;
import com.caixa.test.caixatest.entities.details.LoanRequestDetail;
import com.caixa.test.caixatest.service.LoanRequestService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.security.access.prepost.PreAuthorize;

import com.caixa.test.caixatest.dto.ChangeLoanStatusDTO;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/loan-requests")
@Tag(name = "Loan Requests", description = "Operations related to loan requests")
public class LoanRequestController {

    private final LoanRequestService service;

    public LoanRequestController(LoanRequestService service) {
        this.service = service;
    }

    @Operation(summary = "Get detailed loan requests (admin only)", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/private/details")
    public List<LoanRequestDetail> getAllLoanRequestDetails() {
        return service.getAllLoanRequestDetails();
    }

    @Operation(summary = "Get loan request DTOs (admin only)", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/details")
    public List<LoanRequestDTO> getAllLoanRequestDtos() {
        return service.getAllLoanRequestDtos();
    }

    @Operation(summary = "Get a loan request by id (admin only)", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })

    @GetMapping("/{id}")
    public ResponseEntity<LoanRequestDTO> getLoanById(
            @PathVariable Long id) {
        return service.getLoanById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Loan not found"));
    }

    @Operation(summary = "Create a loan request", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PostMapping("/create")
    public ResponseEntity<LoanRequestDTO> createLoan(
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Create loan request payload", required = true) @RequestBody CreateLoanRequestDTO request,
            UriComponentsBuilder uriBuilder) {
        var created = service.createLoan(request);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change the status of a loan request (admin only)", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Invalid transition"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<LoanRequestDTO> changeStatus(@PathVariable Long id,
            @Valid @RequestBody ChangeLoanStatusDTO body) {
        var updated = service.changeStatus(id, body.status());
        return ResponseEntity.ok(updated);
    }
}
