package com.biblioteca.biblioteca_api.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.biblioteca_api.util.ExcelGenerator;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/export")
public class ExportController {
    
    @Autowired
    private ExcelGenerator exportService;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> exportExcel() {
        byte[] excelData = exportService.generateExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename("library_summary_" + LocalDateTime.now() + ".xlsx")
                .build());

        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }
    
}
