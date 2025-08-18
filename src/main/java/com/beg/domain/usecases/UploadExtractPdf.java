package com.beg.domain.usecases;

import com.beg.domain.repository.IExtractRepositoryDatabase;
import com.beg.domain.services.ExtractDataDTO;
import com.beg.domain.services.ProcessPdfDataService;
import org.apache.coyote.BadRequestException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.text.ParseException;
import java.util.List;

@Service
public class UploadExtractPdf {

    private static final String INVALID_EXTENSION = "Invalid file format! It is necessary to be a pdf file!";

    private ProcessPdfDataService pdfDataService;
    private IExtractRepositoryDatabase extractRepositoryDatabase;

    public UploadExtractPdf(IExtractRepositoryDatabase extractRepositoryDatabase, ProcessPdfDataService pdfDataService) {
        this.extractRepositoryDatabase = extractRepositoryDatabase;
        this.pdfDataService = pdfDataService;
    }

    public void execute(File extractFile) throws BadRequestException, FileSystemException, ParseException {

        if (isPdf(extractFile)) {
            List<ExtractDataDTO> pdfExtractProcessed = pdfDataService.execute(extractFile);
            extractRepositoryDatabase.saveAllExtracts(pdfExtractProcessed);
        } else {
            throw new BadRequestException(INVALID_EXTENSION);
        }
    }

    private boolean isPdf(File extractFile) {
        try (PDDocument document = Loader.loadPDF(extractFile)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }


}
