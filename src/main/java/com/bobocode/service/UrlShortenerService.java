package com.bobocode.service;

import com.bobocode.dto.UrlInfoDto;
import com.bobocode.entity.ShortUrl;
import com.bobocode.exception.NoUrlFoundException;
import com.bobocode.repository.UrlRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

  private final UrlRepository urlRepository;

  @Retryable
  public String shorten(UrlInfoDto urlInfo) {
    ShortUrl shortUrl = new ShortUrl();
    shortUrl.setOriginalUrl(urlInfo.url());
    shortUrl.setTitle(urlInfo.title());
    shortUrl.setCreatedAt(LocalDateTime.now());

    ShortUrl savedUrl = urlRepository.save(shortUrl);

    return savedUrl.getId();
  }

  public String getOriginalUrl(String urlId) {
    return urlRepository.findById(urlId)
        .map(ShortUrl::getOriginalUrl)
        .orElseThrow(
            () -> new NoUrlFoundException("No short url found for key %s".formatted(urlId)));
  }
}
