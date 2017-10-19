package com.rocket;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.rocket.tpa.model.tpackage.Recipient;

public class Response implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3143089716634518449L;
	
	private HttpStatus status;
	private List<Map<String, Object>> results;
	
	public Response(HttpStatus status) {
		this.status = status;
	}
	
	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public List<Map<String, Object>> getResults() {
		return results;
	}

	public void setResults(List<Map<String, Object>> results) {
		this.results = results;
	}

	
}
