package com.caixa.test.caixatest.enums;

public enum LoadStatus {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    CANCELLED("CANCELLED");

    private final String code;

    LoadStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
