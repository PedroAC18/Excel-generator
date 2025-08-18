package com.beg.adapters.repository.database;

import com.beg.adapters.repository.springbootdata.IExtractRepositoryJPA;
import com.beg.domain.repository.IExtractRepositoryDatabase;
import com.beg.domain.services.ExtractDataDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExtractRepositoryDatabase implements IExtractRepositoryDatabase {

    private final IExtractRepositoryJPA extractRepositoryJPA;

    public ExtractRepositoryDatabase(IExtractRepositoryJPA iExtractRepositoryJPA) {
        this.extractRepositoryJPA = iExtractRepositoryJPA;
    }

    public void saveAllExtracts(List<ExtractDataDTO> extractDataDTO) {
        extractRepositoryJPA.saveAll(null);
    }
}
