package dev.marcos.url_shortener.service;

import dev.marcos.url_shortener.dto.CreateShortUrlRequest;
import dev.marcos.url_shortener.entity.ShortUrl;
import dev.marcos.url_shortener.exception.NotFoundException;
import dev.marcos.url_shortener.exception.UrlExpiredException;
import dev.marcos.url_shortener.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final ShortCodeService shortCodeService;

    @Transactional
    public ShortUrl save(CreateShortUrlRequest req) {
        String target = normalizeUrl(req.url());
        String shortCode = shortCodeService.generateUniqueShortCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(1);

        ShortUrl shortUrl = new ShortUrl(target, shortCode, expiresAt);
        return shortUrlRepository.save(shortUrl);
    }

    public ShortUrl findByCode(String shortCode) {
        ShortUrl url = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new NotFoundException("The URL doesn't exist"));

        if (!url.getExpiresAt().isAfter(LocalDateTime.now())) {
            throw new UrlExpiredException("The URL is expired");
        }

        return url;
    }

    @Transactional
    public void registerHit(ShortUrl url) {
        url.setHits(url.getHits() + 1);
        url.setLastAccessedAt(LocalDateTime.now());
        shortUrlRepository.save(url);
    }

    private String normalizeUrl(String input) {
        try {
            URI uri = new URI(input.trim());
            if (uri.getScheme() == null) {
                uri = new URI("https://" + input.trim());
            }
            if (!uri.getScheme().equalsIgnoreCase("http") && !uri.getScheme().equalsIgnoreCase("https")) {
                throw new IllegalArgumentException("Only HTTP and HTTPS are supported");
            }
            return uri.normalize().toString();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL");
        }
    }
}
