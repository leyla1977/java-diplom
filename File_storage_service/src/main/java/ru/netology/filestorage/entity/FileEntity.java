package ru.netology.filestorage.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "files")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    @Column(name = "original_filename")
    private String originalFilename;

    private Long size;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "storage_path")
    private String storagePath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }

    // ========== ГЕТТЕРЫ ==========
    public Long getId() { return id; }
    public String getFilename() { return filename; }
    public String getOriginalFilename() { return originalFilename; }
    public Long getSize() { return size; }
    public String getContentType() { return contentType; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public String getStoragePath() { return storagePath; }
    public User getUser() { return user; }

    // ========== СЕТТЕРЫ ==========
    public void setId(Long id) { this.id = id; }
    public void setFilename(String filename) { this.filename = filename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }
    public void setSize(Long size) { this.size = size; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
    public void setStoragePath(String storagePath) { this.storagePath = storagePath; }
    public void setUser(User user) { this.user = user; }
}