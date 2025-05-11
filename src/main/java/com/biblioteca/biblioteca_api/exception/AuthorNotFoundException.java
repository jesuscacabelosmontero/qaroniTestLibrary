package com.biblioteca.biblioteca_api.exception;

public class AuthorNotFoundException extends RuntimeException{
        public AuthorNotFoundException(String message) {
        super(message);
    }
}
