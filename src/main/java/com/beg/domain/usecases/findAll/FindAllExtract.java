package com.beg.domain.usecases.findAll;

import com.beg.domain.repository.IExtractRepositoryDatabase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllExtract {

    private IExtractRepositoryDatabase extractRepositoryDatabase;

    public FindAllExtract(IExtractRepositoryDatabase iExtractRepositoryDatabase) {
        this.extractRepositoryDatabase = iExtractRepositoryDatabase;
    }

    public List<ExtractOutputDTO> execute() {
        return extractRepositoryDatabase.findAll();
    }
}
