package com.caixa.test.caixatest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestDTO {
    private Long id;
    private ClientDetailDTO client;
    private CurrencyDTO currency;
    private String status;
}