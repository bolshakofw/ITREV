package com.example.FileSaver.controller;


import com.example.FileSaver.DTO.ChangeFileDto;

import com.example.FileSaver.FileData;
import com.example.FileSaver.service.FileService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.*;
import java.util.stream.Stream;


@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {


    private final FileService fileService;


    @PostMapping("/upload")
    public FileData upload(@RequestBody MultipartFile file) throws IOException {
        return fileService.uploadFile(file);
    }

    @GetMapping("/names")
    public List<String> getList() {
        return fileService.getList();
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        fileService.delete(id);
    }


    @PutMapping
    public void changeFile(@RequestBody ChangeFileDto changeFileDto) {
        fileService.changeFile(changeFileDto);
    }


    @GetMapping("/filter")
    public List<FileData> model(@RequestParam("name") String fileName,
                                @RequestParam("type") String fileType,
                                @RequestParam("from") Long fromDate,
                                @RequestParam("till") Long tillDate) {
        return fileService.model(fileName, fileType, fromDate, tillDate);
    }

    @GetMapping("/download/{id}")
    public HttpEntity<byte[]> download(@PathVariable UUID id) {
        return fileService.download(id);
    }


    @GetMapping("/download/zip/{ids}")
    public HttpEntity<byte[]> downloadZip(@RequestParam UUID[] ids) throws IOException {

        byte[] body = fileService.downloadZip(ids);
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Disposition", "attachment; filename=" + "files.zip");
        header.setContentLength(body.length);

        return new HttpEntity<>(body, header);
    }


}
