package com.example.FileSaver.service;

import com.example.FileSaver.DTO.ChangeFileDto;
import com.example.FileSaver.DTO.ModelDTO;
import com.example.FileSaver.FileData;
import com.example.FileSaver.exceptions.WrongFileSizeException;
import com.example.FileSaver.exceptions.WrongFileTypeException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RequiredArgsConstructor
@Service
public class FileService {


    private static final List<String> CONTENT_TYPES = Arrays.asList("image/png", "image/jpeg", "image/gif", "text/plain", "pdf/application", "");
    private static final long MAX_SIZE = 15 * 1024 * 1024;
    private LinkedList<FileData> list = new LinkedList<>();


    public void setList(LinkedList<FileData> list) {
        this.list = list;
    }

    public void uploadFile(MultipartFile file) throws IOException {
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
        fileData.setBytes(file.getBytes());
        fileData.setFileDownloadUri(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/download/").path(fileData.getId().toString()).toUriString());

        list.add(fileData);
    }


    public List<String> getList() {

        List<String> names = new ArrayList<>();
        for (FileData fileData : list) {
            names.add(fileData.getFileName());
        }
        return names;
    }

    public FileData getFile(String fileName) {
        for (FileData fileData : list) {
            if (fileData.getFileName().contains(fileName)) {
                return fileData;
            }
        }
        return null;
    }

    public void delete(UUID id) {
        list.removeIf(fileData -> fileData.getId().equals(id));
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
        for (FileData fileData : list) {
            if (fileData.getId().equals(id)) {
                byte[] body = fileData.getBytes();
                HttpHeaders header = new HttpHeaders();
                header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileData.getFileName());
                header.setContentLength(fileData.getFileSize());
                return new HttpEntity<>(body, header);
            }
        }
        return null;
    }




    public ArrayList<FileData> model(ModelDTO modelDTO) {
        ArrayList<FileData> finalList = new ArrayList<>();
        for(FileData fileData:list){
            if(modelDTO.getFileName()!= null && fileData.getFileName().contains(modelDTO.getFileName())){
                finalList.add(fileData);
            }
            if(modelDTO.getFileType()!=null && fileData.getFileType().contains(modelDTO.getFileType())){
                if(!finalList.contains(fileData)){
                    finalList.add(fileData);
                }
            }
        }
        return finalList;
    }


    public byte[] downloadZip(UUID[] uuids) throws IOException {
        for (FileData fileData : list) {
            if (Arrays.stream(uuids).toList().contains(fileData.getId())) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ZipOutputStream zos = new ZipOutputStream(bos);
                zos.putNextEntry(new ZipEntry(fileData.getFileName()));
                zos.write(fileData.getBytes());
                zos.closeEntry();
                return bos.toByteArray();
            }
        }


        return new byte[0];
    }


}
