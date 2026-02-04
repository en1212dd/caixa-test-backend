package com.caixa.test.caixatest.controller;

import com.caixa.test.caixatest.dto.ClientDetailDTO;
import com.caixa.test.caixatest.entities.details.ClientDetail;
import com.caixa.test.caixatest.service.ClientDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Operations related to clients")
public class ClientDetailController {

    private final ClientDetailService service;

    public ClientDetailController(ClientDetailService service) {
        this.service = service;
    }

    @Operation(summary = "Get all client details (admin only)", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/private/details")
    public List<ClientDetail> getAllClientDetails() {
        return service.getAllClientDetails();
    }

    @Operation(summary = "Get client DTOs", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/details")
    public List<ClientDetailDTO> getAllClientDetailsDto() {
        return service.getAllClientDetailsDto();
    }
}
