package com.rocket;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
	public User() {
	}

	public int UserID;
	// Only a-z A-Z 0-9 . _ @ - Characters are allowed in username.
	// Minimum length is 1 and maximum length is 16
	// Duplicate username not allowed
	public String Username;
	public String FirstName;
	public String LastName;
	public String Email;
	public String CompanyName;
	public String Location;
	public String UserUniqueID;
	public String Password;
	public int CompanyId;
	public int LocationID;
	public String LanguageCode;
	public String _userUniqueID;
	public String CompanyUniqueID;
	public String LocationUniqueID;
	public String StatusName;
	public int StatusID;
	public String Phone;
	public String Fax;
	public String TimeZoneId;
	public String DateFormat;
	private Name name;

	public String SupplierCode;
	public Name getName() {
		if (name == null)
			name = new Name(Username);
		return name;
	}

	public String PublicKey;
	public boolean CanInviteToCommunity;
	public int ErrorCode;
	public String ErrorMessage;
	public int RegistrationRequestID;
	public int DepartmentID;

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getCompanyName() {
		return CompanyName;
	}

	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getUserUniqueID() {
		return UserUniqueID;
	}

	public void setUserUniqueID(String userUniqueID) {
		UserUniqueID = userUniqueID;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public int getCompanyId() {
		return CompanyId;
	}

	public void setCompanyId(int companyId) {
		CompanyId = companyId;
	}

	public int getLocationID() {
		return LocationID;
	}

	public void setLocationID(int locationID) {
		LocationID = locationID;
	}

	public String getLanguageCode() {
		return LanguageCode;
	}

	public void setLanguageCode(String languageCode) {
		LanguageCode = languageCode;
	}

	public String get_userUniqueID() {
		return _userUniqueID;
	}

	public void set_userUniqueID(String _userUniqueID) {
		this._userUniqueID = _userUniqueID;
	}

	public String getCompanyUniqueID() {
		return CompanyUniqueID;
	}

	public void setCompanyUniqueID(String companyUniqueID) {
		CompanyUniqueID = companyUniqueID;
	}

	public String getLocationUniqueID() {
		return LocationUniqueID;
	}

	public void setLocationUniqueID(String locationUniqueID) {
		LocationUniqueID = locationUniqueID;
	}

	public String getStatusName() {
		return StatusName;
	}

	public void setStatusName(String statusName) {
		StatusName = statusName;
	}

	public int getStatusID() {
		return StatusID;
	}

	public void setStatusID(int statusID) {
		StatusID = statusID;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getFax() {
		return Fax;
	}

	public void setFax(String fax) {
		Fax = fax;
	}

	public String getTimeZoneId() {
		return TimeZoneId;
	}

	public void setTimeZoneId(String timeZoneId) {
		TimeZoneId = timeZoneId;
	}

	public String getDateFormat() {
		return DateFormat;
	}

	public void setDateFormat(String dateFormat) {
		DateFormat = dateFormat;
	}

	public String getPublicKey() {
		return PublicKey;
	}

	public void setPublicKey(String publicKey) {
		PublicKey = publicKey;
	}

	public boolean isCanInviteToCommunity() {
		return CanInviteToCommunity;
	}

	public void setCanInviteToCommunity(boolean canInviteToCommunity) {
		CanInviteToCommunity = canInviteToCommunity;
	}

	public int getErrorCode() {
		return ErrorCode;
	}

	public void setErrorCode(int errorCode) {
		ErrorCode = errorCode;
	}

	public String getErrorMessage() {
		return ErrorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		ErrorMessage = errorMessage;
	}

	public int getRegistrationRequestID() {
		return RegistrationRequestID;
	}

	public void setRegistrationRequestID(int registrationRequestID) {
		RegistrationRequestID = registrationRequestID;
	}

	public int getDepartmentID() {
		return DepartmentID;
	}

	public void setDepartmentID(int departmentID) {
		DepartmentID = departmentID;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public String getSupplierCode() {
		return SupplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		SupplierCode = supplierCode;
	}
}
