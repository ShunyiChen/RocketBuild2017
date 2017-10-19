package com.rocket;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class Response implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3143089716634518449L;
	
	private HttpStatus status;
	private String token;
	private List<Map<String, Object>> company_user_info;
	
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

	public List<Map<String, Object>> getCompany_user_info() {
		return company_user_info;
	}

	public void setCompany_user_info(List<Map<String, Object>> company_user_info) {
		this.company_user_info = company_user_info;
	}
	
}
