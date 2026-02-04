package com.caixa.test.caixatest.testutil;

import com.caixa.test.caixatest.dto.ClientDetailDTO;
import com.caixa.test.caixatest.dto.ChangeLoanStatusDTO;
import com.caixa.test.caixatest.dto.CreateLoanRequestDTO;
import com.caixa.test.caixatest.dto.CurrencyDTO;
import com.caixa.test.caixatest.dto.LoanRequestDTO;
import com.caixa.test.caixatest.entities.details.ClientDetail;
import com.caixa.test.caixatest.entities.details.LoanRequestDetail;
import com.caixa.test.caixatest.enums.LoadStatus;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Test utilities that provide sample objects for unit tests.
 */
public final class TestDataFactory {
    private TestDataFactory() {
    }

    public static ClientDetail clientDetail() {
        try {
            ClientDetail cd = new ClientDetail();
            setField(cd, "id", 1L);
            setField(cd, "fullName", "John Smith");
            setField(cd, "documentType", "DNI");
            setField(cd, "documentNumber", "12345678A");
            setField(cd, "createdAt", LocalDateTime.now());
            setField(cd, "updatedAt", LocalDateTime.now());
            return cd;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ClientDetailDTO clientDetailDto() {
        return new ClientDetailDTO("John Smith", "12345678A", "DNI");
    }

    public static LoanRequestDetail loanRequestDetail() {
        try {
            LoanRequestDetail ld = new LoanRequestDetail();
            setField(ld, "id", 10L);
            setField(ld, "amount", BigDecimal.valueOf(5000));
            setField(ld, "currency", "EUR");
            setField(ld, "status", "PENDING");
            setField(ld, "clientName", "John Smith");
            setField(ld, "clientDocumentType", "DNI");
            setField(ld, "documentNumber", "12345678A");
            setField(ld, "createdAt", LocalDateTime.now());
            setField(ld, "updatedAt", LocalDateTime.now());
            return ld;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    private static void setField(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }
}
