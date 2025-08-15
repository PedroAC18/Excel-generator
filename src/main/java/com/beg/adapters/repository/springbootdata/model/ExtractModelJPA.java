package com.beg.adapters.repository.springbootdata.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"TB_EXTRACT\"", schema = "public")
public class ExtractModelJPA implements Serializable {

    @Id
    @Column(name = "\"ID\"")
    private Long id;

    @Column(name = "\"DATE\"")
    private LocalDate date;

    @Column(name = "\"INFO\"")
    private String info;

    @Column(name = "\"AMOUNT\"")
    private Double amount;

    @Column(name = "\"BALANCE\"")
    private Double balance;
}
