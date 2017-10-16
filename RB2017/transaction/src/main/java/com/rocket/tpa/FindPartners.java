package com.rocket.tpa;

import java.util.ArrayList;
import java.util.List;

import com.rocket.client.TpaContext;
import com.rocket.tpa.api.CommunityApi;
import com.rocket.tpa.api.LocationApi;
import com.rocket.tpa.api.TrushareApi;
import com.rocket.tpa.model.admin.Community;
import com.rocket.tpa.model.common.Filter;
import com.rocket.tpa.model.identity.CustomCertificateToken;
import com.rocket.tpa.model.identity.Location;
import com.rocket.tpa.model.identity.User;
import com.rocket.tpa.model.identity.interfaces.IIdentityToken;
import com.rocket.tpa.model.tpackage.Recipient;

public class FindPartners {

	public static String tokenString = "OJf5/FHwXcSsaXYYxCAhpo2r8y+w43k3+7GGIA05VTxqN+Y8E3OyElKibEVx9DM"
			+ "zGymLd3ErhmLW3bdx/OuKsMpVwhfwb0SDR6ujldeWQwtU/GFHpP+9fhgWgbMfNajkZoD+yTfuMfn3ncFZXP4K0Q==";
	public static String communityName = "AFX";
	public static String senderUserName = "tfedevthree";
	public static String recipientUsername = "kamafx";
	public static String recipientFirstName = "Kam";
	public static String recipientLastName = "AFXAdmin";
	public static String recipientCompanyName = "KamAFX";
	private LocationApi locationApi = null;
	private IIdentityToken uToken = null;
	private CommunityApi communityApi = null;
	private TrushareApi trushareApi = null;
	
	public FindPartners(TpaContext context) {
		this.locationApi = (LocationApi) context.getApi("LocationApi");
		trushareApi = (TrushareApi) context.getApi("TrushareApi");
		communityApi = (CommunityApi) context.getApi("CommunityApi");
		uToken = new CustomCertificateToken();
		uToken.setAuthenticationToken(tokenString);
	}

	public List<Recipient> call() {
		User sender = this.getUserAsync(senderUserName);
		// Get sender's community
		List<Community> lsCommunities = communityApi.getCommunitiesOfUserAsync(uToken, sender.UserUniqueID);
		if (lsCommunities.size() > 0) {
			Community community = lsCommunities.get(0);
			// Find partners by the recipient names
			List<Recipient> partners = this.trushareApi.findPartnersAsync(uToken, community.CommunityUniqueID,
					recipientCompanyName, null, null, recipientFirstName, recipientLastName, null, null, null, null, 0,
					99);
			return partners;
		} else {
			return new ArrayList<Recipient>();
		}
	}

	/**
	 * Query a user by username in location users
	 *
	 * @param username
	 * @return
	 */
	private User getUserAsync(String username) {
		Location location = this.getLocationAsync();
		List<User> users = this.getLocationUsersAsync(location);
		User _user = null;
		for (User user : users) {
			if (user.getUsername().equals(username)) {
				_user = user;
			}
		}
		// Assert.assertTrue(_user != null);
		return _user;
	}

	/**
	 * call get location API and return fixed token user location;
	 *
	 * @return
	 */
	private Location getLocationAsync() {
		Location location = locationApi.getMyLocationAsync(uToken);
		return location;
	}

	/**
	 * call get location users and return a list of user from that location
	 *
	 * @param location
	 * @return
	 */
	private List<User> getLocationUsersAsync(Location location) {
		List<User> users = locationApi.getLocationUsersAsync(uToken, location.LocationUniqueID, new Filter<User>());
		return users;
	}
}
