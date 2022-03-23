package com.example.FileSaver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDate;
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