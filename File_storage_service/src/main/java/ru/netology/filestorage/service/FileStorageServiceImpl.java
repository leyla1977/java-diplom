package ru.netology.filestorage.service;

import ru.netology.filestorage.dto.FileResponse;
import ru.netology.filestorage.entity.FileEntity;
import ru.netology.filestorage.entity.User;
import ru.netology.filestorage.repository.FileRepository;
import ru.netology.filestorage.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service


public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public FileStorageServiceImpl(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @Value("${app.storage.location}")
    private String storageLocation;

    @Override
    public FileResponse storeFile(MultipartFile file, String login) {
        try {
            User user = userRepository.findByLogin(login)
                    .orElseThrow(() -> new RuntimeException("User not found: " + login));

            // Генерируем уникальное имя файла
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // Создаем директорию пользователя если не существует
            Path userDir = Paths.get(storageLocation, login).toAbsolutePath().normalize();
            Files.createDirectories(userDir);

            // Сохраняем файл
            Path targetLocation = userDir.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Сохраняем информацию в БД
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFilename(uniqueFilename);
            fileEntity.setOriginalFilename(originalFilename);
            fileEntity.setSize(file.getSize());
            fileEntity.setContentType(file.getContentType());
            fileEntity.setStoragePath(targetLocation.toString());
            fileEntity.setUser(user);

            FileEntity savedFile = fileRepository.save(fileEntity);

            logger.info("File uploaded: {} by user: {}", originalFilename, login);

            return convertToDto(savedFile);

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename(), ex);
        }
    }


    @Override
    public FileResponse createEmptyFile(String filename, String username) {
        System.out.println("=== createEmptyFile ===");
        System.out.println("Filename: " + filename);
        System.out.println("Username: " + username);
        System.out.println("Storage location: " + storageLocation);

        try {
            User user = userRepository.findByLogin(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            String storageFilename = UUID.randomUUID().toString() + "_" + filename;
            System.out.println("Storage filename: " + storageFilename);

            Path filePath = Paths.get(storageLocation, storageFilename);
            System.out.println("File path: " + filePath);

            // Проверяем, существует ли папка
            Files.createDirectories(Paths.get(storageLocation));

            // Создаем пустой файл
            Files.createFile(filePath);
            System.out.println("File created successfully");

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFilename(storageFilename);
            fileEntity.setOriginalFilename(filename);
            fileEntity.setSize(0L);

            // Пробуем определить Content-Type
            String contentType = Files.probeContentType(filePath);
            System.out.println("Content type: " + contentType);
            fileEntity.setContentType(contentType != null ? contentType : "application/octet-stream");

            fileEntity.setStoragePath(storageFilename);
            fileEntity.setUploadedAt(LocalDateTime.now());
            fileEntity.setUser(user);

            FileEntity savedFile = fileRepository.save(fileEntity);
            System.out.println("File saved to DB with ID: " + savedFile.getId());

            FileResponse response = new FileResponse();
            response.setFilename(savedFile.getOriginalFilename());
            response.setOriginalFilename(savedFile.getOriginalFilename());


            return response;

        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create empty file: " + filename, e);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    public Resource loadFileAsResource(String filename, String login) {
        try {
            FileEntity fileEntity = fileRepository.findByFilenameAndUserLogin(filename, login)
                    .orElseThrow(() -> new RuntimeException("File not found: " + filename));

            Path filePath = Paths.get(fileEntity.getStoragePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or not readable: " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found: " + filename, ex);
        }
    }

    @Override
    public List<FileResponse> listFiles(String login) {
        return fileRepository.findByUserLogin(login).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFile(String filename, String username) {
        System.out.println("========== DELETE FILE SERVICE ==========");
        System.out.println("Filename to delete: " + filename);

        try {
            User user = userRepository.findByLogin(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            // Ищем файл, который заканчивается на filename (оригинальное имя)
            List<FileEntity> files = fileRepository.findByUser(user);
            FileEntity fileToDelete = files.stream()
                    .filter(f -> f.getOriginalFilename().equals(filename) || f.getFilename().endsWith(filename))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("File not found: " + filename));

            System.out.println("Found file in DB: " + fileToDelete.getFilename());

            // Удаляем физический файл
            Path filePath = Paths.get(storageLocation, fileToDelete.getFilename());
            Files.deleteIfExists(filePath);
            System.out.println("Physical file deleted: " + filePath);

            // Удаляем запись из базы данных
            fileRepository.delete(fileToDelete);
            System.out.println("Database record deleted");

        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete file: " + filename, e);
        }
    }

    @Override
    public FileResponse getFileInfo(String filename, String login) {
        FileEntity fileEntity = fileRepository.findByFilenameAndUserLogin(filename, login)
                .orElseThrow(() -> new RuntimeException("File not found: " + filename));
        return convertToDto(fileEntity);
    }

    private FileResponse convertToDto(FileEntity fileEntity) {
        FileResponse response = new FileResponse();
        response.setFilename(fileEntity.getFilename());
        response.setOriginalFilename(fileEntity.getOriginalFilename());

        return response;
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex);
    }

}
