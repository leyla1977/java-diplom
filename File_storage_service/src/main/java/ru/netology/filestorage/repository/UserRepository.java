package ru.netology.filestorage.repository;

import ru.netology.filestorage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface
UserRepository extends JpaRepository<User, Long> {

    // Найти пользователя по имени
    Optional<User> findByLogin(String login);

}