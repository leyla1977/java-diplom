package ru.netology.filestorage.service;

import ru.netology.filestorage.dto.FileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    FileResponse storeFile(MultipartFile file, String login);
    FileResponse createEmptyFile(String filename, String username);
    Resource loadFileAsResource(String filename, String login);
    List<FileResponse> listFiles(String login);
    void deleteFile(String filename, String login);
    FileResponse getFileInfo(String filename, String login);
}

