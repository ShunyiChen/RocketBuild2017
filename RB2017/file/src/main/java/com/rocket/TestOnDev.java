package com.rocket;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.rocket.client.TpaContext;
import com.rocket.tpa.api.CommunityApi;
import com.rocket.tpa.api.IdentityApi;
import com.rocket.tpa.api.LocationApi;
import com.rocket.tpa.api.TrushareApi;
import com.rocket.tpa.config.RuntimeConfig;
import com.rocket.tpa.mfx.MFXClient;
import com.rocket.tpa.mfx.listener.DownloadListener;
import com.rocket.tpa.mfx.listener.UploadListener;
import com.rocket.tpa.model.Enums.CommunityEncryptionType;
import com.rocket.tpa.model.Enums.KeyType;
import com.rocket.tpa.model.Enums.MetaDataFieldType;
import com.rocket.tpa.model.Enums.MetaDataType;
import com.rocket.tpa.model.Enums.ShareActions;
import com.rocket.tpa.model.admin.Community;
import com.rocket.tpa.model.admin.MetaDataField;
import com.rocket.tpa.model.common.Filter;
import com.rocket.tpa.model.identity.CustomCertificateToken;
import com.rocket.tpa.model.identity.EncryptionKey;
import com.rocket.tpa.model.identity.KeyData;
import com.rocket.tpa.model.identity.Location;
import com.rocket.tpa.model.identity.User;
import com.rocket.tpa.model.identity.interfaces.IIdentityToken;
import com.rocket.tpa.model.tpackage.PackageActionsDetail;
import com.rocket.tpa.model.tpackage.PackageFilterType;
import com.rocket.tpa.model.tpackage.PackageFilters;
import com.rocket.tpa.model.tpackage.PackagePayload;
import com.rocket.tpa.model.tpackage.Recipient;
import com.rocket.tpa.model.tpackage.TpaPackage;

/**
 * 
 * TEST OBJECTIVES:
 * 
 * (1)JSafe RSA library replacement with Java Standard library; (2)MD5
 * replacement with SHA256; (3)Migration path 1024 to 2048 RSA keys;
 * 
 * 
 * @author chens
 */
public class TestOnDev {

	// TRUcore user
	public String recipientUsername = "ventchev11";
	public String recipientFirstName = "Vladtest";
	public String recipientLastName = "Test";
	public String recipientCompanyName = "Rocket Software";
	// TFE user
	public String senderUserName = "tfetpa1";
	private String tokenString = "1URhI01VOE383zTkxkV5fL10+e8sN7QOsNa2hHxMjderAoa3efM3R1rKbAQxW5q2L05QWzIFrKdDzpwvu904g8AIBBuJssjJXR5+FrCAxYF/mNjn/L/BRWn2ZKyE1LHZ";
	private TpaContext context;
	private MFXClient mfxClient;
	private IdentityApi identityApi = null;
	private TrushareApi trushareApi;
	private LocationApi locationApi;
	private CommunityApi communityApi;
	private CustomCertificateToken token;
	
	private Callback cb;
	
	public static void main(String[] args) {
//		// Downloads files in a multithreaded environment
//		Thread t1 = new Thread() {
//			@Override
//			public void run() {
//				TestOnDev t = new TestOnDev();
//				t.setUp();
//				t.download("8799H917577");
//			}
//		};
//		Thread t2 = new Thread() {
//			@Override
//			public void run() {
//				TestOnDev t = new TestOnDev();
//				t.setUp();
//				t.download("8799H917575");
//			}
//		};
//		t2.start();
//		t1.start();
		
		String dt = System.currentTimeMillis()+"";
		dt = dt.substring(0, dt.length() -3);
		int datetime = Integer.parseInt(dt);
		
		System.out.println(datetime);
		
	}

	// Upload file paths
	private String[] uploadFiles = {
			// "C:\\Demo\\Files\\500MB.zip"
			"C:\\Demo\\Files\\Koala.jpg"
			// "C:\\Demo\\Files\\175MB.zip"
	};
	
