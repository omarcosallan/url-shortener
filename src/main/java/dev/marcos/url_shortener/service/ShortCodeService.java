package dev.marcos.url_shortener.service;

import dev.marcos.url_shortener.repository.ShortUrlRepository;
import dev.marcos.url_shortener.util.Base62;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShortCodeService {

    private static final int DEFAULT_LENGTH = 7;
    private final ShortUrlRepository shortUrlRepository;

    public String generateUniqueShortCode() {
        for (int i = 0; i < 10; i++) {
            String candidate = Base62.randomCode(DEFAULT_LENGTH);
            if (!shortUrlRepository.existsByShortCode(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("Failed to generate unique short code");
    }
}
