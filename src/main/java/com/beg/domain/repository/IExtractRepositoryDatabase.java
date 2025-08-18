package com.beg.domain.repository;

import com.beg.domain.services.ExtractDataDTO;

import java.util.List;

public interface IExtractRepositoryDatabase {
    void saveAllExtracts(List<ExtractDataDTO> extractDataList);
}
