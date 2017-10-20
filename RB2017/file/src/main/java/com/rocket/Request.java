package com.rocket;

import java.io.Serializable;

public class Request implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4476463833323485681L;
	private int userId;
	private String[] fileNames;
	private String description_1;
	private String description_2;
	private String description_3;
	private String description_4;
	private String description_5;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String[] getFileNames() {
		return fileNames;
	}

	public void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}

	public String getDescription_1() {
		return description_1;
	}

	public void setDescription_1(String description_1) {
		this.description_1 = description_1;
	}

	public String getDescription_2() {
		return description_2;
	}

	public void setDescription_2(String description_2) {
		this.description_2 = description_2;
	}

	public String getDescription_3() {
		return description_3;
	}

	public void setDescription_3(String description_3) {
		this.description_3 = description_3;
	}

	public String getDescription_4() {
		return description_4;
	}

	public void setDescription_4(String description_4) {
		this.description_4 = description_4;
	}

	public String getDescription_5() {
		return description_5;
	}

	public void setDescription_5(String description_5) {
		this.description_5 = description_5;
	}
	

}
