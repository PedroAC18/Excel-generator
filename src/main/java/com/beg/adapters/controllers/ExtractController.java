package com.beg.adapters.controllers;

import com.beg.domain.usecases.UploadExtractPdf;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("api/extract")
public class ExtractController {

    private UploadExtractPdf uploadPdf;

    public ExtractController(UploadExtractPdf uploadExtractPdf) {
        this.uploadPdf = uploadExtractPdf;
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
}
