package com.project.spring.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.hash.Hashing;
import com.project.spring.model.Url;
import com.project.spring.model.UrlDto;
import com.project.spring.repository.UrlRepository;

@Component
public class UrlServiceImpl implements UrlService {
	
	@Autowired
	UrlRepository urlRepository;

	/**
	 * Generates an encoded short url, sets all the parameters in Url entity and calls method to save record in db
	 */
	@Override
	public Url generateShortLink(UrlDto urlDto) {
		if(StringUtils.isNotEmpty(urlDto.getUrl())) {
			String encodedUrl = encodeUrl(urlDto.getUrl());
			Url urlToPersist = new Url();
			urlToPersist.setOriginalUrl(urlDto.getUrl());
			urlToPersist.setShortLink(encodedUrl);
			urlToPersist.setCreationDate(LocalDateTime.now());
			urlToPersist.setExpirationDate(generateExpirationDate(urlDto.getExpirationDate(), urlToPersist.getCreationDate()));
			
			Url persistedUrl = persistShortLink(urlToPersist);
			if(persistedUrl != null)
				return persistedUrl;
		}
		return null;
	}

	@Override
	public Url persistShortLink(Url url) {
		Url persistedUrl = urlRepository.save(url);
		
		return persistedUrl;
	}

	@Override
	public Url getEncodedUrl(String url) {
		Url encodedUrl = urlRepository.findByShortLink(url);
		
		return encodedUrl;
	}

	@Override
	public void deleteShortLink(Url url) {
		urlRepository.delete(url);
	}
	
	/**
	 * Returns encodedUrl with hashing technique murmur3_32
	 * @param url
	 * @return String
	 */
	private String encodeUrl(String url) {
		String encodedUrl = "";
		LocalDateTime currentTime = LocalDateTime.now();
		encodedUrl = Hashing.murmur3_32()
				.hashString(url.concat(currentTime.toString()), StandardCharsets.UTF_8)
				.toString();
		
		return encodedUrl;
	}
	
	/**
	 * Returns expirationDate as creationDate+60seconds...if expirationDate is provided by user it returns as it is in LocalDateTime
	 * @param expirationDate
	 * @param creationDate
	 * @return LocalDateTime
	 */
	private LocalDateTime generateExpirationDate(String expirationDate, LocalDateTime creationDate) {
		if(StringUtils.isBlank(expirationDate)) {
			return creationDate.plusSeconds(60);
		}
		LocalDateTime generatedExpirationDate = LocalDateTime.parse(expirationDate);
		
		return generatedExpirationDate;
	}

}
