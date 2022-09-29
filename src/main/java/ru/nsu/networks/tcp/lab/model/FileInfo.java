package ru.nsu.networks.tcp.lab.model;

import java.io.Serializable;

public class FileInfo implements Serializable {
    private String fileName;

    private long fileSizeInBytes;

    public FileInfo(String fileName, long fileSizeInBytes) {
        this.fileName = fileName;
        this.fileSizeInBytes = fileSizeInBytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSizeInBytes() {
        return fileSizeInBytes;
    }

    public void setFileSizeInBytes(long fileSizeInBytes) {
        this.fileSizeInBytes = fileSizeInBytes;
    }
}

