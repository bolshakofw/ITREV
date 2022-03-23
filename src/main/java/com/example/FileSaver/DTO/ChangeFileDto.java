package com.example.FileSaver.DTO;

import lombok.Data;

import java.util.UUID;


@Data
public class ChangeFileDto {
    private UUID id;
    private String fileName;
    private String fileType;

}


