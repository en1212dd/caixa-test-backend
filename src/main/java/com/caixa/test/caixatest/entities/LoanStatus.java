package com.caixa.test.caixatest.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "loan_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanStatus {

    @Id
    private Integer id;

    @Column(length = 20, nullable = false, unique = true)
    private String code;

    @Column(length = 100, nullable = false)
    private String description;
}