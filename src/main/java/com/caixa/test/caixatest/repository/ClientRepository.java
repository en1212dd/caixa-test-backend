package com.caixa.test.caixatest.repository;

import com.caixa.test.caixatest.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByDocumentType_CodeAndDocumentNumber(String documentTypeCode, String documentNumber);
}
