package dev.marcos.url_shortener.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record CreateShortUrlRequest(
        @URL(message = "URL inválida")
        @NotBlank(message = "URL obrigatória")
        @Size(max = 2048, message = "URL muito longa")
        String url,
        @Min(0)
        Integer expiryDays
) {
}
