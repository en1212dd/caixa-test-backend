package com.caixa.test.caixatest.repository;

import com.caixa.test.caixatest.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findByCodeIgnoreCase(String code);
}
