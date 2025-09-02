package com.beg.domain.services.processPdfData;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.FileSystemException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class ProcessPdfDataService {

    private static final String ERROR_READING_PDF = "Occurred an error trying to read the pdf file";
    private static final String BALACE_STRING = "SALDO DO DIA";
    private static final String DATA_FORMATTER = "dd/MM/yyyy";

    private final NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));

    public ProcessPdfDataService() {
    }

    public List<ExtractDataDTO> execute(File pdfFile) throws FileSystemException, ParseException {

        String fileStringData = readPdfFile(pdfFile);
        String[] pdfRows = fileStringData.split("\n");

        List<String> extractInfoList = filterPdfRows(pdfRows);

        return buildExtractData(extractInfoList);
    }

    private String readPdfFile(File pdf) throws FileSystemException {
        try {
            PDDocument document = Loader.loadPDF(pdf);
            PDFTextStripper stringDocument = new PDFTextStripper();
            return stringDocument.getText(document);
        } catch (Exception e) {
            throw new FileSystemException(ERROR_READING_PDF);
        }
    }

    private List<String> filterPdfRows(String[] pdfRows) {
        return Arrays.stream(pdfRows).filter(
                row -> ( Character.isDigit(row.charAt(0)))
        ).toList();
    }

    private List<ExtractDataDTO> buildExtractData(List<String> extractInfoList) throws ParseException {
        List<ExtractDataDTO> extractDataList = new ArrayList<>();
        for (String extractInfo : extractInfoList) {
            ExtractDataDTO extractData = new ExtractDataDTO();

            String dateString = extractInfo.substring(0, 10);
            if (!dateString.matches("\\d{2}/\\d{2}/\\d{4}")) {
                continue;
            }
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(DATA_FORMATTER));
            extractData.setDate(date);

            int lastTransactionSpace = extractInfo.lastIndexOf(" ");
            String transactionInfo = extractInfo.substring(11, lastTransactionSpace);
            extractData.setInfo(transactionInfo);

            if(transactionInfo.equals(BALACE_STRING)) {
                String balanceString = extractInfo.substring(lastTransactionSpace + 1);
                double balance = parseStringToDouble(balanceString);
                extractData.setBalance(balance);
            } else {
                String amountString = extractInfo.substring(lastTransactionSpace + 1);
                double amount = parseStringToDouble(amountString);
                extractData.setAmount(amount);
            }

            extractDataList.add(extractData);
        }

        return extractDataList;
    }

    private double parseStringToDouble(String valueString) throws ParseException {
        return numberFormat.parse(valueString).doubleValue();
    }
}
