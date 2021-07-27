package com.project.spring.model;

public class UrlErrorResponseDto {
	
	private String status;
	private String error;
	
	public UrlErrorResponseDto(String status, String error) {
		super();
		this.status = status;
		this.error = error;
	}
	
	public UrlErrorResponseDto() {
		
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "UrlErrorResponseDto [status=" + status + ", error=" + error + "]";
	}

}
