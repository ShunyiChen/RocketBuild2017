package com.rocket;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.rocket.tpa.model.tpackage.Recipient;

public class Response implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3143089716634518449L;
	
	private HttpStatus status;
	private List<Recipient> partners;
	
	public Response(HttpStatus status) {
		this.status = status;
	}
	
	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public List<Recipient> getPartners() {
		return partners;
	}

	public void setPartners(List<Recipient> partners) {
		this.partners = partners;
	}
}
