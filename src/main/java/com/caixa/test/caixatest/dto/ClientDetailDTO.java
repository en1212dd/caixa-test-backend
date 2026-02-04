package com.caixa.test.caixatest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDetailDTO {
    @NotBlank(message = "Name is required")
    private String fullName;
    @NotBlank(message = "Document number is required")
    private String documentNumber;
    @NotBlank(message = "Document type is required")
    private String documentType;
}
