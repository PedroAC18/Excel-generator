package com.beg.domain.usecases.upload;

import com.beg.domain.entities.Extract;
import com.beg.domain.repository.IExtractRepositoryDatabase;
import com.beg.domain.services.processPdfData.ExtractDataDTO;
import com.beg.domain.services.processPdfData.ProcessPdfDataService;
import org.apache.coyote.BadRequestException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.text.ParseException;
import java.util.ArrayList;
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
            List<Extract> extractList = buildExtract(pdfExtractProcessed);
            extractRepositoryDatabase.saveAllExtracts(extractList);
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

    private List<Extract> buildExtract(List<ExtractDataDTO> pdfExtractProcessed) {
        List<Extract> extractList = new ArrayList<>();
        for (ExtractDataDTO dto : pdfExtractProcessed) {
            Extract extract = new Extract();
            extract.setDate(dto.getDate());
            extract.setInfo(dto.getInfo());

            if (dto.getBalance() != null) {
                extract.setBalance(dto.getBalance());
            } else {
                extract.setAmount(dto.getAmount());
            }
            extractList.add(extract);
        }
        return extractList;
    }


}
