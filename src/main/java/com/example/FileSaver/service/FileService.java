package com.example.FileSaver.service;

import com.example.FileSaver.DTO.ChangeFileDto;
import com.example.FileSaver.FileData;
import com.example.FileSaver.exceptions.FileNotFoundException;
import com.example.FileSaver.exceptions.WrongFileSizeException;
import com.example.FileSaver.exceptions.WrongFileTypeException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
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
    private static final long MAX_SIZE = 15 * 1024 * 1024;
    private final LinkedList<FileData> list = new LinkedList<>();


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


    public List<String> getList() {

        return list.stream()
                .map(FileData::getFileName)
                .collect(Collectors.toList());
    }


    public void delete(UUID id) {
        list.removeIf(it -> it.getId().equals(id));
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
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(bos);) {
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

    public List<FileData> model(String fileName, String fileType, Long tillDate, Long fromDate) {
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
