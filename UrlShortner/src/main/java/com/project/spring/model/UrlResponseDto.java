package com.project.spring.model;

import java.time.LocalDateTime;

public class UrlResponseDto {
	
	private String originalLink;
	private String shortLink;
	private LocalDateTime expirationDate;
	
	public UrlResponseDto(String originalLink, String shortLink, LocalDateTime expirationDate) {
		super();
		this.originalLink = originalLink;
		this.shortLink = shortLink;
		this.expirationDate = expirationDate;
	}

	public UrlResponseDto() {
		
	}

	public String getOriginalLink() {
		return originalLink;
	}
	
	public void setOriginalLink(String originalLink) {
		this.originalLink = originalLink;
	}
	
	public String getShortLink() {
		return shortLink;
	}
	
	public void setShortLink(String shortLink) {
		this.shortLink = shortLink;
	}
	
	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}
	
	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	@Override
	public String toString() {
		return "UrlResponseDto [originalLink=" + originalLink + ", shortLink=" + shortLink + ", expirationDate="
				+ expirationDate + "]";
	}
	
}
