package com.rocket;

import java.io.InputStream;
import java.io.Serializable;

import org.springframework.http.HttpStatus;

public class Response implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3143089716634518449L;
	
	private HttpStatus status;
	// Temporary folder ID  
	private String randomId;
	// Transaction number
	private String refNumber;
	
	private int jobId;
	private String[] fileNames;
	private long[] fileSize;
	
	private InputStream inputStream;
	
	public Response(HttpStatus status) {
		this.status = status;
	}
	
	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getRandomId() {
		return randomId;
	}

	public void setRandomId(String randomId) {
		this.randomId = randomId;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public String[] getFileNames() {
		return fileNames;
	}

	public void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public long[] getFileSize() {
		return fileSize;
	}

	public void setFileSize(long[] fileSize) {
		this.fileSize = fileSize;
	}
	
	
}
