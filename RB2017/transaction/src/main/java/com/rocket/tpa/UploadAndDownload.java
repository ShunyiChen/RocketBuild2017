package com.rocket.tpa;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rocket.client.TpaContext;
import com.rocket.tpa.api.CommunityApi;
import com.rocket.tpa.api.IdentityApi;
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
import com.rocket.tpa.model.identity.EncryptionKey;
import com.rocket.tpa.model.identity.KeyData;
import com.rocket.tpa.model.identity.User;
import com.rocket.tpa.model.identity.interfaces.IIdentityToken;
import com.rocket.tpa.model.tpackage.PackageActionsDetail;
import com.rocket.tpa.model.tpackage.PackageFilterType;
import com.rocket.tpa.model.tpackage.PackageFilters;
import com.rocket.tpa.model.tpackage.PackagePayload;
import com.rocket.tpa.model.tpackage.Recipient;
import com.rocket.tpa.model.tpackage.TpaPackage;

public class UploadAndDownload {

	private MFXClient mfxClient = null;
	private TrushareApi trushareApi = null;
	private CommunityApi communityApi = null;
	private IdentityApi identityApi = null;

	public UploadAndDownload(TpaContext context) {
		mfxClient = (MFXClient) context.getApi("MFXClient");
		trushareApi = (TrushareApi) context.getApi("TrushareApi");
		communityApi = (CommunityApi) context.getApi("CommunityApi");
		identityApi = (IdentityApi) context.getApi("IdentityApi");
	}

	/**
	 * Fill the package information
	 *
	 * @param token
	 *            Sender's token
	 * @param communityID
	 *            Sender's community Id
	 * @param lsRecipients
	 *            Recipient list
	 * @param sender
	 *            Sender object
	 * @param files
	 *            File list
	 * @param comments
	 *            Comments
	 * @return
	 */
	public TpaPackage fillPackage(IIdentityToken token, String communityID, List<Recipient> lsRecipients, User sender,
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

	/**
	 * Upload package files
	 *
	 * @param token
	 *            Sender's token
	 * @param package1
	 *            created package
	 * @param community
	 *            Sender's community
	 * @param sender
	 *            Sender object
	 * @param recipients
	 *            Recipients list
	 * @param files
	 *            File list
	 */
	public void uploadPackageFilesAsync(final IIdentityToken token, final TpaPackage package1, Community community,
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
							System.out.println("Done.");
							updateTransactionStatus(token, package1.PackageID, recipients, ShareActions.Send);
						}

					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Download package
	 *
	 * @param refNumber
	 *            Specified transaction reference number to download
	 * @param token
	 *            Receiver's token
	 * @param recipient
	 *            Receiver object
	 */
	public void downloadPackage(String refNumber, final IIdentityToken token, final User recipient) {
		try {
			PackageFilters fltr = new PackageFilters();
			List<TpaPackage> packages = trushareApi.getPackagesAsync(token, fltr, PackageFilterType.RECEIVED);
			for (final TpaPackage pack : packages) {
				if (refNumber.equals(pack.TransactionRefNumber)) {
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
						PublicKey.KeyPassphrase = recipient.Username;
						// Get private key
						EncryptionKey PrivateKey = new EncryptionKey();
						PrivateKey.KeyPassphrase = recipient.Username;
						recipientPrivateKeyInfo.setPublicKey(PublicKey);
						recipientPrivateKeyInfo.setPrivateKey(PrivateKey);
						recipientPrivateKeyInfo.setUsername(recipient.Username);
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
										List<Recipient> lst = new ArrayList<Recipient>();
										Recipient r = new Recipient();
										r.set_userUniqueID(recipient.getUserUniqueID());
										r.setUserUniqueID(recipient.getUserUniqueID());
										lst.add(r);
										updateTransactionStatus(token, pack.PackageID, lst, ShareActions.Download);
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
	 * Update transaction status if the upload/download was finished.
	 *
	 * @param token
	 *            user's token
	 * @param packageID
	 *            Package Id
	 * @param recipients
	 *            Recipient list
	 * @param act
	 *            Transaction status
	 */
	private void updateTransactionStatus(IIdentityToken token, String packageID, List<Recipient> recipients,
			ShareActions act) {
		PackageActionsDetail packageAction = new PackageActionsDetail();
		packageAction.Action = act;
		packageAction.PackageID = packageID;
		packageAction.Recipients = recipients;
		packageAction.ClientApp = "JWS";
		trushareApi.actionPackageAsync(token, packageAction);
	}
}
