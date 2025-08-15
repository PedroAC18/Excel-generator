package com.beg.domain.usecases;

import com.beg.domain.repository.IExtractRepositoryDatabase;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class UploadExtractPdf {
    private IExtractRepositoryDatabase extractRepositoryDatabase;

    public UploadExtractPdf(IExtractRepositoryDatabase extractRepositoryDatabase) {
        this.extractRepositoryDatabase = extractRepositoryDatabase;
    }

    public void execute(File pdf) {
        extractRepositoryDatabase.saveAllExtracts();
    }
}
