package com.caixa.test.caixatest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateLoanRequestDTO(

        @NotNull(message = "Client is required") @Valid ClientDetailDTO client,

        @NotNull(message = "Amount is required") @DecimalMin(value = "100.00", message = "Minimum loan amount is 100.00") @DecimalMax(value = "50000.00", message = "Maximum loan amount is 50000.00") BigDecimal amount,

        @NotNull(message = "Currency is required") @Size(min = 3, max = 3, message = "Currency code must be 3 characters") String currencyCode) {
}