	@Before
	public void setUp() {
		System.out.println("SDK initialization ...");
		context = new TpaContext(getClass().getResourceAsStream("/tpa-sdk-config.dev.xml"));
//		context = new TpaContext(
//				"C:\\Users\\chens\\Documents\\development\\gxg_java\\TPA-SDK-Java\\config\\tpa-sdk-config.dev.xml");
		mfxClient = (MFXClient) context.getApi("MFXClient");
		identityApi = (IdentityApi) context.getApi("IdentityApi");
		locationApi = (LocationApi) context.getApi("LocationApi");
		trushareApi = (TrushareApi) context.getApi("TrushareApi");
		communityApi = (CommunityApi) context.getApi("CommunityApi");
		token = new CustomCertificateToken();
		token.setAuthenticationToken(tokenString);
	}
	
	public void setUploadFiles(String[] uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

//	 @Test
	public void testcase1() {
		create();
	}

//	@Test
//	public void testcase3() {
//		download("8799H917575");
//	}

	@After
	public void tearDown() {
	}
	

	private void create() {
		User user = this.getUserAsync(senderUserName);
		Assert.assertNotNull(user);
		KeyData keyData = new KeyData();
		keyData.UserUniqueID = user.UserUniqueID;
		keyData.Username = user.Username;
		keyData.KeyType = KeyType.User;
		// Create the key pair(public key and private key)
		identityApi.createKeyAsync(token, keyData);
	}

	public void upload(Callback cb) {
		this.cb = cb;
		// Get sender's user profile
		User sender = this.getUserAsync(this.senderUserName);
		List<File> files = new ArrayList<File>();
		for (String path : uploadFiles) {
			File uploadFile = new File(path);
			files.add(uploadFile);
		}
		// Get sender's community
		List<Community> lsCommunities = communityApi.getCommunitiesOfUserAsync(token, sender.UserUniqueID);
		if (lsCommunities.size() == 0)
			return;
		Community community = lsCommunities.get(0);
		// Find partners by the recipient names
		List<Recipient> partners = trushareApi.findPartnersAsync(token, community.CommunityUniqueID,
				recipientCompanyName, null, null, recipientFirstName, recipientLastName, null, null, null, null, 0, 99);
		// Filter the recipient
		Iterator<Recipient> iter = partners.iterator();
		while (iter.hasNext()) {
			Recipient rec = iter.next();
			if (!rec.Username.equals(recipientUsername)) {
				iter.remove();
			}
		}
		// Fill the package information
		TpaPackage tpackage = fillPackage(token, community.CommunityUniqueID, partners, sender, files, "no comments.");
		// Create the package on the server
		tpackage = trushareApi.createPackageAsync(token, tpackage);
		// Upload package files
		uploadPackageFilesAsync(token, tpackage, community, sender, partners, files);
	}
	

	private TpaPackage fillPackage(IIdentityToken token, String communityID, List<Recipient> lsRecipients, User sender,
			List<File> files, String comments) {
		TpaPackage tpaPackage = new TpaPackage();
		tpaPackage.Sender = sender;
		for (File file : files) {
			PackagePayload payLoad = new PackagePayload();
			payLoad.FileName = file.getName();
			payLoad.FilePath = file.getAbsolutePath();
			if (file.isFile()) {
				payLoad.FileSize = file.length();
			}
			payLoad.IsFolder = file.isDirectory();
			tpaPackage.Payloads.add(payLoad);
		}
		// Validity period of the package
		tpaPackage.Duration = 1;
		// Sender's comments
		tpaPackage.Comments = comments;
		// Recipients list
		tpaPackage.Recipients = lsRecipients;
		// Community ID
		tpaPackage.CommunityUniqueID = communityID;

		// Set values to meta data
		List<MetaDataField> lstmetadata = communityApi.getAllMetaDataFieldsAsync(token, communityID,
				MetaDataType.MessageManager);
		for (MetaDataField mf : lstmetadata) {
			MetaDataField mt = new MetaDataField();
			if (mf.FieldType == MetaDataFieldType.SingleSelect.getValue()) {
				mt.MetaDataFieldID = mf.MetaDataFieldID;
				mt.Label = mf.Label;
				mt.Description = mf.Description;
				mt.EntryValue = mf.FieldValues.get(0).Value;
			} else {
				mt.MetaDataFieldID = mf.MetaDataFieldID;
				mt.Description = mf.Description;
				mt.Label = mf.Label;
				mt.EntryValue = "1";
			}
			tpaPackage.MetaData.add(mt);
		}
		return tpaPackage;
	}

	private void uploadPackageFilesAsync(final IIdentityToken token, final TpaPackage package1, Community community,
			User sender, final List<Recipient> recipients, List<File> files) {
		try {
			// Prepare the public key and private key for sender
			KeyData senderPrivateKeyInfo = new KeyData();
			senderPrivateKeyInfo.setKeyType(KeyType.User);
			senderPrivateKeyInfo.setUserUniqueID(sender._userUniqueID);
			EncryptionKey PublicKey = new EncryptionKey();
			PublicKey.setKeyPassphrase(sender.Username);
			PublicKey.setKeyString(sender.PublicKey);
			senderPrivateKeyInfo.setPublicKey(PublicKey);
			senderPrivateKeyInfo.setUsername(sender.Username);
			// Prepare the public keys for recipients
			Map<String, String> recipientPassphraseKeys = new HashMap<String, String>();
			for (User recipient : recipients) {
				if (recipient.Username != null && !recipient.Username.equals("") && recipient.PublicKey != null
						&& !recipient.PublicKey.equals("")) {
					recipientPassphraseKeys.put(recipient.Username, recipient.PublicKey);
				}
			}
			// Executes the file upload
			mfxClient.encryptAndUploadAsync(token, senderPrivateKeyInfo, files, recipientPassphraseKeys,
					package1.PackageID, RuntimeConfig.ENCRYPT_FOLDER, new UploadListener() {

						@Override
						public void onStartEncrypt() {
							System.out.println("UploadListener : onStartEncrypt");
						}

						@Override
						public void onFinishEncrypt() {
							System.out.println("UploadListener : onFinishEncrypt");
						}

						@Override
						public void onFileUploadStart(long totalSize) {
							System.out.println("UploadListener : onFileUploadStart and total size is " + totalSize);
						}

						@Override
						public void onSingleFileUploading(int uploadRate, double transferDataBytes, double percent) {
							System.out.println("UploadListener :onSingleFileUploading " + uploadRate + "KB/S");
							// System.out.println("UploadListener : onSingleFileUploading " +
							// transferDataBytes + "KB");
						}

						@Override
						public void onException(Exception ex) {
							System.out.println("UploadListener : onException : " + ex.getMessage());
						}

						@Override
						public void onSingleFileUploadFinish() {
							System.out.println("UploadListener : onSingleFileUploadFinish");

						}

						@Override
						public void onCompletelyFinish() {
							
//							DBinsert(package1);
							cb.call(package1);
							
							System.out.println("UploadListener : onCompletelyFinish");
							updateStatus(token, package1.PackageID, recipients, ShareActions.Send);
						}

					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateStatus(IIdentityToken token, String packageID, List<Recipient> recipients, ShareActions act) {
		PackageActionsDetail packageAction = new PackageActionsDetail();
		packageAction.Action = act;
		packageAction.PackageID = packageID;
		packageAction.Recipients = recipients;
		packageAction.ClientApp = "TFE";
		trushareApi.actionPackageAsync(token, packageAction);
	}
	
//	private void DBinsert(TpaPackage pack) {
//		String sql1 = "UPDATE ddxadmin.JOB SET STATUS=2 where id="+jobId;
//		this.jdbcTemplate.execute(sql1);
//		
//		String sql = "INSERT INTO ddxadmin.JOB_TRUCORE (ID,ID_JOB,TRANS_ID,TRANS_REF,STATUS_TRANS,STATUS_SENDER,STATUS_TEXT,DELETED) values ("+jobId+", "+jobId+", '"+pack.PackageID+"','"+pack.TransactionRefNumber+"',null,null,'Created',1)";
//		this.jdbcTemplate.execute(sql);
//		
//		String sql3 = "INSERT INTO ddxadmin.JOB_CONTACT (ID, ID_JOB,ID_RECEIVER, RECEIVER_TYPE, FIRSTNAME, FAMILYNAME, department,phone, fax, email, id_medium, medium_type,engdat_abstract, engdat_confirm, deleted) VALUES ("+jobId+", "+jobId+", 4, 1, 'Vladtest', 'Test', '_2D', '123456789', '_22', 'Dummy_40Dummy.com1', 1, 10, 1,1, 1)";
//		this.jdbcTemplate.execute(sql3);
//	}

	public void download(String refnumber, Callback callback) {
		try {
			PackageFilters fltr = new PackageFilters();
			List<TpaPackage> packages = trushareApi.getPackagesAsync(token, fltr, PackageFilterType.RECEIVED);
			for (final TpaPackage pack : packages) {
				if (refnumber.equals(pack.TransactionRefNumber)) {
					KeyData recipientPrivateKeyInfo = new KeyData();
					recipientPrivateKeyInfo.KeyType = KeyType.User;
					if (pack.CommunityEncryptionType == CommunityEncryptionType.UserkeystoredonUserMachine) {
						KeyData param = new KeyData();
						param.KeyType = KeyType.User;
						param.UserUniqueID = pack.Sender.UserUniqueID;
						EncryptionKey senderPublicKey = identityApi.getPublicKeyAsync(token, param);
						// Get public key
						EncryptionKey PublicKey = new EncryptionKey();
						PublicKey.KeyString = senderPublicKey.KeyString;
						PublicKey.KeyPassphrase = senderUserName;
						// Get private key
						EncryptionKey PrivateKey = new EncryptionKey();
						PrivateKey.KeyPassphrase = senderUserName;
						recipientPrivateKeyInfo.setPublicKey(PublicKey);
						recipientPrivateKeyInfo.setPrivateKey(PrivateKey);
						recipientPrivateKeyInfo.setUsername(senderUserName);
						// Get files list
						List<PackagePayload> filesPayload = new ArrayList<PackagePayload>();
						for (PackagePayload p : pack.Payloads) {
							if (!p.IsFolder) {
								filesPayload.add(p);
							}
						}
						// Executes the file download
						mfxClient.downloadAndDecryptFilesAsync(token, recipientPrivateKeyInfo, pack.PackageID,
								filesPayload, pack.TotalSizeInBytes, RuntimeConfig.DOWNLOAD_FOLDER,
								new DownloadListener() {

									@Override
									public void onStartDecrypt() {
										System.out.println("DownloadListener : onStartDncrypt");
									}

									@Override
									public void onFinishDecrypt() {
										System.out.println("DownloadListener : onFinishDncrypt");
									}

									@Override
									public void onFileDownloadStart(long totalSize) {
										System.out.println(
												"DownloadListener : onFileDownloadStart,total size is " + totalSize);
									}

									@Override
									public void onSingleFileDownloading(int uploadRate, double transferDataBytes,
											double percent) {
									}

									@Override
									public void onException(Exception ex) {
										System.out.println("on Exception :" + ex.getLocalizedMessage());
										return;
									}

									@Override
									public void onSingleFileDownloadFinish() {
									}

									@Override
									public void onCompletelyFinish() {
										
										callback.call(pack);
										
										System.out.println("DownloadListener : onCompletelyFinish");
										List<Recipient> lst = new ArrayList<Recipient>();
										Recipient r = new Recipient();
										User user = getUserAsync(senderUserName);
										r.set_userUniqueID(user.getUserUniqueID());
										r.setUserUniqueID(user.getUserUniqueID());
										lst.add(r);
										// Update the package status when it finished the download
										updateStatus(token, pack.PackageID, lst, ShareActions.Download);
									}

								}, true);
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		return _user;
	}

	/**
	 * call get location API and return fixed token user location;
	 *
	 * @return
	 */
	private Location getLocationAsync() {
		Location location = locationApi.getMyLocationAsync(token);
		return location;
	}

	/**
	 * call get location users and return a list of user from that location
	 *
	 * @param location
	 * @return
	 */
	private List<User> getLocationUsersAsync(Location location) {
		List<User> users = locationApi.getLocationUsersAsync(token, location.LocationUniqueID, new Filter<User>());
		return users;
	}
	
}
