package com.beg.adapters.controllers;

import com.beg.domain.usecases.findAll.ExtractOutputDTO;
import com.beg.domain.usecases.findAll.FindAllExtract;
import com.beg.domain.usecases.findByPeriod.ExtractByPeriodOutputDTO;
import com.beg.domain.usecases.findByPeriod.FindExtractByPeriod;
import com.beg.domain.usecases.upload.UploadExtractPdf;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/extract")
public class ExtractController {

    private final UploadExtractPdf uploadPdf;
    private final FindAllExtract findAllExtract;
    private final FindExtractByPeriod findExtractByPeriod;

    public ExtractController(UploadExtractPdf uploadExtractPdf, FindAllExtract findAllExtract, FindExtractByPeriod findExtractByPeriod) {
        this.uploadPdf = uploadExtractPdf;
        this.findAllExtract = findAllExtract;
        this.findExtractByPeriod = findExtractByPeriod;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload success"),
            @ApiResponse(responseCode = "500", description = "Error processing file")
    })
    public ResponseEntity<String> uploadExtractPdfFile(@RequestParam("pdfFile") @Parameter(required = true)
                                                       MultipartFile pdfFile) throws IOException, ParseException {
        File tempFile = File.createTempFile("temp", ".pdf");
        pdfFile.transferTo(tempFile);

        uploadPdf.execute(tempFile);
        return ResponseEntity.ok("Upload success");

    }

    @GetMapping(value = "/findAll")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All data returned"),
            @ApiResponse(responseCode = "500", description = "Error finding data")
    })
    public List<ExtractOutputDTO> findAllExtract() {
        return findAllExtract.execute();
    }

    @GetMapping(value = "/findByPeriod")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All data returned"),
            @ApiResponse(responseCode = "$00", description = "Final date cannot be previous than initial date"),
            @ApiResponse(responseCode = "500", description = "Error finding data")
    })
    public List<ExtractByPeriodOutputDTO> findByPeriod(@RequestParam("initialDate") @Parameter(required = true) LocalDate initialDate,
                                                       @RequestParam("finalDate") @Parameter(required = true) LocalDate finalDate) throws BadRequestException {
        return findExtractByPeriod.execute(initialDate, finalDate);
    }
}
