package com.example.FileSaver;


import com.example.FileSaver.DTO.ChangeFileDto;

import com.example.FileSaver.service.FileService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.io.*;
import java.util.*;


@RestController
@RequiredArgsConstructor
public class FileController {


    private final FileService fileService;


    @PostMapping("/upload")
    public FileData upload(@RequestBody MultipartFile file) throws IOException {
        return fileService.uploadFile(file);
    }

    @GetMapping("/getList")
    public List<String> getList() {
        return fileService.getList();
    }

    @GetMapping("/getFile")
    public FileData getFile(@RequestParam("fileName") String fileName) {
        return fileService.getFile(fileName);
    }


    @DeleteMapping("/delete")
    public void delete(@RequestParam("id") UUID id) {
        fileService.delete(id);
    }


    @PutMapping("/changeFile")
    public void changeFile(@RequestBody ChangeFileDto changeFileDto) {
        fileService.changeFile(changeFileDto);
    }


    @GetMapping("/model")
    public LinkedList<FileData> model(@RequestParam("name") String fileName,
                                      @RequestParam("type") String fileType,
                                      @RequestParam("from") long fromDate,
                                      @RequestParam("till") long tillDate) {
        return fileService.model(fileName, fileType, fromDate, tillDate);
    }

    @GetMapping("/download/{id}")
    public HttpEntity<byte[]> download(@PathVariable UUID id) {
        return fileService.download(id);
    }


    @GetMapping("/downloadZip")
    public HttpEntity<byte[]> downloadZip(@RequestParam UUID[] ids) throws IOException {

        byte[] body = fileService.downloadZip(ids);
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Disposition", "attachment; filename=" + "files.zip");
        header.setContentLength(body.length);
        String downloadZipUri = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/downloadZip/").path(Arrays.toString(ids)).toUriString();
        return new HttpEntity<>(body, header);
    }


}
