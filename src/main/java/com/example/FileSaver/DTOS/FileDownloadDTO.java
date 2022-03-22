package com.example.FileSaver.DTOS;

import lombok.Data;

import java.util.UUID;


@Data
public class FileDownloadDTO {
    private UUID id;
    private String fileName;


}
