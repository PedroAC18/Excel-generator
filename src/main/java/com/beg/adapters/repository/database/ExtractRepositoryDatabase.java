package com.beg.adapters.repository.database;

import com.beg.adapters.repository.springbootdata.IExtractRepositoryJPA;
import com.beg.domain.repository.IExtractRepositoryDatabase;
import org.springframework.stereotype.Repository;

@Repository
public class ExtractRepositoryDatabase implements IExtractRepositoryDatabase {

    private final IExtractRepositoryJPA extractRepositoryJPA;

    public ExtractRepositoryDatabase(IExtractRepositoryJPA iExtractRepositoryJPA) {
        this.extractRepositoryJPA = iExtractRepositoryJPA;
    }

    public void saveAllExtracts() {
        extractRepositoryJPA.saveAll(null);
    }
}
