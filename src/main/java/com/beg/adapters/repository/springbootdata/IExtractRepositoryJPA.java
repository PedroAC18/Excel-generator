package com.beg.adapters.repository.springbootdata;

import com.beg.adapters.repository.springbootdata.model.ExtractModelJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExtractRepositoryJPA extends JpaRepository<ExtractModelJPA, Long> {
}
