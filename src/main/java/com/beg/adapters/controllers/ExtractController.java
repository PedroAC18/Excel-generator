package com.beg.adapters.controllers;

import com.beg.domain.usecases.findAll.ExtractOutputDTO;
import com.beg.domain.usecases.findAll.FindAllExtract;
import com.beg.domain.usecases.upload.UploadExtractPdf;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("api/extract")
public class ExtractController {

    private UploadExtractPdf uploadPdf;
    private FindAllExtract findAllExtract;

    public ExtractController(UploadExtractPdf uploadExtractPdf, FindAllExtract findAllExtract) {
        this.uploadPdf = uploadExtractPdf;
        this.findAllExtract = findAllExtract;
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
}
