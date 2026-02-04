package com.caixa.test.caixatest.entities.details;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "v_loan_request_detail")
@Immutable
@Getter
public class LoanRequestDetail {

    @Id
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "status")
    private String status;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_document_type")
    private String clientDocumentType;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
