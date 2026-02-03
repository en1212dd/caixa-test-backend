package com.caixa.test.caixatest.service;

import com.caixa.test.caixatest.entities.ClientDetail;
import com.caixa.test.caixatest.repository.ClientDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientDetailService {

    private final ClientDetailRepository repository;

    public ClientDetailService(ClientDetailRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ClientDetail> getAllClientDetails() {
        return repository.findAll();
    }
}
