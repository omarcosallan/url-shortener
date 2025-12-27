package dev.marcos.url_shortener.controller;

import dev.marcos.url_shortener.dto.CreateShortUrlRequest;
import dev.marcos.url_shortener.entity.ShortUrl;
import dev.marcos.url_shortener.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ShortUrlController {

    private final UrlService urlService;

    @PostMapping("/shorten-url")
    public ResponseEntity<ShortUrl> save(@Valid @RequestBody CreateShortUrlRequest request) {
        ShortUrl result = urlService.save(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/urls/{short-code}")
    public ResponseEntity<ShortUrl> get(@PathVariable("short-code") String shortCode) {
        return ResponseEntity.ok(urlService.findByCode(shortCode));
    }

    @GetMapping("/{short-code}")
    public ResponseEntity<Void> redirect(@PathVariable("short-code") String shortCode) {
        ShortUrl shortUrl = urlService.findByCode(shortCode);
        urlService.registerHit(shortUrl);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, shortUrl.getOriginalUrl())
                .build();
    }
}
