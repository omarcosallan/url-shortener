package dev.marcos.url_shortener.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Entity
@Table(name = "short_urls", indexes = {
        @Index(name = "idx_code_unique", columnList = "short_code", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @URL
    @Column(name = "original_url", nullable = false, columnDefinition = "TEXT")
    private String originalUrl;

    @Size(min = 5, max = 10)
    @Column(name = "short_code", nullable = false, unique = true, length = 10)
    private String shortCode;

    private Long hits = 0L;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    public ShortUrl(String originalUrl, String shortCode, LocalDateTime expiresAt) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.expiresAt = expiresAt;
    }
}
