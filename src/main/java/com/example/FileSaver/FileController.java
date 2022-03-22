package com.example.FileSaver;


import com.example.FileSaver.DTOS.ChangeFileDto;
import com.example.FileSaver.DTOS.FileDownloadDTO;
import com.example.FileSaver.DTOS.ModelDTO;
import com.example.FileSaver.service.FileService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.*;


@RestController
@RequiredArgsConstructor
public class FileController {


    private final FileService fileService;


    @PostMapping("/upload")
    public FileData upload(@RequestBody MultipartFile file) throws IOException {
        fileService.uploadFile(file);
        return new FileData();
    }

    @GetMapping("/getList")
    public List<String> getList() {
        return fileService.getList();
    }

    @GetMapping("/getFile")
    public ArrayList<FileData> getFile(@RequestParam("fileName") String fileName) {
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
    public List<FileData> model(@RequestBody ModelDTO modelDTO) {
        return fileService.getModel(modelDTO);
    }

    @GetMapping("/download/{fileName}")
    public HttpEntity<byte[]> download(@PathVariable String fileName,FileDownloadDTO fileDownloadDTO) {
        return fileService.download(fileDownloadDTO);

    }

}
