package com.beg.domain.services.processPdfData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExtractDataDTO {
    private LocalDate date;
    private String info;
    private Double amount;
    private Double balance;
}
