package com.example.FileSaver;


import com.example.FileSaver.DTO.ChangeFileDto;
import com.example.FileSaver.service.FileService;
import lombok.RequiredArgsConstructor;

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
    public List<String> getFile(@RequestParam("id") String fileName) {
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


}
