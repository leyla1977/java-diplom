package ru.netology.filestorage.repository;

import ru.netology.filestorage.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.netology.filestorage.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    // Найти файл по имени и пользователю
    Optional<FileEntity> findByFilenameAndUserLogin(String filename, String login);

    // Найти все файлы пользователя
    List<FileEntity> findByUserLogin(String login);

    List<FileEntity> findByUser(User user);

}
