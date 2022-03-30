package com.example.FileSaver.DTO;

import lombok.Getter;

import java.sql.Timestamp;


@Getter
public class ErrorDetails {
    private final Timestamp time;
    private final String message;

    public ErrorDetails(String message) {
        time = new Timestamp(System.currentTimeMillis());
        this.message = message;
    }
}
