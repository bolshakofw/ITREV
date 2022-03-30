package com.example.FileSaver.DTO;

import lombok.Data;

import java.sql.Timestamp;


@Data
public class ModelDTO {

    private String fileName;

    private String fileType;

    private Timestamp dateOfChange;


}
