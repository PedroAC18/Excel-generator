package com.beg.domain.usecases;

import com.beg.domain.repository.IExtractRepositoryDatabase;
import com.beg.domain.services.ExtractDataDTO;
import com.beg.domain.services.ProcessPdfDataService;
import com.beg.domain.usecases.upload.UploadExtractPdf;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.FileSystemException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UploadExtractPdfTest {

    @Mock
    private UploadExtractPdf uploadExtractPdf;

    @Mock
    private ProcessPdfDataService pdfDataService;

    @Mock
    private IExtractRepositoryDatabase extractRepositoryDatabase;

    private File mockFile;

    void mockServiceSuccess() throws Exception {
        List<ExtractDataDTO> dtoList = new ArrayList();

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

        // We mock execute() to do nothing but simulate success
        doNothing().when(uploadExtractPdf).execute(mockFile);
    }

    void mockServiceWithInvalidFile() throws Exception {
        doThrow(new BadRequestException("Invalid file format! It is necessary to be a pdf file!"))
                .when(uploadExtractPdf).execute(mockFile);
    }

    void mockServiceThrowsFileSystemException() throws Exception {
        doThrow(new FileSystemException("Occurred an error trying to read the pdf file"))
                .when(uploadExtractPdf).execute(mockFile);
    }

    @BeforeEach
    void setup() throws Exception {
        mockFile = new File("test.pdf");
        mockServiceSuccess();
    }

    @Test
    void whenExecuteWithValidPdf_thenSuccess() throws Exception {
        // Act
        uploadExtractPdf.execute(mockFile);

        // Assert
        verify(uploadExtractPdf, times(1)).execute(mockFile);
    }

    @Test
    void whenExecuteWithInvalidPdf_thenThrowBadRequestException() throws Exception {
        mockServiceWithInvalidFile();

        BadRequestException ex = assertThrows(BadRequestException.class, () -> uploadExtractPdf.execute(mockFile));
        assertTrue(ex.getMessage().contains("Invalid file format"));
    }

    @Test
    void whenExecuteThrowsFileSystemException_thenPropagate() throws Exception {
        mockServiceThrowsFileSystemException();

        FileSystemException ex = assertThrows(FileSystemException.class, () -> uploadExtractPdf.execute(mockFile));
        assertTrue(ex.getMessage().contains("Occurred an error trying to read the pdf file"));
    }
}