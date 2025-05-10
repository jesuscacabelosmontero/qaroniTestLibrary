package com.biblioteca.biblioteca_api.util;

import com.biblioteca.biblioteca_api.model.Author;
import com.biblioteca.biblioteca_api.repository.AuthorRepository;
import com.biblioteca.biblioteca_api.repository.BookRepository;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelGenerator {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public byte[] generateExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Library Summary");

            int rowIdx = 0;

            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Total Books");
            row.createCell(1).setCellValue(bookRepository.count());

            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Total Authors");
            row.createCell(1).setCellValue(authorRepository.count());
            rowIdx++;

            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("Author");
            row.createCell(1).setCellValue("Number of Books");

            List<Author> authors = authorRepository.findAll();
            for (Author author : authors) {
                row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(author.getName());
                row.createCell(1).setCellValue(author.getBooks().size());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file", e);
        }
    }
}
