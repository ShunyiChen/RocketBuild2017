package com.procaess.ddxv6.serverDatabase;

import com.procaess.common2.crypt.PCrypt;
import com.procaess.common2.utils.ErrorHandler;
import com.procaess.common2.utils.Filesystem;
import com.procaess.common2.utils.PJavaException;

public class DDXDatabaseUtilities {

	public static String DDXNullString = "";

	public static String stringToCryptedString(String decryptedString) {
		/*                                                                            */
		String encrypted = "NULL";
		/*                                                                            */
		if (decryptedString == null)
			return ("NULL");
		/*                                                                            */
		if (decryptedString.equals(DDXNullString))
			return ("NULL");
		/*                                                                            */
		try {
			// Encode string
			PCrypt crypt = new PCrypt(ErrorHandler.getInstance());
			encrypted = crypt.encode(decryptedString);
		} catch (PJavaException pEx) {
			// System.out.println("PJavaException in stringToCryptedString.");
			// pEx.printStackTrace();
		}
		return "'" + encrypted + "'";
	}

	public static String stringToTableString(String decodedString) {
		/*                                                                            */
		if (decodedString == null)
			return ("NULL");
		/*                                                                            */
		if (decodedString.equals(DDXNullString))
			return ("NULL");
		/*                                                                            */
		String result = Filesystem.toFilesystem(decodedString);

		return "'" + result + "'";
	}
}
