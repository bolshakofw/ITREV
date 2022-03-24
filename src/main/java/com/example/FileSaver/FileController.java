package com.example.FileSaver;


import com.example.FileSaver.DTO.ChangeFileDto;
import com.example.FileSaver.DTO.ModelDTO;
import com.example.FileSaver.service.FileService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.*;


@RestController
@RequiredArgsConstructor
public class FileController {


    private final FileService fileService;


    @PostMapping("/upload")
    public void upload(@RequestBody MultipartFile file) throws IOException {
        fileService.uploadFile(file);
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
        return new HttpEntity<>(body, header);
    }

    @GetMapping("/model")
    public ArrayList<FileData> model(@RequestBody ModelDTO modelDTO) {
        return fileService.model(modelDTO);
    }
}
