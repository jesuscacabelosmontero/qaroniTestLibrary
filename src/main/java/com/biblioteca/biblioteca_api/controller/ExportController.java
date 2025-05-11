package com.biblioteca.biblioteca_api.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.biblioteca.biblioteca_api.util.ExcelGenerator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/export")
@Tag(name = "Export", description = "Endpoints para exportar datos en formato Excel.")
public class ExportController {
    
    @Autowired
    private ExcelGenerator exportService;

    @Operation(
        summary = "Exportar resumen de libros y autores en Excel",
        description = "Genera un archivo Excel con un resumen que incluye el total de libros, el total de autores, y el total de libros por autor. Solo accesible para usuarios con rol de 'MANAGER'."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
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