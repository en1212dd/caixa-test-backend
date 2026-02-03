package com.caixa.test.caixatest.controller;

import com.caixa.test.caixatest.entities.ClientDetail;
import com.caixa.test.caixatest.service.ClientDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientDetailController {

    private final ClientDetailService service;

    public ClientDetailController(ClientDetailService service) {
        this.service = service;
    }

    @GetMapping("/details")
    public List<ClientDetail> getAllClientDetails() {
        return service.getAllClientDetails();
    }
}
