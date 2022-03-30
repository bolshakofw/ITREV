package com.example.FileSaver.service;

import com.example.FileSaver.DTO.ChangeFileDto;
import com.example.FileSaver.FileData;
import com.example.FileSaver.exceptions.EmptyFieldException;
import com.example.FileSaver.exceptions.FileNotFoundException;
import com.example.FileSaver.exceptions.InvalidFileSizeException;
import com.example.FileSaver.exceptions.InvalidFileTypeException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RequiredArgsConstructor
@Service
public class FileService {


    private static final List<String> CONTENT_TYPES = List.of("image/png", "image/jpeg", "image/gif", "text/plain", "pdf/application");
    private final LinkedList<FileData> list = new LinkedList<>();

    @Value("${file.size.max}")
    private Long maxSize;

    public FileData upload(MultipartFile file) throws IOException {
        if (!CONTENT_TYPES.contains((file.getContentType()))) {
            throw new InvalidFileTypeException(file.getContentType() + " not a valid file type , supported file types " + CONTENT_TYPES);
        } else if (!(file.getSize() < maxSize)) {
            throw new InvalidFileSizeException("The file size is more than " + maxSize);
        } else if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new EmptyFieldException("Empty filename or file not received");
        }

        FileData fileData = new FileData();

        fileData.setId(UUID.randomUUID());
        fileData.setFileName(file.getOriginalFilename());
        fileData.setFileType(file.getContentType());
        fileData.setFileSize(file.getSize());
        fileData.setLoad(new Timestamp(System.currentTimeMillis()));
        fileData.setChange(new Timestamp(System.currentTimeMillis()));
        fileData.setBytes(file.getBytes());
        fileData.setFileDownloadUri(ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/download/")
                .path(fileData.getId().toString())
                .toUriString());

        list.add(fileData);
        return fileData;
    }


    public List<String> names() {

        return list.stream()
                .map(FileData::getFileName)
                .collect(Collectors.toList());
    }


    public void delete(UUID id) {
        list.removeIf(it -> it.getId().equals(id));
    }

    public void change(ChangeFileDto changeFileDto) {
        for (FileData fileData : list) {
            if (fileData.getId().equals(changeFileDto.getId())) {
                fileData.setFileName(changeFileDto.getFileName());
                fileData.setFileType(changeFileDto.getFileType());
                fileData.setChange(new Timestamp(System.currentTimeMillis()));
                break;
            }
        }
    }

    public HttpEntity<byte[]> download(UUID id) {
        checkExists(id);

        for (FileData fileData : list) {
            if (fileData.getId().equals(id)) {
                byte[] body = fileData.getBytes();
                HttpHeaders header = new HttpHeaders();
                header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = " + fileData.getFileName());
                header.setContentLength(fileData.getFileSize());
                return new HttpEntity<>(body, header);
            }
        }
        return null;
    }

    private void checkExists(UUID id) {
        if (list.stream().noneMatch(it -> it.getId().equals(id))) {
            throw new FileNotFoundException("File not found");
        }
    }


    public byte[] downloadZip(UUID[] uuids) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (ZipOutputStream zos = new ZipOutputStream(bos)) {
                for (FileData fileData : list) {
                    if (Arrays.stream(uuids).toList().contains(fileData.getId())) {
                        zos.putNextEntry(new ZipEntry(fileData.getFileName()));
                        zos.write(fileData.getBytes());
                    }
                }
                zos.closeEntry();
            }
            return bos.toByteArray();
        }
    }

    public List<FileData> filter(String fileName, String fileType, Long tillDate, Long fromDate) {
        Stream<FileData> fileInfo = list.stream();

        if (StringUtils.hasLength(fileName))
            fileInfo = fileInfo.filter(fileData -> fileData.getFileName().contains(fileName));


        if (StringUtils.hasLength(fileType))
            fileInfo = fileInfo.filter(fileData -> fileData.getFileType().contains(fileType));


        if (!Objects.isNull(fromDate)) {
            fileInfo = fileInfo.filter(fileData -> fileData.getChange().getTime() > fromDate);
        }


        if (!Objects.isNull(tillDate)) {
            fileInfo = fileInfo.filter(fileData -> fileData.getChange().getTime() > tillDate);
        }



        return fileInfo.collect(Collectors.toList());
    }

}
