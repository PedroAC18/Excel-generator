package com.beg.domain.usecases;

import com.beg.domain.services.findByPeriod.ExtractByPeriodOutputDTO;
import com.beg.domain.services.findByPeriod.FindExtractByPeriod;
import com.beg.domain.usecases.createExcel.CreateExtractExcel;
import org.apache.coyote.BadRequestException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateExtractExcelTest {

    @Mock
    private FindExtractByPeriod findExtractByPeriod;

    @InjectMocks
    private CreateExtractExcel createExtractExcel;

    private LocalDate initialDate;
    private LocalDate finalDate;
    private List<ExtractByPeriodOutputDTO> mockExtractData;

    @BeforeEach
    void setUp() {
        initialDate = LocalDate.of(2025, 8, 1);
        finalDate = LocalDate.of(2025, 8, 31);

        ExtractByPeriodOutputDTO entry1 = new ExtractByPeriodOutputDTO();
        entry1.setDate(LocalDate.of(2025, 8, 10));
        entry1.setInfo("Depósito");
        entry1.setAmount(1000.00);
        entry1.setBalance(null);

        ExtractByPeriodOutputDTO entry2 = new ExtractByPeriodOutputDTO();
        entry2.setDate(LocalDate.of(2025, 8, 15));
        entry2.setInfo("Pagamento");
        entry2.setAmount(-250.50);
        entry2.setBalance(null);

        ExtractByPeriodOutputDTO entry3 = new ExtractByPeriodOutputDTO();
        entry3.setDate(LocalDate.of(2025, 8, 20));
        entry3.setInfo("Saldo atual");
        entry3.setAmount(null);
        entry3.setBalance(1249.50);

        mockExtractData = Arrays.asList(entry1, entry2, entry3);
    }

    @Test
    void shouldGenerateExcelFileWithExtractData() throws BadRequestException, IOException {
        when(findExtractByPeriod.execute(initialDate, finalDate)).thenReturn(mockExtractData);

        byte[] excelBytes = createExtractExcel.execute(initialDate, finalDate);

        assertNotNull(excelBytes);
        assertTrue(excelBytes.length > 0);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelBytes))) {
            Sheet sheet = workbook.getSheet("Extrato");
            assertNotNull(sheet);

            Row periodRow = sheet.getRow(0);
            assertNotNull(periodRow);
            assertEquals("Período: 01/08/2025 a 31/08/2025", periodRow.getCell(0).getStringCellValue());

            Row headerRow = sheet.getRow(1);
            assertNotNull(headerRow);
            assertEquals("Data", headerRow.getCell(0).getStringCellValue());
            assertEquals("Informação", headerRow.getCell(1).getStringCellValue());
            assertEquals("Valor", headerRow.getCell(2).getStringCellValue());
            assertEquals("Saldo", headerRow.getCell(3).getStringCellValue());

            assertEquals(3, mockExtractData.size());

            Row row1 = sheet.getRow(2);
            assertNotNull(row1);
            assertEquals("10/08/2025", row1.getCell(0).getStringCellValue());
            assertEquals("Depósito", row1.getCell(1).getStringCellValue());
            assertEquals(1000.00, row1.getCell(2).getNumericCellValue(), 0.001);

            Cell balanceCell1 = row1.getCell(3);
            if (balanceCell1 != null) {
                assertEquals(CellType.BLANK, balanceCell1.getCellType(), "A célula de saldo deve estar vazia");
            }

            Row row2 = sheet.getRow(3);
            assertNotNull(row2);
            assertEquals("15/08/2025", row2.getCell(0).getStringCellValue());
            assertEquals("Pagamento", row2.getCell(1).getStringCellValue());
            assertEquals(-250.50, row2.getCell(2).getNumericCellValue(), 0.001);

            Cell balanceCell2 = row2.getCell(3);
            if (balanceCell2 != null) {
                assertEquals(CellType.BLANK, balanceCell2.getCellType(), "A célula de saldo deve estar vazia");
            }

            Row row3 = sheet.getRow(4);
            assertNotNull(row3);
            assertEquals("20/08/2025", row3.getCell(0).getStringCellValue());
            assertEquals("Saldo atual", row3.getCell(1).getStringCellValue());

            Cell amountCell3 = row3.getCell(2);
            if (amountCell3 != null) {
                assertEquals(CellType.BLANK, amountCell3.getCellType(), "A célula de valor deve estar vazia");
            }

            assertEquals(1249.50, row3.getCell(3).getNumericCellValue(), 0.001);
        }

        verify(findExtractByPeriod, times(1)).execute(initialDate, finalDate);
    }

    @Test
    void shouldGenerateEmptyExcelFileWhenNoData() throws BadRequestException, IOException {
        when(findExtractByPeriod.execute(initialDate, finalDate)).thenReturn(Collections.emptyList());

        byte[] excelBytes = createExtractExcel.execute(initialDate, finalDate);

        assertNotNull(excelBytes);
        assertTrue(excelBytes.length > 0);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelBytes))) {
            Sheet sheet = workbook.getSheet("Extrato");
            assertNotNull(sheet);

            Row periodRow = sheet.getRow(0);
            assertNotNull(periodRow);
            assertEquals("Período: 01/08/2025 a 31/08/2025", periodRow.getCell(0).getStringCellValue());

            Row headerRow = sheet.getRow(1);
            assertNotNull(headerRow);
            assertEquals("Data", headerRow.getCell(0).getStringCellValue());
            assertEquals("Informação", headerRow.getCell(1).getStringCellValue());
            assertEquals("Valor", headerRow.getCell(2).getStringCellValue());
            assertEquals("Saldo", headerRow.getCell(3).getStringCellValue());

            assertNull(sheet.getRow(2));
        }

        verify(findExtractByPeriod, times(1)).execute(initialDate, finalDate);
    }

    @Test
    void shouldHandleNullValuesInExtractData() throws BadRequestException, IOException {
        ExtractByPeriodOutputDTO entryWithNulls = new ExtractByPeriodOutputDTO();
        entryWithNulls.setDate(LocalDate.of(2025, 8, 10));
        entryWithNulls.setInfo("Informação sem valores");
        entryWithNulls.setAmount(null);
        entryWithNulls.setBalance(null);

        List<ExtractByPeriodOutputDTO> dataWithNulls = Collections.singletonList(entryWithNulls);

        when(findExtractByPeriod.execute(initialDate, finalDate)).thenReturn(dataWithNulls);

        byte[] excelBytes = createExtractExcel.execute(initialDate, finalDate);

        assertNotNull(excelBytes);
        assertTrue(excelBytes.length > 0);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelBytes))) {
            Sheet sheet = workbook.getSheet("Extrato");
            assertNotNull(sheet);

            Row dataRow = sheet.getRow(2);
            assertNotNull(dataRow);
            assertEquals("10/08/2025", dataRow.getCell(0).getStringCellValue());
            assertEquals("Informação sem valores", dataRow.getCell(1).getStringCellValue());

            Cell amountCell = dataRow.getCell(2);
            if (amountCell != null) {
                assertEquals(CellType.BLANK, amountCell.getCellType(), "A célula de valor deve estar vazia");
            }

            Cell balanceCell = dataRow.getCell(3);
            if (balanceCell != null) {
                assertEquals(CellType.BLANK, balanceCell.getCellType(), "A célula de saldo deve estar vazia");
            }
        }
    }

    @Test
    void shouldPropagateBadRequestException() throws BadRequestException {
        when(findExtractByPeriod.execute(any(), any())).thenThrow(new BadRequestException("Período inválido"));

        assertThrows(BadRequestException.class, () -> {
            createExtractExcel.execute(initialDate, finalDate);
        });

        verify(findExtractByPeriod, times(1)).execute(initialDate, finalDate);
    }
}