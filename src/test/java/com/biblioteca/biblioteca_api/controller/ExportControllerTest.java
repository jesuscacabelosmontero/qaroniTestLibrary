package com.biblioteca.biblioteca_api.controller;

import com.biblioteca.biblioteca_api.config.TestSecurityConfig;
import com.biblioteca.biblioteca_api.security.JwtTokenUtil;
import com.biblioteca.biblioteca_api.util.ExcelGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(ExportController.class)
public class ExportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExcelGenerator exportService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    @DisplayName("GET /export - should export books and authors summary as Excel")
    void exportExcel_returnsExcel() throws Exception {
        byte[] mockExcelData = new byte[]{1, 2, 3, 4, 5};

        Mockito.when(exportService.generateExcel()).thenReturn(mockExcelData);

        mockMvc.perform(get("/export"))
            .andExpect(status().isOk())  
            .andExpect(content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .andExpect(content().bytes(mockExcelData)); 
    }
}