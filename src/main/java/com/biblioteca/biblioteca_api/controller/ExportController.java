package com.biblioteca.biblioteca_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.biblioteca_api.model.User;
import com.biblioteca.biblioteca_api.service.ExportService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/export")
public class ExportController {
    
    @Autowired
    private ExportService exportService;

    @GetMapping
    public ResponseEntity<?> exportExcel(
        @CookieValue(value = "JSESSIONID", defaultValue = "") String sessionId, 
        HttpSession session) {

        if (sessionId.isEmpty() || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You need to log in.");
        }
        User user = (User) session.getAttribute("user");
        boolean isDirective = user.getRoles().stream().anyMatch(role -> role.getRole().equalsIgnoreCase("MANAGER"));
        if (!isDirective) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body ("Only members of the directive can export the excel");
        }

        byte[] excelData = exportService.generateSummaryExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename("library_summary.xlsx")
                .build());

        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }
    
    
}
