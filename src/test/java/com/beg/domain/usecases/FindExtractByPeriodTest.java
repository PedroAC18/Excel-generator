package com.beg.domain.usecases;

import com.beg.domain.repository.IExtractRepositoryDatabase;
import com.beg.domain.services.findByPeriod.ExtractByPeriodOutputDTO;
import com.beg.domain.services.findByPeriod.FindExtractByPeriod;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FindExtractByPeriodTest {

    @Mock
    private IExtractRepositoryDatabase extractRepositoryDatabase;

    @InjectMocks
    private FindExtractByPeriod findExtractByPeriod;

    private LocalDate validInitialDate;
    private LocalDate validFinalDate;
    private LocalDate invalidFinalDate;
    private List<ExtractByPeriodOutputDTO> mockExtractList;

    @BeforeEach
    void setup() {
        validInitialDate = LocalDate.of(2025, 8, 20);
        validFinalDate = LocalDate.of(2025, 8, 25);
        invalidFinalDate = LocalDate.of(2025, 8, 15);

        mockExtractList = new ArrayList<>();

        ExtractByPeriodOutputDTO firstExtract = new ExtractByPeriodOutputDTO();
        firstExtract.setDate(LocalDate.of(2025, 8, 21));
        firstExtract.setInfo("PAYMENT TO JOHN DOE");
        firstExtract.setAmount(150.50);
        mockExtractList.add(firstExtract);

        ExtractByPeriodOutputDTO secondExtract = new ExtractByPeriodOutputDTO();
        secondExtract.setDate(LocalDate.of(2025, 8, 22));
        secondExtract.setInfo("SALDO DO DIA");
        secondExtract.setBalance(1250.75);
        mockExtractList.add(secondExtract);

        when(extractRepositoryDatabase.findByPeriod(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(mockExtractList);
    }

    @Test
    void whenExecuteWithValidDateRange_thenSuccess() throws BadRequestException {
        List<ExtractByPeriodOutputDTO> result = findExtractByPeriod.execute(validInitialDate, validFinalDate);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(extractRepositoryDatabase, times(1)).findByPeriod(validInitialDate, validFinalDate);
    }

    @Test
    void whenExecuteWithInvalidDateRange_thenThrowBadRequestException() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            findExtractByPeriod.execute(validInitialDate, invalidFinalDate);
        });

        assertEquals("Final date cannot be previous than initial date", exception.getMessage());
        verify(extractRepositoryDatabase, never()).findByPeriod(any(), any());
    }

    @Test
    void whenRepositoryThrowsException_thenPropagateException() throws BadRequestException {
        when(extractRepositoryDatabase.findByPeriod(any(LocalDate.class), any(LocalDate.class)))
                .thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            findExtractByPeriod.execute(validInitialDate, validFinalDate);
        });

        assertEquals("Database error", exception.getMessage());
        verify(extractRepositoryDatabase, times(1)).findByPeriod(validInitialDate, validFinalDate);
    }

    @Test
    void whenDatesAreEqual_thenThrowBadRequestException() {
        LocalDate sameDate = LocalDate.of(2025, 8, 20);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            findExtractByPeriod.execute(sameDate, sameDate);
        });

        assertEquals("Final date cannot be previous than initial date", exception.getMessage());
        verify(extractRepositoryDatabase, never()).findByPeriod(any(), any());
    }
}
