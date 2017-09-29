package com.rocket;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

public class Response implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3143089716634518449L;
	
	private HttpStatus status;
	private String token;
	
	public Response(String token, HttpStatus status) {
		this.token = token;
		this.status = status;
	}
	
	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
