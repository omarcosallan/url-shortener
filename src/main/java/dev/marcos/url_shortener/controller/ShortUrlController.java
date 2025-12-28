package dev.marcos.url_shortener.controller;

import dev.marcos.url_shortener.dto.CreateShortUrlRequest;
import dev.marcos.url_shortener.entity.ShortUrl;
import dev.marcos.url_shortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ShortUrlController {

    private final UrlService urlService;

    @PostMapping("/shorten-url")
    public ResponseEntity<Map<String, String>> save(@Valid @RequestBody CreateShortUrlRequest request, HttpServletRequest httpRequest) {
        ShortUrl shortUrl = urlService.save(request);
        String shortenedUrl = httpRequest.getRequestURL().toString()
                .replace(httpRequest.getRequestURI(), "/" + shortUrl.getShortCode());
        return ResponseEntity.ok(Map.of("url", shortenedUrl));
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
