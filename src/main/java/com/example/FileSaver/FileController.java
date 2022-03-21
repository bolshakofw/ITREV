package com.example.FileSaver;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
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

    @GetMapping("/getList")
    public List<String> getList() {
        List<FileData> list = fileStorage.getList();
        List<String> names = new ArrayList<>();
        for (FileData fileData : list) {
            names.add(fileData.getFileName());
        }
        return names;
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


    @GetMapping("/model")
    public LinkedHashSet<FileData> model(@RequestBody ModelDTO modelDTO) {
        List<FileData> list = fileStorage.getList();
        LinkedHashSet<FileData> spisok = new LinkedHashSet();

        for (FileData fileData : list) {
            if(modelDTO.getFileName() != "") {
                if (fileData.getFileName().contains(modelDTO.getFileName())) {
                    spisok.add(fileData);
                }
            }
            if(modelDTO.getFileType()!= ""){
                if(fileData.getFileType().contains(modelDTO.getFileType())){
                    spisok.add(fileData);
                }
            }


        }

        return spisok;
    }

    @GetMapping("/download/{fileName}")
    public HttpEntity<byte[]> download(@RequestBody FileDownloadDTO fileDownloadDTO) throws IOException {

        List<FileData> list = fileStorage.getList();

        for (FileData fileData : list) {
            if (fileData.getId().equals(fileDownloadDTO.getId())) {
                String fileName = fileData.getFileName();
                byte[] bytes = fileData.getBytes();
                HttpHeaders head = new HttpHeaders();
                switch (fileData.getFileType()) {
                    case ("image/png") -> head.setContentType(MediaType.valueOf(MediaType.IMAGE_PNG_VALUE));
                    case ("text/plain") -> head.setContentType(MediaType.valueOf(MediaType.TEXT_PLAIN_VALUE));
                    case ("image/jpeg") -> head.setContentType(MediaType.valueOf(MediaType.IMAGE_JPEG_VALUE));
                    case ("application/pdf") -> head.setContentType(MediaType.valueOf(MediaType.APPLICATION_PDF_VALUE));
                    case ("*/*") -> head.setContentType(MediaType.valueOf(MediaType.ALL_VALUE));
                }

                head.set(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + fileData.getFileName().replace(" ", ""));
                head.setContentLength(bytes.length);

                return new HttpEntity<byte[]>(bytes, head);
            }
        }
        return null;
    }

}
