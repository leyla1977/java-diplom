package ru.netology.filestorage.controller;

import ru.netology.filestorage.dto.FileResponse;
import ru.netology.filestorage.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping({"/file", "/files", "/list"})
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public ResponseEntity<List<FileResponse>> listFiles() {
        System.out.println("========== LIST FILES ==========");
        List<FileResponse> files = fileStorageService.listFiles("user");
        return ResponseEntity.ok(files);
    }


    @PostMapping
    public ResponseEntity<FileResponse> uploadFile(
            @RequestParam("filename") String filename) {

        System.out.println("========== CONTROLLER REACHED ==========");
        System.out.println("Filename: " + filename);

        // Используем стандартного пользователя "user" для теста
        FileResponse response = fileStorageService.createEmptyFile(filename, "user");
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{filename}")
    public ResponseEntity<Void> deleteFile(@PathVariable String filename) {
        System.out.println("========== DELETE FILE ==========");
        System.out.println("Deleting: " + filename);
        fileStorageService.deleteFile(filename, "user");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFileByParam(@RequestParam("filename") String filename) {
        System.out.println("========== DELETE FILE (with param) ==========");
        System.out.println("Filename: " + filename);
        fileStorageService.deleteFile(filename, "user");
        return ResponseEntity.ok().build();
    }
}