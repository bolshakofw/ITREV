package com.example.FileSaver.DTOS;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Data
public class ModelDTO {
    @NotNull
    private String fileName;
    @NotNull
    private String fileType;

    private LocalDate dateOfChange;



}
