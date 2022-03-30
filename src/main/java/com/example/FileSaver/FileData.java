package com.example.FileSaver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class FileData {
    private UUID id;
    private String fileName;

    private String fileType;

    private long fileSize;
    private Timestamp load;
    private Timestamp change;

    @JsonIgnore
    private byte[] bytes;

    private String fileDownloadUri;


}