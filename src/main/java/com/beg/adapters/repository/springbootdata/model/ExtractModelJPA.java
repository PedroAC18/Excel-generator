package com.beg.adapters.repository.springbootdata.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "\"TB_EXTRACT\"", schema = "public")
public class ExtractModelJPA implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
