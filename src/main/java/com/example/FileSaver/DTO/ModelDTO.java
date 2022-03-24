package com.example.FileSaver.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;


@Data
public class ModelDTO {

    private String fileName;

    private String fileType;

    private Timestamp dateOfChange;



}
