package com.example.FileSaver.DTO;

import lombok.Data;

import java.util.UUID;


@Data
public class FileDownloadDTO {
    private UUID id;
    private String fileName;


}
