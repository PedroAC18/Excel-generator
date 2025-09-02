package com.beg.adapters.repository.database;

import com.beg.adapters.repository.springbootdata.IExtractRepositoryJPA;
import com.beg.adapters.repository.springbootdata.model.ExtractModelJPA;
import com.beg.domain.entities.Extract;
import com.beg.domain.repository.IExtractRepositoryDatabase;
import com.beg.domain.usecases.findAll.ExtractOutputDTO;
import com.beg.domain.services.findByPeriod.ExtractByPeriodOutputDTO;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    @Override
    public List<ExtractOutputDTO> findAll() {
        List<ExtractModelJPA> modelJpaList = extractRepositoryJPA.findAll();
        return buildExtractOutputDto(modelJpaList);
    }

    private List<ExtractOutputDTO> buildExtractOutputDto(List<ExtractModelJPA> modelJpaList) {
        List<ExtractOutputDTO> outputDTOList = new ArrayList<>();

        for (ExtractModelJPA jpa : modelJpaList) {
            ExtractOutputDTO dto = new ExtractOutputDTO();
            dto.setDate(jpa.getDate());
            dto.setInfo(jpa.getInfo());

            if (jpa.getBalance() != null) {
                dto.setBalance(jpa.getBalance());
            } else {
                dto.setAmount(jpa.getAmount());
            }
            outputDTOList.add(dto);
        }

        return outputDTOList;
    }

    @Override
    public List<ExtractByPeriodOutputDTO> findByPeriod(LocalDate initialDate, LocalDate finalDate) {
        List<ExtractModelJPA> modelJPAList = extractRepositoryJPA.findByPeriod(initialDate, finalDate);
        return buildExtractByDateOutputDTO(modelJPAList);
    }

    private List<ExtractByPeriodOutputDTO> buildExtractByDateOutputDTO(List<ExtractModelJPA> modelJPAList) {
        List<ExtractByPeriodOutputDTO> outputDTOList = new ArrayList<>();

        for (ExtractModelJPA jpa : modelJPAList) {
            ExtractByPeriodOutputDTO dto = new ExtractByPeriodOutputDTO();
            dto.setDate(jpa.getDate());
            dto.setInfo(jpa.getInfo());

            if (jpa.getBalance() != null) {
                dto.setBalance(jpa.getBalance());
            } else {
                dto.setAmount(jpa.getAmount());
            }
            outputDTOList.add(dto);
        }

        return outputDTOList;
    }
}
