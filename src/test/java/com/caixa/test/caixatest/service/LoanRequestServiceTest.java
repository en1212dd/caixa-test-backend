package com.caixa.test.caixatest.service;

import com.caixa.test.caixatest.dto.CreateLoanRequestDTO;
import com.caixa.test.caixatest.dto.LoanRequestDTO;
import com.caixa.test.caixatest.entities.Client;
import com.caixa.test.caixatest.entities.Currency;
import com.caixa.test.caixatest.entities.LoanRequest;
import com.caixa.test.caixatest.enums.LoadStatus;
import com.caixa.test.caixatest.repository.ClientRepository;
import com.caixa.test.caixatest.repository.CurrencyRepository;
import com.caixa.test.caixatest.repository.LoanRequestDetailRepository;
import com.caixa.test.caixatest.repository.LoanRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link LoanRequestService}.
 *
 * - Uses Mockito's @InjectMocks for the service and @Mock for repositories.
 * - Builds sample entities with Lombok builders where applicable at the top of
 * the class.
 * - Each test documents the scenario and asserts expected behavior.
 */
@ExtendWith(MockitoExtension.class)
class LoanRequestServiceTest {

    @Mock
    LoanRequestDetailRepository detailRepository;

    @Mock
    LoanRequestRepository requestRepository;

    @Mock
    ClientRepository clientRepository;

    @Mock
    CurrencyRepository currencyRepository;

    @InjectMocks
    LoanRequestService service;

    // Sample entities built once per test class using entity builders
    private Client sampleClient;
    private Currency sampleCurrency;
    private LoanRequest sampleLoan;

    @BeforeEach
    void setUp() {
        sampleClient = Client.builder()
                .id(1L)
                .fullName("John Smith")
                .documentNumber("12345678A")
                .build();
        // ensure document type is present because service mapping reads
        // documentType.code
        sampleClient.setDocumentType(
                com.caixa.test.caixatest.entities.DocumentType.builder().id(1).code("DNI").description("DNI").build());

        sampleCurrency = Currency.builder()
                .id(1L)
                .code("EUR")
                .description("Euro")
                .build();

        sampleLoan = LoanRequest.builder()
                .id(10L)
                .client(sampleClient)
                .amount(BigDecimal.valueOf(1000.00))
                .currency(sampleCurrency)
                .loanStatus(LoadStatus.PENDING)
                .build();
    }

    /**
     * Scenario: Creating a loan when client and currency exist and amount valid.
     * Expected: service.save() is called and returned DTO reflects created loan
     * with PENDING status.
     */
    @Test
    @DisplayName("createLoan() - success when client and currency exist")
    void createLoan_success() {
        var create = new CreateLoanRequestDTO(
                new com.caixa.test.caixatest.dto.ClientDetailDTO("John Smith", "12345678A", "DNI"),
                BigDecimal.valueOf(1000.00),
                "EUR");

        when(clientRepository.findByDocumentType_CodeAndDocumentNumber(any(), any()))
                .thenReturn(Optional.of(sampleClient));
        when(currencyRepository.findByCodeIgnoreCase("EUR")).thenReturn(Optional.of(sampleCurrency));
        when(requestRepository.save(any())).thenAnswer(invocation -> {
            var arg = invocation.getArgument(0, LoanRequest.class);
            arg.setId(10L);
            return arg;
        });

        LoanRequestDTO dto = service.createLoan(create);
        assertNotNull(dto);
        assertEquals("PENDING", dto.getStatus());
    }

    /**
     * Scenario: Getting a loan by id when not present.
     * Expected: empty Optional returned by service.
     */
    @Test
    @DisplayName("getLoanById() - returns empty when not found")
    void getLoanById_notFound() {
        when(requestRepository.findById(99L)).thenReturn(Optional.empty());
        var opt = service.getLoanById(99L);
        assertTrue(opt.isEmpty());
    }

    /**
     * Scenario: Changing status from PENDING to APPROVED (allowed transition).
     * Expected: status updated and returned DTO shows APPROVED.
     */
    @Test
    @DisplayName("changeStatus() - PENDING -> APPROVED succeeds")
    void changeStatus_pendingToApproved() {
        when(requestRepository.findById(10L)).thenReturn(Optional.of(sampleLoan));
        when(requestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var dto = service.changeStatus(10L, LoadStatus.APPROVED);
        assertNotNull(dto);
        assertEquals("APPROVED", dto.getStatus());
    }

    /**
     * Scenario: Attempting an invalid transition (PENDING -> CANCELLED).
     * Expected: service throws ResponseStatusException with BAD_REQUEST.
     */
    @Test
    @DisplayName("changeStatus() - invalid transition PENDING -> CANCELLED fails")
    void changeStatus_invalidTransition_throws() {
        when(requestRepository.findById(10L)).thenReturn(Optional.of(sampleLoan));

        assertThrows(ResponseStatusException.class, () -> service.changeStatus(10L, LoadStatus.CANCELLED));
    }
}
