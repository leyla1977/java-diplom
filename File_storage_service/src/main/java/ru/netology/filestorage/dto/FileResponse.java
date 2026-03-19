package ru.netology.filestorage.dto;

import java.time.LocalDateTime;


public class FileResponse {
    private String filename;
    private String originalFilename;


    // Конструктор по умолчанию
    public FileResponse() {}

    // Геттеры
    public String getFilename() { return filename; }
    public String getOriginalFilename() { return originalFilename; }

    // Сеттеры
    public void setFilename(String filename) { this.filename = filename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }

}