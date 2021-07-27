package com.project.spring.Controller;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.spring.model.Url;
import com.project.spring.model.UrlDto;
import com.project.spring.model.UrlErrorResponseDto;
import com.project.spring.model.UrlResponseDto;
import com.project.spring.service.UrlService;

@RestController
public class UrlShortnerController {
	
	@Autowired
	private UrlService urlService;
	
	@PostMapping("/generate")
	public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {
		Url urlObject = urlService.generateShortLink(urlDto);
		
		if(urlObject != null) {
			UrlResponseDto urlResponse = new UrlResponseDto();
			urlResponse.setOriginalLink(urlObject.getOriginalUrl());
			urlResponse.setShortLink(urlObject.getShortLink());
			urlResponse.setExpirationDate(urlObject.getExpirationDate());
			
			return new ResponseEntity<UrlResponseDto> (urlResponse, HttpStatus.OK);
		}
		
		String status = "404";
		String errorMessage = "There was an error processing your request please try again";
		return errorResponse(status, errorMessage);
	}
	
	@GetMapping("/{shortLink}")
	public ResponseEntity<?> redirectToOriginalLink(@PathVariable String shortLink, HttpServletResponse response) throws IOException {
		if(StringUtils.isEmpty(shortLink)) {
			String status = "400";
			String errorMessage = "Invalid url -_-";
			return errorResponse(status, errorMessage);
		}
		
		Url urlObject = urlService.getEncodedUrl(shortLink);
		
		if(urlObject == null) {
			String status = "400";
			String errorMessage = "Invalid url or the url might have expired";
			return errorResponse(status, errorMessage);
		}
		
		if(urlObject.getExpirationDate().isBefore(LocalDateTime.now())) {
			urlService.deleteShortLink(urlObject);
			String status = "200";
			String errorMessage = "Url has expired. Please try again with a fresh one";
			return errorResponse(status, errorMessage);
		}
		
		response.sendRedirect(urlObject.getOriginalUrl());
		return null;
	}
	
	private ResponseEntity<?> errorResponse(String status, String erorMessage) {
		UrlErrorResponseDto urlErrorResponse = new UrlErrorResponseDto();
		urlErrorResponse.setStatus(status);
		urlErrorResponse.setError(erorMessage);
		
		return new ResponseEntity<UrlErrorResponseDto> (urlErrorResponse, HttpStatus.OK); 
	}

}
