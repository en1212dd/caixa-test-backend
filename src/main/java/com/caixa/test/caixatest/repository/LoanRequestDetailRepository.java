package com.caixa.test.caixatest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.caixa.test.caixatest.entities.details.LoanRequestDetail;

@Repository
public interface LoanRequestDetailRepository extends JpaRepository<LoanRequestDetail, Long> {
}
