package com.beg.domain.services;

import com.beg.domain.services.processPdfData.ExtractDataDTO;
import com.beg.domain.services.processPdfData.ProcessPdfDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.FileSystemException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
public class ProcessPdfDataServiceTest {

    @Mock
    private ProcessPdfDataService service;

    private File mockFile;

    void mockServiceSuccess() throws FileSystemException, ParseException {

        List<ExtractDataDTO> dtoList = new ArrayList<>();
        ExtractDataDTO first = new ExtractDataDTO();
        first.setDate(LocalDate.of(2025, 8, 20));
        first.setInfo("PAYMENT TO JOHN DOE");
        first.setAmount(150.50);
        dtoList.add(first);

        ExtractDataDTO second = new ExtractDataDTO();
        second.setDate(LocalDate.of(2025, 8, 21));
        second.setInfo("SALDO DO DIA");
        second.setBalance(1250.75);
        dtoList.add(second);

        doReturn(dtoList).when(service).execute(mockFile);
    }

    void mockServiceWithInvalidDate() throws FileSystemException, ParseException {
        List<ExtractDataDTO> dtoList = new ArrayList<>();

        ExtractDataDTO valid = new ExtractDataDTO();
        valid.setDate(LocalDate.of(2025, 8, 20));
        valid.setInfo("VALID ROW");
        valid.setAmount(200.00);
        dtoList.add(valid);

        doReturn(dtoList).when(service).execute(mockFile);
    }

    void mockInvalidPdf() throws FileSystemException, ParseException {
        doThrow(new FileSystemException("Occurred an error trying to read the pdf file"))
                .when(service).execute(mockFile);
    }

    @BeforeEach
    void setup() throws FileSystemException, ParseException {
        mockFile = new File("test.pdf");
        mockServiceSuccess();
    }

    @Test
    void whenExecuteServiceReturnSuccess() throws FileSystemException, ParseException {
        List<ExtractDataDTO> result = service.execute(mockFile);

        assertEquals(2, result.size());

        ExtractDataDTO first = result.get(0);
        assertEquals(LocalDate.of(2025, 8, 20), first.getDate());
        assertEquals("PAYMENT TO JOHN DOE", first.getInfo());
        assertEquals(150.50, first.getAmount());
        assertNull(first.getBalance());

        ExtractDataDTO second = result.get(1);
        assertEquals(LocalDate.of(2025, 8, 21), second.getDate());
        assertEquals("SALDO DO DIA", second.getInfo());
        assertEquals(1250.75, second.getBalance());
        assertNull(second.getAmount());
    }

    @Test
    void whenExecuteServiceSkipInvalidDateRows() throws FileSystemException, ParseException {
        mockServiceWithInvalidDate();

        List<ExtractDataDTO> result = service.execute(mockFile);

        assertEquals(1, result.size());
        assertEquals(200.00, result.get(0).getAmount());
    }

    @Test
    void whenExecuteServiceThrowFileException() throws FileSystemException, ParseException {
        mockInvalidPdf();

        FileSystemException ex = assertThrows(FileSystemException.class, () -> service.execute(mockFile));
        assertTrue(ex.getMessage().contains("Occurred an error trying to read the pdf file"));
    }
}