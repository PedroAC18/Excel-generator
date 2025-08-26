package com.beg.domain.usecases;

import com.beg.domain.repository.IExtractRepositoryDatabase;
import com.beg.domain.usecases.findAll.ExtractOutputDTO;
import com.beg.domain.usecases.findAll.FindAllExtract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class FindAllExtractTest {

    @Mock
    private IExtractRepositoryDatabase extractRepositoryDatabase;

    private FindAllExtract findAllExtract;

    @BeforeEach
    void setUp() {
        findAllExtract = new FindAllExtract(extractRepositoryDatabase);
    }

    @Test
    @DisplayName("Should return a list of extracts when repository has data")
    void shouldReturnListOfExtracts() {
        List<ExtractOutputDTO> extractList = new ArrayList();

        ExtractOutputDTO extract1 = new ExtractOutputDTO();
        extract1.setDate(LocalDate.of(2025, 8, 20));
        extract1.setInfo("Deposit");
        extract1.setAmount(1000.0);
        extract1.setBalance(1000.0);
        extractList.add(extract1);

        ExtractOutputDTO extract2 = new ExtractOutputDTO();
        extract2.setDate(LocalDate.of(2025, 8, 21));
        extract2.setInfo("Withdrawal");
        extract2.setAmount(-500.0);
        extract2.setBalance(500.0);
        extractList.add(extract2);

        when(extractRepositoryDatabase.findAll()).thenReturn(extractList);

        List<ExtractOutputDTO> result = findAllExtract.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(extractList, result);
        verify(extractRepositoryDatabase).findAll();
    }

    @Test
    @DisplayName("Should return an empty list when repository has no data")
    void shouldReturnEmptyList() {
        List<ExtractOutputDTO> emptyList = Collections.emptyList();
        when(extractRepositoryDatabase.findAll()).thenReturn(emptyList);

        List<ExtractOutputDTO> result = findAllExtract.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(extractRepositoryDatabase).findAll();
    }

    @Test
    @DisplayName("Should pass through any exception thrown by the repository")
    void shouldPassThroughExceptions() {
        when(extractRepositoryDatabase.findAll()).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            findAllExtract.execute();
        });

        assertEquals("Database error", exception.getMessage());
        verify(extractRepositoryDatabase).findAll();
    }
}
