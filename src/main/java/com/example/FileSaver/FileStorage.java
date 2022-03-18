package com.example.FileSaver;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class FileStorage {

    private LinkedList<FileData> list = new LinkedList<>();

    public LinkedList<FileData> getList() {
        return list;
    }

    public void setList(LinkedList<FileData> list) {
        this.list = list;
    }
}
