package com.beg.domain.usecases.findByPeriod;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ExtractByPeriodOutputDTO {
    private LocalDate date;
    private String info;
    private Double amount;
    private Double balance;
}
