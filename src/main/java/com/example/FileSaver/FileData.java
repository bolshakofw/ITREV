package com.example.FileSaver;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.UUID;

public class FileData {
    private UUID id;
    private String fileName;
    private String fileType;
    private long fileSize;
    private LocalDate load;
    private LocalDate change;

    @JsonIgnore
    private byte[] bytes;

    private String fileDownloadUri;


//    public FileData(UUID id, String fileName, String fileType, long fileSize, LocalDate load, LocalDate change, byte[] bytes) {
//        this.id = id;
//        this.fileName = fileName;
//        this.fileType = fileType;
//        this.fileSize = fileSize;
//        this.load = load;
//        this.change = change;
//        this.bytes = bytes;
//    }

    public FileData(UUID id, String fileName, String fileType, long fileSize, LocalDate load, LocalDate change) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.load = load;
        this.change = change;
    }

    public FileData() {

    }

    public FileData(String originalFilename, String contentType, long size, LocalDate change, LocalDate load) {

    }



    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDate getLoad() {
        return load;
    }

    public void setLoad(LocalDate load) {
        this.load = load;
    }

    public LocalDate getChange() {
        return change;
    }

    public void setChange(LocalDate change) {
        this.change = change;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public UUID getId() {
        return id;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

}