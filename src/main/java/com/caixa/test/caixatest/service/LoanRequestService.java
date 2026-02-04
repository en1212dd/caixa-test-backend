package com.caixa.test.caixatest.service;

import com.caixa.test.caixatest.dto.ClientDetailDTO;
import com.caixa.test.caixatest.dto.CreateLoanRequestDTO;
import com.caixa.test.caixatest.dto.CurrencyDTO;
import com.caixa.test.caixatest.dto.LoanRequestDTO;
import com.caixa.test.caixatest.entities.LoanRequest;
import com.caixa.test.caixatest.entities.details.LoanRequestDetail;
import com.caixa.test.caixatest.enums.LoadStatus;
import com.caixa.test.caixatest.repository.ClientRepository;
import com.caixa.test.caixatest.repository.CurrencyRepository;
import com.caixa.test.caixatest.repository.LoanRequestDetailRepository;
import com.caixa.test.caixatest.repository.LoanRequestRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LoanRequestService {

    private final LoanRequestDetailRepository detailRepository;
    private final LoanRequestRepository requestRepository;
    private final ClientRepository clientRepository;
    private final CurrencyRepository currencyRepository;

    public LoanRequestService(LoanRequestDetailRepository detailRepository, LoanRequestRepository requestRepository,
            ClientRepository clientRepository,
            CurrencyRepository currencyRepository) {
        this.detailRepository = detailRepository;
        this.requestRepository = requestRepository;
        this.clientRepository = clientRepository;
        this.currencyRepository = currencyRepository;
    }

    @Transactional(readOnly = true)
    public List<LoanRequestDetail> getAllLoanRequestDetails() {
        return detailRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<LoanRequestDTO> getAllLoanRequestDtos() {
        return requestRepository.findAll().stream().map(r -> {
            var clientDto = new ClientDetailDTO(r.getClient().getFullName(), r.getClient().getDocumentNumber(),
                    r.getClient().getDocumentType().getCode());
            var currencyDto = new CurrencyDTO(r.getCurrency().getCode(), r.getCurrency().getDescription());
            return new LoanRequestDTO(r.getId(), clientDto, currencyDto, r.getLoanStatus().getCode());
        }).toList();
    }

    @Transactional
    public LoanRequestDTO createLoan(CreateLoanRequestDTO request) {
        // validate amount
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Amount must be greater than 0");
        }

        // check client exists
        var clientOpt = clientRepository.findByDocumentType_CodeAndDocumentNumber(request.client().getDocumentType(),
                request.client().getDocumentNumber());
        var client = clientOpt.orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Client not found"));

        // check currency exists
        var currency = currencyRepository.findByCodeIgnoreCase(request.currencyCode())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Currency not found"));

        // create loan
        var loan = new LoanRequest();
        loan.setClient(client);
        loan.setAmount(request.amount());
        loan.setCurrency(currency);
        loan.setLoanStatus(LoadStatus.PENDING);

        var saved = requestRepository.save(loan);

        var clientDto = new ClientDetailDTO(saved.getClient().getFullName(), saved.getClient().getDocumentNumber(),
                saved.getClient().getDocumentType().getCode());
        var currencyDto = new CurrencyDTO(saved.getCurrency().getCode(), saved.getCurrency().getDescription());
        var s = saved.getLoanStatus() != null ? saved.getLoanStatus().getCode() : null;
        return new LoanRequestDTO(saved.getId(), clientDto, currencyDto, s);
    }
}