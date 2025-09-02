package com.beg.domain.usecases.createExcel;

import com.beg.domain.services.findByPeriod.ExtractByPeriodOutputDTO;
import com.beg.domain.services.findByPeriod.FindExtractByPeriod;
import org.apache.coyote.BadRequestException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CreateExtractExcel {

    private static final String[] HEADERS = {"Data", "Informação", "Valor", "Saldo"};
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final FindExtractByPeriod findExtractByPeriod;

    public CreateExtractExcel(FindExtractByPeriod findExtractByPeriod) {
        this.findExtractByPeriod = findExtractByPeriod;
    }

    public byte[] execute(LocalDate initialDate, LocalDate finalDate) throws BadRequestException {
        List<ExtractByPeriodOutputDTO> extractData = findExtractByPeriod.execute(initialDate, finalDate);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Extrato");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (ExtractByPeriodOutputDTO extract : extractData) {
                Row row = sheet.createRow(rowNum++);

                Cell dateCell = row.createCell(0);
                dateCell.setCellValue(extract.getDate().format(DATE_FORMATTER));
                dateCell.setCellStyle(dateStyle);

                Cell infoCell = row.createCell(1);
                infoCell.setCellValue(extract.getInfo());

                Cell amountCell = row.createCell(2);
                if (extract.getAmount() != null) {
                    amountCell.setCellValue(extract.getAmount());
                    amountCell.setCellStyle(currencyStyle);
                }

                Cell balanceCell = row.createCell(3);
                if (extract.getBalance() != null) {
                    balanceCell.setCellValue(extract.getBalance());
                    balanceCell.setCellStyle(currencyStyle);
                }
            }

            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            Row footerRow = sheet.createRow(rowNum + 1);
            Cell footerCell = footerRow.createCell(0);
            footerCell.setCellValue("Período: " + initialDate.format(DATE_FORMATTER) +
                    " a " + finalDate.format(DATE_FORMATTER));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar arquivo Excel: " + e.getMessage(), e);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("dd/mm/yyyy"));
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        return style;
    }
}