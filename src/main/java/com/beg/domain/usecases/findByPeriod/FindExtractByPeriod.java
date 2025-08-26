package com.beg.domain.usecases.findByPeriod;

import com.beg.domain.repository.IExtractRepositoryDatabase;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FindExtractByPeriod {

    private static final String INVALID_DATE = "Final date cannot be previous than initial date";

    private final IExtractRepositoryDatabase extractRepositoryDatabase;

    public FindExtractByPeriod(IExtractRepositoryDatabase repositoryDatabase) {
        this.extractRepositoryDatabase = repositoryDatabase;
    }

    public List<ExtractByPeriodOutputDTO> execute(LocalDate initialDate, LocalDate finalDate) throws BadRequestException {
        if(!isDateValid(initialDate, finalDate)){
            throw new BadRequestException(INVALID_DATE);
        }
        return extractRepositoryDatabase.findByPeriod(initialDate, finalDate);
    }

    private boolean isDateValid(LocalDate initialDate, LocalDate finalDate) {
        return finalDate.isAfter(initialDate);
    }
}
