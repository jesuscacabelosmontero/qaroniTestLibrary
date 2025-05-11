package com.biblioteca.biblioteca_api.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHandleBookNotFoundException() throws Exception {
        mockMvc.perform(get("/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book with id 999 not found"));
    }

    @Test
    void testHandleAuthorNotFoundException() throws Exception {
        mockMvc.perform(get("/authors/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Author with id 999 not found"));
    }
}
