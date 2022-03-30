package com.example.FileSaver.exceptions.handler;

import com.example.FileSaver.DTO.ErrorDetails;
import com.example.FileSaver.exceptions.EmptyFieldException;
import com.example.FileSaver.exceptions.FileNotFoundException;
import com.example.FileSaver.exceptions.InvalidFileSizeException;
import com.example.FileSaver.exceptions.InvalidFileTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${file.size.max}")
    private Long maxSize;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleInternalErrors(Exception e) {
        log.error("Internal exception", e);
        //e.getClass().getName();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDetails("Internal exception"));
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleFileNotFoundException(FileNotFoundException e) {
        log.warn("File not found", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDetails(e.getMessage()));
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ErrorDetails> handleWrongFileTypeException(Exception e) {
        log.warn("Invalid file type", e);
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ErrorDetails(e.getMessage()));
    }

    @ExceptionHandler({InvalidFileSizeException.class, EmptyFieldException.class})
    public ResponseEntity<ErrorDetails> handleWrongFileSizeException(Exception e) {
        log.warn("Incorrect file size: " + maxSize, e);
        return ResponseEntity
                .badRequest()
                .body(new ErrorDetails(e.getMessage()));
    }


}
