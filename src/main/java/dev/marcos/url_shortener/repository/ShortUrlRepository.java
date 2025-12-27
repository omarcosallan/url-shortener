package dev.marcos.url_shortener.repository;

import dev.marcos.url_shortener.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByShortCode(String code);
    boolean existsByShortCode(String code);
}
