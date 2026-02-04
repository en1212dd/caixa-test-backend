package com.caixa.test.caixatest.testutil;

import com.caixa.test.caixatest.dto.ClientDetailDTO;
import com.caixa.test.caixatest.dto.ChangeLoanStatusDTO;
import com.caixa.test.caixatest.dto.CreateLoanRequestDTO;
import com.caixa.test.caixatest.dto.CurrencyDTO;
import com.caixa.test.caixatest.dto.LoanRequestDTO;
import com.caixa.test.caixatest.entities.Client;
import com.caixa.test.caixatest.entities.Currency;
import com.caixa.test.caixatest.entities.DocumentType;
import com.caixa.test.caixatest.entities.LoanRequest;
import com.caixa.test.caixatest.entities.details.ClientDetail;
import com.caixa.test.caixatest.entities.details.LoanRequestDetail;
import com.caixa.test.caixatest.enums.LoadStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Test utilities that provide sample objects for unit tests.
 */
public final class TestDataFactory {
    private TestDataFactory() {
    }

    public static ClientDetail clientDetail() {
        return ClientDetail.builder()
                .id(1L)
                .fullName("John Smith")
                .documentType("DNI")
                .documentNumber("12345678A")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static ClientDetailDTO clientDetailDto() {
        return new ClientDetailDTO("John Smith", "12345678A", "DNI");
    }

    public static Client client() {
        var sampleClient = Client.builder()
                .id(1L)
                .fullName("John Smith")
                .documentNumber("12345678A")
                .build();
        // ensure document type is present because service mapping reads
        // documentType.code
        sampleClient.setDocumentType(
                DocumentType.builder().id(1).code("DNI").description("DNI").build());
        return sampleClient;
    }

    public static Currency currency() {
        return Currency.builder()
                .id(1L)
                .code("EUR")
                .description("Euro")
                .build();
    }

    public static LoanRequest loanRequest() {
        return LoanRequest.builder()
                .id(10L)
                .client(client())
                .amount(BigDecimal.valueOf(1000.00))
                .currency(currency())
                .loanStatus(LoadStatus.PENDING)
                .build();
    }

    public static LoanRequestDetail loanRequestDetail() {
        return LoanRequestDetail.builder()
                .id(10L)
                .amount(BigDecimal.valueOf(5000))
                .currency("EUR")
                .status("PENDING")
                .clientName("John Smith")
                .clientDocumentType("DNI")
                .documentNumber("12345678A")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static LoanRequestDTO loanRequestDto() {
        return new LoanRequestDTO(10L, clientDetailDto(), new CurrencyDTO("EUR", "Euro"), "PENDING");
    }

    public static CreateLoanRequestDTO createLoanRequestDto() {
        return new CreateLoanRequestDTO(clientDetailDto(), BigDecimal.valueOf(1000.00), "EUR");
    }

    public static ChangeLoanStatusDTO changeLoanStatusDto(LoadStatus status) {
        return new ChangeLoanStatusDTO(status);
    }

}
