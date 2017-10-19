package com.rocket;

import java.io.Serializable;

public class Request implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4476463833323485681L;

	private int idPartnerFactory;
	private int userId;

	public int getIdPartnerFactory() {
		return idPartnerFactory;
	}

	public void setIdPartnerFactory(int idPartnerFactory) {
		this.idPartnerFactory = idPartnerFactory;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
