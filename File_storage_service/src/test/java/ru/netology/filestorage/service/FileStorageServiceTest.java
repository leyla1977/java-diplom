package ru.netology.filestorage.service;

import org.springframework.test.util.ReflectionTestUtils;
import ru.netology.filestorage.dto.FileResponse;
import ru.netology.filestorage.entity.FileEntity;
import ru.netology.filestorage.entity.User;
import ru.netology.filestorage.repository.FileRepository;
import ru.netology.filestorage.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FileStorageServiceImpl fileStorageService;

    private User testUser;
    private FileEntity testFile;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setLogin("user");
        testUser.setEmail("user@example.com");

        testFile = new FileEntity();
        testFile.setId(1L);
        testFile.setFilename("test.txt");
        testFile.setOriginalFilename("test.txt");
        testFile.setUser(testUser);

        // Устанавливаем storageLocation для тестов
        ReflectionTestUtils.setField(fileStorageService, "storageLocation", "./test-uploads");
    }

    @Test
    void createEmptyFile_ShouldCreateFile() {
        when(userRepository.findByLogin("user")).thenReturn(Optional.of(testUser));
        when(fileRepository.save(any(FileEntity.class))).thenReturn(testFile);

        FileResponse response = fileStorageService.createEmptyFile("test.txt", "user");

        assertNotNull(response);
        assertEquals("test.txt", response.getOriginalFilename());
        verify(fileRepository).save(any(FileEntity.class));
    }


    @Test
    void deleteFile_ShouldDeleteFile() {
        // Подготавливаем данные
        when(userRepository.findByLogin("user")).thenReturn(Optional.of(testUser));

        String uuidFilename = UUID.randomUUID().toString() + "_test.txt";
        System.out.println("Generated UUID filename: " + uuidFilename);

        FileEntity testFileWithUuid = new FileEntity();
        testFileWithUuid.setId(1L);
        testFileWithUuid.setFilename(uuidFilename);
        testFileWithUuid.setOriginalFilename("test.txt");
        testFileWithUuid.setUser(testUser);

        // Мокаем findByUser (а не findAll)
        when(fileRepository.findByUser(testUser)).thenReturn(List.of(testFileWithUuid));

        // Выполняем удаление
        fileStorageService.deleteFile("test.txt", "user");

        // Проверяем, что delete был вызван с правильным файлом
        verify(fileRepository, times(1)).delete(testFileWithUuid);
    }
}