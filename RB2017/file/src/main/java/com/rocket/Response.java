package com.rocket;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.HttpStatus;

public class Response implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3143089716634518449L;
	
	private HttpStatus status;
	
	public Response(HttpStatus status) {
		this.status = status;
	}
	
	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
