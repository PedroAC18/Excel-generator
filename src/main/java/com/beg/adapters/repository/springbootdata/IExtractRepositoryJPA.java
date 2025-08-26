package com.beg.adapters.repository.springbootdata;

import com.beg.adapters.repository.springbootdata.model.ExtractModelJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IExtractRepositoryJPA extends JpaRepository<ExtractModelJPA, Long> {

    @Query("SELECT e " +
            "FROM ExtractModelJPA e " +
            "WHERE e.date >= :initialDate " +
            "AND e.date <= :finalDate")
    List<ExtractModelJPA> findByPeriod(@Param("initialDate") LocalDate initalDate, @Param("finalDate") LocalDate finalDate);
}
