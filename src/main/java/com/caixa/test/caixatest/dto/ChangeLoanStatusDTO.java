package com.caixa.test.caixatest.dto;

import com.caixa.test.caixatest.enums.LoadStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeLoanStatusDTO(@NotNull LoadStatus status) {
}