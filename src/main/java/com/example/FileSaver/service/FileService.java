package com.example.FileSaver.service;

import com.example.FileSaver.DTO.ChangeFileDto;
import com.example.FileSaver.FileData;
import com.example.FileSaver.DTO.FileDownloadDTO;
import com.example.FileSaver.exceptions.WrongFileSizeException;
import com.example.FileSaver.exceptions.WrongFileTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@RequiredArgsConstructor
@Service
public class FileService {


    private static final List<String> CONTENT_TYPES = Arrays.asList("image/png", "image/jpeg", "image/gif", "text/plain", "pdf/application");
    private static final long MAX_SIZE = 15 * 1024 * 1024;
    private LinkedList<FileData> list = new LinkedList<>();


    public void setList(LinkedList<FileData> list) {
        this.list = list;
    }

    public FileData uploadFile(MultipartFile file) throws IOException {
        if (!CONTENT_TYPES.contains((file.getContentType()))) {
            throw new WrongFileTypeException("Wrong file type");
        } else if (!(file.getSize() < MAX_SIZE)) {
            throw new WrongFileSizeException("File too big");
        }

        FileData fileData = new FileData();

        fileData.setId(UUID.randomUUID());
        fileData.setFileName(file.getOriginalFilename());
        fileData.setFileType(file.getContentType());
        fileData.setFileSize(file.getSize());
        fileData.setLoad(new Timestamp(System.currentTimeMillis()));
        fileData.setBytes(file.getBytes());
        fileData.setFileDownloadUri(ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(fileData.getId().toString()).toUriString());
        list.add(fileData);
        return fileData;
    }


    public List<String> getList() {

        List<String> names = new ArrayList<>();
        for (FileData fileData : list) {
            names.add(fileData.getFileName());
        }
        return names;
    }

    public List<String> getFile(String fileName) {
        List<String> names = new ArrayList<>();
        for (FileData fileData : list) {
            if (fileData.getFileName().contains(fileName)) {
                names.add(fileData.getFileName());
            }
        }
        return names;
    }

    public void delete(UUID id) {
        list.removeIf(fileData -> fileData.getId().equals(id));
    }

    public void changeFile(ChangeFileDto changeFileDto) {
        for (FileData fileData : list) {
            if (fileData.getId().equals(changeFileDto.getId())) {
                fileData.setFileName(changeFileDto.getFileName());
                fileData.setFileType(changeFileDto.getFileType());
                fileData.setChange(new Timestamp(System.currentTimeMillis()));
                break;
            }
        }
    }


    public HttpHeaders createHeader(String fileName, int size) {
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Disposition", "attachment; filename=" + fileName);
        header.setContentLength(size);
        return header;
    }


}
