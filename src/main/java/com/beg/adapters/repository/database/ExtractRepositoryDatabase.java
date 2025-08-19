package com.beg.adapters.repository.database;

import com.beg.adapters.repository.springbootdata.IExtractRepositoryJPA;
import com.beg.adapters.repository.springbootdata.model.ExtractModelJPA;
import com.beg.domain.entities.Extract;
import com.beg.domain.repository.IExtractRepositoryDatabase;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ExtractRepositoryDatabase implements IExtractRepositoryDatabase {

    private final IExtractRepositoryJPA extractRepositoryJPA;

    public ExtractRepositoryDatabase(IExtractRepositoryJPA iExtractRepositoryJPA) {
        this.extractRepositoryJPA = iExtractRepositoryJPA;
    }

    public void saveAllExtracts(List<Extract> extractList) {
        List<ExtractModelJPA> extractModelJPAList = buildExtractJPAList(extractList);
        extractRepositoryJPA.saveAll(extractModelJPAList);
    }

    private List<ExtractModelJPA> buildExtractJPAList(List<Extract> extractList) {
        List<ExtractModelJPA> extractModelJPAList = new ArrayList<>();

        for (Extract extract : extractList) {
            ExtractModelJPA modelJPA = new ExtractModelJPA();
            modelJPA.setDate(extract.getDate());
            modelJPA.setInfo(extract.getInfo());

            if (extract.getBalance() != null) {
                modelJPA.setBalance(extract.getBalance());
            } else {
                modelJPA.setAmount(extract.getAmount());
            }
            extractModelJPAList.add(modelJPA);
        }

        return extractModelJPAList;
    }
}
