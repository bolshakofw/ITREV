package com.example.FileSaver;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
public class FileController {
    @Autowired
    private FileStorage fileStorage;

    @PostMapping("/upload")
    public FileData upload(@RequestBody MultipartFile file) throws IOException {
        FileData fileData = new FileData();

        fileData.setId(UUID.randomUUID());
        fileData.setFileName(file.getOriginalFilename());
        fileData.setFileType(file.getContentType());
        fileData.setFileSize(file.getSize());
        fileData.setLoad(LocalDate.now());
        fileData.setBytes(file.getBytes());
        fileData.setFileDownloadUri(ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(fileData.getFileName()).toUriString());
        fileStorage.getList().add(fileData);

        return fileData;

    }

    @GetMapping("/getFile")
    public ArrayList<FileData> getFile(@RequestParam("fileName") String fileName) {
        List<FileData> list = fileStorage.getList();
        ArrayList<FileData> names = new ArrayList<>();
        for (FileData fileData : list) {
            if (fileData.getFileName().contains(fileName)) {
                names.add(fileData);
            }
        }
        return names;

    }

    @GetMapping("/getList")
    public List<String> getList() {
        List<FileData> list = fileStorage.getList();
        List<String> names = new ArrayList<>();
        for (FileData fileData : list) {
            names.add(fileData.getFileName());
        }
        return names;
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam("id") UUID id) {
        List<FileData> list = fileStorage.getList();
        list.removeIf(fileData -> fileData.getId().equals(id));
    }


    @PutMapping("/changeFile")
    public void changeFile(@RequestBody ChangeFileDto changeFileDto) {
        List<FileData> list = fileStorage.getList();

        for (FileData fileData : list) {
            if (fileData.getId().equals(changeFileDto.getId())) {
                fileData.setFileName(changeFileDto.getFileName());
                fileData.setFileType(changeFileDto.getFileType());
                fileData.setChange(LocalDate.now());
                break;
            }
        }
    }


    @GetMapping("/download")
    public File download(@RequestParam("id") UUID id) throws IOException {
        List<FileData> list = fileStorage.getList();

        for (FileData fileData : list) {
            if (fileData.getId().equals(id)) {
                File file = new File(fileData.getFileName());
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(fileData.getBytes());
                return file;

            }
        }
        return null;
    }

}
