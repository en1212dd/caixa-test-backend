package com.caixa.test.caixatest.repository;

import com.caixa.test.caixatest.entities.ClientDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDetailRepository extends JpaRepository<ClientDetail, Long> {
    // read-only view repository
}
