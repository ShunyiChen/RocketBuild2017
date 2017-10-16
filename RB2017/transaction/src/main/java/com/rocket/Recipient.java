package com.rocket;

import java.util.ArrayList;
import java.util.List;
import com.rocket.tpa.model.admin.Community;
import com.rocket.tpa.model.identity.User;

public class Recipient extends User {
	public Recipient() {
		super();
		CommonCommunities = new ArrayList<Community>();
	}

	public int DepartmentID;
	public String DepartmentUniqueID;
	public int RecipientTypeID;
	public int RegistrationRequestID;
	public int RecipientID;
	public List<Community> CommonCommunities;
	public boolean CanAdd;
	public String Conflicts;
	public String DepartmentName;
	public String FullName;
	public boolean IsExternal;
	public boolean IsTPalreadyAdded; // Added shahid
	public boolean IsOEM; // Added shahid
	public String GroupName;
	public String CommunityName;
	public int id;
	public int LocationTypeID;
}
