package com.beg.domain.repository;

import com.beg.domain.entities.Extract;

import java.util.List;

public interface IExtractRepositoryDatabase {
    void saveAllExtracts(List<Extract> extractList);
}
