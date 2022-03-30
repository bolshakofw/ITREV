package com.example.FileSaver.controller;


import com.example.FileSaver.DTO.ChangeFileDto;
import com.example.FileSaver.FileData;
import com.example.FileSaver.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {


    private final FileService fileService;


    @PostMapping("/upload")
    public FileData upload(@RequestBody MultipartFile file) throws IOException {
        return fileService.upload(file);
    }

    @GetMapping("/names")
    public List<String> names() {
        return fileService.names();
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        fileService.delete(id);
    }


    @PutMapping
    public void change(@RequestBody ChangeFileDto changeFileDto) {
        fileService.change(changeFileDto);
    }


    @GetMapping("/filter")
    public List<FileData> filter(@RequestParam(value = "name", required = false) String fileName,
                                 @RequestParam(value = "type", required = false) String fileType,
                                 @RequestParam(value = "from", required = false) Long fromDate,
                                 @RequestParam(value = "till", required = false) Long tillDate) {
        return fileService.filter(fileName, fileType, fromDate, tillDate);
    }

    @GetMapping("/download/{id}")
    public HttpEntity<byte[]> download(@PathVariable UUID id) {
        return fileService.download(id);
    }


    @GetMapping("/download/zip")
    public HttpEntity<byte[]> downloadZip(@RequestParam("ids") UUID[] ids) throws IOException {
        byte[] body = fileService.downloadZip(ids);
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Disposition", "attachment; filename=" + "files.zip");
        header.setContentLength(body.length);

        return new HttpEntity<>(body, header);
    }


}
