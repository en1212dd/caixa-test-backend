package com.caixa.test.caixatest.service;

import com.caixa.test.caixatest.dto.ClientDetailDTO;
import com.caixa.test.caixatest.entities.details.ClientDetail;
import com.caixa.test.caixatest.repository.ClientDetailRepository;
import com.caixa.test.caixatest.testutil.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ClientDetailService}.
 *
 * - Uses Mockito's @InjectMocks to instantiate the service under test and @Mock
 * for the repository.
 * - Sample data is provided by `TestDataFactory` to keep test setup clear and
 * reusable.
 */
@ExtendWith(MockitoExtension.class)
class ClientDetailServiceTest {

    @Mock
    ClientDetailRepository repository;

    @InjectMocks
    ClientDetailService service;

    private ClientDetail sampleDetail;
    private ClientDetailDTO sampleDto;

    @BeforeEach
    void setUp() {
        // Build sample objects used by tests
        sampleDetail = TestDataFactory.clientDetail();
        sampleDto = TestDataFactory.clientDetailDto();
    }

    /**
     * Verifies that `getAllClientDetails()` returns the list coming from the
     * repository.
     */
    @Test
    @DisplayName("getAllClientDetails() returns repository data")
    void getAllClientDetails_returnsRepositoryList() {
        when(repository.findAll()).thenReturn(List.of(sampleDetail));

        var result = service.getAllClientDetails();

        assertEquals(1, result.size());
        assertEquals("John Smith", result.get(0).getFullName());
    }

    /**
     * Verifies that `getAllClientDetailsDto()` maps repository view entities to
     * DTOs correctly.
     */
    @Test
    @DisplayName("getAllClientDetailsDto() maps to DTOs")
    void getAllClientDetailsDto_mapsCorrectly() {
        when(repository.findAll()).thenReturn(List.of(sampleDetail));

        var dtos = service.getAllClientDetailsDto();

        assertEquals(1, dtos.size());
        ClientDetailDTO dto = dtos.get(0);
        assertEquals(sampleDto.getFullName(), dto.getFullName());
        assertEquals(sampleDto.getDocumentNumber(), dto.getDocumentNumber());
        assertEquals(sampleDto.getDocumentType(), dto.getDocumentType());
    }
}
