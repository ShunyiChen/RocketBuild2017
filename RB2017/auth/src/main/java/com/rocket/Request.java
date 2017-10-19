package com.rocket;

import java.io.Serializable;

public class Request implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4476463833323485681L;

	private String account;
	private String password;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
