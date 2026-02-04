package com.caixa.test.caixatest.service;

import com.caixa.test.caixatest.dto.ClientDetailDTO;
import com.caixa.test.caixatest.entities.details.ClientDetail;
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

    @Transactional(readOnly = true)
    public List<ClientDetailDTO> getAllClientDetailsDto() {
        return repository.findAll().stream()
                .map(cd -> new ClientDetailDTO(cd.getFullName(), cd.getDocumentNumber(),
                        cd.getDocumentType()))
                .toList();
    }
}
