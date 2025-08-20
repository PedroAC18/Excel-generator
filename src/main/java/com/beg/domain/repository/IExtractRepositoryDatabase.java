package com.beg.domain.repository;

import com.beg.domain.entities.Extract;
import com.beg.domain.usecases.findAll.ExtractOutputDTO;

import java.util.List;

public interface IExtractRepositoryDatabase {
    void saveAllExtracts(List<Extract> extractList);

    List<ExtractOutputDTO> findAll();
}
