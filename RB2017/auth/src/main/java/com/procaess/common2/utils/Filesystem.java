/*============================================================================*/
/*=====                                                                  =====*/
/*====    PPPP                     CCCC    AAA     EEEE                   ====*/
/*===     P   P                   C       A   A   E                        ===*/
/*==      P   P                   C       A   A   E                         ==*/
/*=       PPPP    rrrrr    ooo    C       AAAAA   EEE      ssss    ssss      =*/
/*==      P        rr     o   o   C       A   A   E       sss     sss       ==*/
/*===     P        r      o   o   C       A   A   E         sss     sss    ===*/
/*====    P        r       ooo     CCCC   A   A    EEEE   ssss    ssss    ====*/
/*=====                                                                  =====*/
/*============================================================================*/
/*                                                                            */
/*        ProCAEss GmbH                   email: Info@procaess.com            */
/*        Klaus-von-Klitzing-Str. 3       phone: +49 6341/954-183             */
/*        76829 Landau                    fax:   +49 6341/954-184             */
/*                                                                            */
/*============================================================================*/
/*                                                                            */
//  Project:    Filesystem
//  Source:     Filesystem.java
//  Language:   Java
//  Author:     $Author: rb $
//  Date:       $Date: 2010/05/07 16:05:19 $
//  Revision:   $Revision: 1.25 $
//  State:      $State: Exp $
/*                                                                            */
/*============================================================================*/
/*                                                                            */
/*    Package:                                                                */
/*    ========                                                                */
/*                                                                            */
package com.procaess.common2.utils;

/*                                                                            */
/* ============================================================================ */
/*                                                                            */
/* Import: */
/* ======= */
/*                                                                            */
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import javax.activation.DataHandler;


/*                                                                            */
/**
 * Common filesystem methods for all ProCAEss products.
 * 
 * <p>
 * Imports:
 * <ul>
 * <li> java.io.*
 * <li> java.util.*
 * </ul>
 */
/*                                                                            */
/* ============================================================================ */
/*                                                                            */
/* Class: Filesystem */
/* ====== */
/*                                                                            */
public class Filesystem {
	/*                                                                            */
	/* ============================================================================ */
	/*                                                                            */
	/* - Public Members: */
	/* =============== */
	/*                                                                            */
	public static String FILTER_WILDCARD = "*";

	/*                                                                            */
	/* ============================================================================ */
	/*                                                                            */
	/* - Public Methods: */
	/* =============== */
	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Retrieve the filename of a path/filename string.
	 * 
	 * @param pathAndFilename
	 *            path and filename to read path from
	 * @return String with filename
	 * @return null if wrong path separator
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static String getFilename(String pathAndFilename) {
		File theFile = new File(pathAndFilename);
		return theFile.getName();
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Retrieve the path of a path/filename string.
	 * 
	 * @param pathAndFilename
	 *            path and filename to read filename from
	 * @return String with path
	 * @return null if wrong path separator
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static String getPath(String pathAndFilename) {
		File theFile = new File(pathAndFilename);
		return theFile.getParent();
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Change a path to platform-specific path separators.
	 * 
	 * @param directory
	 *            directory name to convert
	 * @return String with converted path
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static String path(String directory) {
		String newDirectory = null;
		if (File.separatorChar == '/')
			newDirectory = directory.replace('\\', File.separatorChar);
		if (File.separatorChar == '\\')
			newDirectory = directory.replace('/', File.separatorChar);
		return newDirectory;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * List content of a directory.
	 * 
	 * @param directory
	 *            directory to list
	 * @return String array with listed files
	 * @return null if any error
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static String[] dir(String directory) {
		/*                                                                            */
		File theDirectory = new File(directory);
		String[] theList = theDirectory.list();
		/*                                                                            */
		return theList;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * List content of a directory with a filter.
	 * 
	 * @param directory
	 *            directory to list
	 * @param filter
	 *            filter string, e.g. "*.*" or "*.zip"
	 * @return String array with listed files
	 * @return null if any error
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static String[] dir(String directory, String filter) {
		/*                                                                            */
		/** - - Check if filter equals wildcard character */
		/*                                                                            */
		if (filter.compareTo(Filesystem.FILTER_WILDCARD) == 0)
			return Filesystem.dir(directory);
		/*                                                                            */
		/* - - Create file list */
		/*                                                                            */
		File theDirectory = new File(directory);
		String[] theList = theDirectory.list();
		/*                                                                            */
		if (theList == null) {
			return null;
		}
		/*                                                                            */
		/* - - Create filtered list */
		/*                                                                            */
		Vector vector = new Vector();
		/*                                                                            */
		/* - - CASE 1: filter starts and ends with wildcard */
		/*                                                                            */
		if (filter.startsWith(Filesystem.FILTER_WILDCARD)
				&& filter.endsWith(Filesystem.FILTER_WILDCARD)) {
			/*                                                                            */
			String substringToFind = filter.substring(1, filter.length() - 1)
					.toUpperCase();
			for (int i = 0; i < theList.length; i++) {
				if (theList[i].toUpperCase().indexOf(substringToFind) >= 0)
					vector.addElement(theList[i]);
			}
		}
		/*                                                                            */
		/* - - CASE 2: filter only starts with wildcard */
		/*                                                                            */
		else if (filter.startsWith(Filesystem.FILTER_WILDCARD)) {
			/*                                                                            */
			String substringToFind = filter.substring(1).toUpperCase();
			/*                                                                            */
			for (int i = 0; i < theList.length; i++) {
				if (theList[i].toUpperCase().endsWith(substringToFind))
					vector.addElement(theList[i]);
			}
		}
		/*                                                                            */
		/* - - CASE 3: filter is contained somewhere in the string */
		/*                                                                            */
		else if (filter.indexOf(Filesystem.FILTER_WILDCARD) != -1) {
			/*                                                                            */
			String upperFilter = filter.toUpperCase();

			for (int i = 0; i < theList.length; i++) {
				/*                                                                            */
				String listEntry = theList[i].toUpperCase();
				int listEntryIndex = 0;
				int filterIndex = 0;
				String filterPart = "";
				boolean entryIsOk = false;
				/*                                                                            */
				while (true) {
					int nextFilterIndex = upperFilter.indexOf(
							Filesystem.FILTER_WILDCARD, filterIndex) + 1;
					/*                                                                            */
					if (filterIndex == -1)
						break;
					if (nextFilterIndex != 0)
						filterPart = upperFilter.substring(filterIndex,
								nextFilterIndex - 1);
					else {
						// reached end of filter string, listentry matched.
						if (filterIndex >= upperFilter.length()) {
							entryIsOk = true;
							break;
						}
						filterPart = upperFilter.substring(filterIndex,
								upperFilter.length());
						nextFilterIndex = -1;
					}
					/*                                                                            */
					filterIndex = nextFilterIndex;
					// all parts of the filter have been controled,
					// and matched with the traited entry.
					if (filterPart.length() == 0
							&& listEntryIndex == listEntry.length()) {
						entryIsOk = true;
						break;
					}
					/*                                                                            */
					listEntryIndex = listEntry.indexOf(filterPart,
							listEntryIndex);
					// the part of the filter isnt contained,
					// in the traited entry.
					if (listEntryIndex == -1) {
						entryIsOk = false;
						break;
					}
					// set listEntryIndex on next position.
					listEntryIndex += filterPart.length();
					// all parts of the filter have been controled,
					// and matched with the traited entry.
					if (nextFilterIndex == -1
							&& listEntryIndex == listEntry.length()) {
						entryIsOk = true;
						break;
					}
				}
				if (entryIsOk)
					vector.addElement(theList[i]);
				/*                                                                            */
			}
		}
		/*                                                                            */
		/* - - CASE 4: filter only ends with wildcard */
		/*                                                                            */
		else {
			/*                                                                            */
			String substringToFind = filter.substring(0, filter.length() - 1);
			/*                                                                            */
			for (int i = 0; i < theList.length; i++) {
				if (theList[i].toUpperCase().startsWith(
						substringToFind.toUpperCase()))
					vector.addElement(theList[i]);
			}
			/*                                                                            */
		}
		String[] filteredList = new String[vector.size()];
		vector.copyInto(filteredList);
		/*                                                                            */
		return filteredList;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Delete a directory. This method does not delete parent directories.
	 * 
	 * @param directory
	 *            directory to delete
	 * @return true if delete succeeded
	 * @return false if delete failed
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static boolean rmdir(String directory) {
		boolean result = delete(directory);
		return result;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Remove all files of a directory and the directory itself.
	 * 
	 * @param directory
	 *            directory to delete
	 * @return true if delete succeeded
	 * @return false if delete failed
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static boolean removeDir(String directory) {
		/*                                                                            */
		boolean successGlobal = true;
		/*                                                                            */
		File theFile = new File(directory);
		/*                                                                            */
		if (theFile.isDirectory()) {
			/*                                                                            */
			boolean successLocal = true;
			String[] fileList = theFile.list();
			for (int i = 0; fileList != null && i < fileList.length; i++) {
				successLocal = removeDir(directory + File.separator
						+ fileList[i]);
			}
			if (!successLocal)
				successGlobal = false;
			/*                                                                            */
		}
		/*                                                                            */
		if (!theFile.delete()) {
			ErrorHandler errorHandler = ErrorHandler.getInstance();
			errorHandler.logMessage(ErrorHandler.ERROR, 1, 0,
					"Filesystem.removeDir", "File.delete",
					"Could not delete file " + directory + ".");
			return false;
		}
		return successGlobal;
	}

	/**
	 * Check if the path corresponds to a directory
	 * 
	 * @param path
	 *            name of the path to check it it is a directory
	 * @return true if it is a directory, false if it is not a directory
	 */
	public static boolean isDirectory(String path) {
		File dir = new File(path);

		if (!dir.exists())
			return false;

		if (!dir.isDirectory())
			return false;

		return true;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Create a path. This method can create a directory including all
	 * non-existent parent directories.
	 * 
	 * @param path
	 *            name of path to create
	 * @return true if delete succeeded
	 * @return false if delete failed
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static boolean mkdir(String path) {
		/*                                                                            */
		File newFile = new File(path);
		/*                                                                            */
		/* - - Check if file entry exists and is a directory */
		/*                                                                            */
		if (newFile.exists() && newFile.isDirectory())
			return true;
		/*                                                                            */
		/* - - Check if file entry exists and is no directory */
		/*                                                                            */
		if (newFile.exists() && !newFile.isDirectory()) {
			ErrorHandler errorHandler = ErrorHandler.getInstance();
			errorHandler.logMessage(errorHandler.ERROR, 1, 0,
					"Filesystem.mkdir", "File.exists", "\"" + path
							+ "\" is not a directory.");
			return false;
		}
		/*                                                                            */
		/* - - File entry does not exist, try to create it */
		/*                                                                            */
		else {
			/*                                                                            */
			/* - - - Creation was successful, print status message */
			/*                                                                            */
			if (newFile.mkdirs())
				return true;
			/*                                                                            */
			/* - - - Directory could not be created, print error */
			/*                                                                            */
			else {
				ErrorHandler errorHandler = ErrorHandler.getInstance();
				errorHandler.logMessage(errorHandler.ERROR, 4, 0,
						"Filesystem.mkdir", "File.exists",
						"Could not create \"" + path + "\".");
				return false;
			} /* if */
		} /* if */
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Delete a file.
	 * 
	 * @param filename
	 *            name of path to delete
	 * @return true if delete succeeded
	 * @return false if delete failed
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static boolean delete(String source) {
		/*                                                                            */
		/** - - Error handler */
		/*                                                                            */
		ErrorHandler errorHandler = ErrorHandler.getInstance();
		/*                                                                            */
		File theFile = new File(source);
		if (!theFile.exists()) {
			errorHandler.logMessage(ErrorHandler.WARNING, 1, 0,
					"Filesystem.delete", "File.exists", "\"" + source
							+ "\" does not exist.");
			return true;
		}
		if (!theFile.delete()) {
			errorHandler.logMessage(ErrorHandler.ERROR, 2, 0,
					"Filesystem.delete", "File.delete", "Could not delete \""
							+ source + "\".");
			return false;
		}
		return true;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Delete a file.
	 * 
	 * @param path
	 *            of file to delete
	 * @param filename
	 *            name of file to delete
	 * @return true if delete succeeded
	 * @return false if delete failed
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static boolean delete(String path, String filename) {
		boolean result = delete(path + File.separator + filename);
		return result;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Rename a file.
	 * 
	 * @param pathSource
	 *            source path
	 * @param filenameSource
	 *            source name of file to move
	 * @param pathDestination
	 *            destination path
	 * @param filenameDestination
	 *            destination name of file
	 * @return true if rename succeeded
	 * @return false if rename failed
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static boolean rename(String pathSource, String filenameSource,
								String pathDestination, String filenameDestination) {
		boolean result = rename(pathSource + File.separator + filenameSource,
				pathDestination + File.separator + filenameDestination);
		return result;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Rename a path.
	 * 
	 * @param source
	 *            source file or path to rename
	 * @param destination
	 *            destination file or path
	 * @return true if rename succeeded
	 * @return false if rename failed
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static boolean rename(String source, String destination) {
		boolean result = move(source, destination);
		return result;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Copy a file.
	 * 
	 * @param pathSource
	 *            source path
	 * @param filenameSource
	 *            source name of file to copy
	 * @param pathDestination
	 *            destination path
	 * @param filenameDestination
	 *            destination name of file
	 * @return true if copy succeeded
	 * @return false if copy failed
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static boolean copy(String pathSource, String filenameSource,
								String pathDestination, String filenameDestination) {
		boolean result = copy(pathSource + File.separator + filenameSource,
								pathDestination + File.separator + filenameDestination);
		return result;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Copy a directory including all files (subdirectories aren't copied!).
	 * 
	 * @param source
	 *            source directory path to copy
	 * @param destination
	 *            destination directory path
	 * @return true if copy succeeded
	 * @return false if copy failed
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static boolean copyDir(String sourceDir, String destinationDir) {
		// Check arguments
		File source = new File(sourceDir);
		if (!source.isDirectory())
			return false;
		File destination = new File(destinationDir);
		if (destination.exists() && !destination.isDirectory())
			return false;

		// Create destination directory
		if (!destination.exists()) {
			if (!destination.mkdirs())
				return false;
		}

		// Copy all files from source to destination directory
		boolean result = true;
		File[] files = source.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					String sourceFile = files[i].getPath();
					String destinationFile = destination.getPath()
							+ File.separator + files[i].getName();
					if (!copy(sourceFile, destinationFile))
						result = false;
				}
			}
		}

		return result;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Copy a file.
	 * 
	 * @param source
	 *            source name of file with path to copy
	 * @param destination
	 *            destination name of file with path
	 * @return true if copy succeeded
	 * @return false if copy failed
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static boolean copy(String source, String destination) {
		/*                                                                            */
		ErrorHandler errorHandler = ErrorHandler.getInstance();
		/*                                                                            */
		FileInputStream inputFile = null;
		BufferedInputStream bufferedInputFile = null;
		FileOutputStream outputFile = null;
		BufferedOutputStream bufferedOutputFile = null;
		int bufferSize = 512 * 1024;
		byte[] dataBuffer = new byte[bufferSize];
		int bytesRead = 0;
		/*                                                                            */

		try {
			inputFile = new FileInputStream(new File(source));
			bufferedInputFile = new BufferedInputStream(inputFile);
			outputFile = new FileOutputStream(new File(destination));
			bufferedOutputFile = new BufferedOutputStream(outputFile);
			/*                                                                            */
			while ((bytesRead = bufferedInputFile.read(dataBuffer, 0, bufferSize))!=-1) {
				bufferedOutputFile.write(dataBuffer, 0, bytesRead);
				delay(1);
			}
			bufferedInputFile.close();
			bufferedOutputFile.close();
			inputFile.close();
			outputFile.close();
			outputFile = null;
			inputFile = null;
			bufferedInputFile = null;
			bufferedOutputFile = null;
			// Wurde von MM eingebaut - Vermutlich um sicherzustellen, dass alle
			// File-Handels wieder freigegeben werden.
			System.gc();
			System.runFinalization();
			/*                                                                            */
		} catch (IOException e) {
			errorHandler.logMessage(ErrorHandler.ERROR, 3, 0,
					"Filesystem.copy", "Stream*", "Could not copy file \""
							+ source + "\" to \"" + destination + "\": "
							+ e.toString());
			return false;
			/*                                                                            */
		} catch (Throwable e) {
			errorHandler.logMessage(ErrorHandler.ERROR, 6, 0,
					"Filesystem.copy", "Stream*", "Could not copy file \""
							+ source + "\" to \"" + destination + "\": "
							+ e.toString());
			return false;
		}finally{
			if (bufferedInputFile != null){
				try {
					bufferedInputFile.close();
				} catch (Exception e) {
					// ignore
				}
			}
			if (bufferedOutputFile != null){
				try {
					bufferedOutputFile.close();
				} catch (Exception e) {
					// ignore
				}
			}
			if (inputFile != null){
				try {
					inputFile.close();
				} catch (Exception e) {
					// ignore
				}
			}
			if (outputFile != null){
				try {
					outputFile.close();
				} catch (Exception e) {
					// ignore
				}
			}
			File sourceFile = new File(source);
			if (sourceFile.lastModified() > 0) {
				File destFile = new File(destination);
				destFile.setLastModified(sourceFile.lastModified());
			}
		}

		return true;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Copy a file.
	 * 
	 * @param source
	 *            source name of file with path to copy
	 * @param destination
	 *            destination name of file with path
	 * @return true if copy succeeded
	 * @return false if copy failed
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	 

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Move a file.
	 * 
	 * @param pathSource
	 *            source path
	 * @param filenameSource
	 *            source name of file to move
	 * @param pathDestination
	 *            destination path
	 * @param filenameDestination
	 *            destination name of file
	 * @return true if move succeeded
	 * @return false if move failed
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static boolean move(String pathSource, String filenameSource,
			String pathDestination, String filenameDestination) {
		return move(pathSource + File.separator + filenameSource,
				pathDestination + File.separator + filenameDestination);
	}

	public static boolean move(String source, String destination) {
		File sourceFile = new File(source);
		File destinationFile = new File(destination);
		boolean result = move(sourceFile, destinationFile);
		return result;
	}
	
	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Move a file.
	 * 
	 * @param source
	 *            source path with path to move
	 * @param destination
	 *            name of file with path
	 * @return true if move succeeded
	 * @return false if move failed
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static boolean move(File sourceFile, File destinationFile) {
		/*                                                                            */
		ErrorHandler errorHandler = ErrorHandler.getInstance();
		/*                                                                            */
		if (destinationFile.exists()) {
			errorHandler.logMessage(ErrorHandler.WARNING, 1, 0,
					"Filesystem.move", "File.exists", "Destination \""
							+ destinationFile.getPath() + " exists.");
		}
		if (sourceFile.renameTo(destinationFile)) {
			return true;
		}
		else {
			errorHandler.logMessage(ErrorHandler.WARNING, 4, 0,
					"Filesystem.move", "File.renameTo", "Could not rename \""
							+ sourceFile.getPath() + "\" to \"" + destinationFile.getPath() + ".");
			if (!Filesystem.copy(sourceFile.getPath(), destinationFile.getPath())) {
				errorHandler.logMessage(ErrorHandler.ERROR, 2, 0,
						"Filesystem.move", "Filesystem.copy", "Copy from \""
								+ sourceFile.getPath() + "\" to \"" + destinationFile.getPath() + " failed.");
				return false;
			}
			if (!Filesystem.delete(sourceFile.getPath())) {
				errorHandler.logMessage(ErrorHandler.ERROR, 3, 0,
						"Filesystem.move", "Filesystem.delete",
						"Could not delete \"" + sourceFile.getPath() + ".");
				return false;
			}
			return true;
		}
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Move a file.
	 * 
	 * @param pathSource
	 *            source path
	 * @param filenameSource
	 *            source name of file to move
	 * @param pathDestination
	 *            destination path
	 * @param filenameDestination
	 *            destination name of file
	 * @return true if move succeeded
	 * @return false if move failed
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
 

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Return decoded string of a UNIQUE filename for each filesystem.
	 *
	 * @param codedString:
	 *            coded string of UNIQUE filesystem filename
	 * @return original filename (decodedString)
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static String fromFilesystem(String codedString) {
		char[] theChars = codedString.toCharArray();
		StringBuffer decodedString = new StringBuffer();

		for (int i = 0; i < codedString.length(); i++) {
			String newString = null;
			char newChar = theChars[i];
			if (newChar == '-' && i + 2 < codedString.length()) {
				int len = codedString.charAt(i + 1) - 'A';
				if (len >= 2 && ((i + 2 + len) <= codedString.length())) {
					String hex = codedString.substring(i + 2, i + 2 + len);
					for (int j = 0; j < hex.length(); j++) {
						char ch = hex.charAt(j);
						if (('0' <= ch && ch <= '9')
						 || ('A' <= ch && ch <= 'F')
						 || ('a' <= ch && ch <= 'f')
						) {
							continue;
						}
						else {
							hex = null;
							break;
						}
					}
					if (hex != null) {
						newChar = (char) Integer.parseInt(hex, 16);
						i = i + len + 1;
					}
				}
				newString = new String(new char[] { newChar });
			}
			else if(newChar != '_') {
				newString = new String(new char[] { newChar });
			}
			else if (i + 2 < codedString.length()) {
				if (codedString.substring(i, i + 3).equals("_$a")
						|| codedString.substring(i, i + 3).equals("_Ua")) {
					newString = "\u00E4"; // ae
				}
				else if (codedString.substring(i, i + 3).equals("_$o")
						|| codedString.substring(i, i + 3).equals("_Uo")) {
					newString = "\u00F6"; // oe
				}
				else if (codedString.substring(i, i + 3).equals("_$u")
						|| codedString.substring(i, i + 3).equals("_Uu")) {
					newString = "\u00FC"; // ue
				}
				else if (codedString.substring(i, i + 3).equals("_$A")
						|| codedString.substring(i, i + 3).equals("_UA")) {
					newString = "\u00C4"; // Ae
				}
				else if (codedString.substring(i, i + 3).equals("_$O")
						|| codedString.substring(i, i + 3).equals("_UO")) {
					newString = "\u00D6"; // Oe
				}
				else if (codedString.substring(i, i + 3).equals("_$U")
						|| codedString.substring(i, i + 3).equals("_UU")) {
					newString = "\u00DC"; // Ue
				}
				else {
					String hex = codedString.substring(i + 1, i + 3);
					if (hex.compareTo("8D") != 0) {
						try {
							if ( hex.equals( "00" ) )
								newString = codedString.substring(i, i + 3);
							else {
								newChar = (char) Integer.parseInt(hex, 16);
								if (newChar != '.' &&
										!(newChar >= '0' && newChar <= '9') && // 0..9
										!(newChar >= 'A' && newChar <= 'Z') && // A..Z
										!(newChar >= 'a' && newChar <= 'z'))   // a..z
								{
									newString = new String(new char[] { newChar });
								}
								else {
									newString = codedString.substring(i, i + 3);
								}
							}
						}
						catch (NumberFormatException ex) {
							newString = codedString.substring(i, i + 3);
						}
					}
				}
				i = i + 2;
			}
			if (newString != null) {
				decodedString.append(newString);
			}
		}
		String res = decodedString.toString();
		return res;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * Return coded string of a UNIQUE filename for each filesystem.
	 *
	 * @param decodedString:
	 * @return coded string of UNIQUE filesystem filename
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static String toFilesystem(String decodedString) {
		/*                                                                            */
		char[] theChars = decodedString.toCharArray();
		StringBuffer codedString = new StringBuffer();
		/*                                                                            */
		for (int i = 0; i < decodedString.length(); i++) {
			String newString = "_5F";
			char newChar = theChars[i];
			/*                                                                            */

			/* - Do not mask letters and digits and "." */
			/*                                                                            */
			if (newChar < 256) {
				if (newChar == '.' ||
					(newChar >= '0' && newChar <= '9') || // 0..9
					(newChar >= 'A' && newChar <= 'Z') || // A..Z
					(newChar >= 'a' && newChar <= 'z'))   // a..z
				{
					newString = new String(new char[] { theChars[i] });
				}
				else {
					if (newChar == '\u00E4') {	    // ae
						newString = "_Ua";
					}
					else if (newChar == '\u00F6') { // oe
						newString = "_Uo";
					}
					else if (newChar == '\u00FC') { // ue
						newString = "_Uu";
					}
					else if (newChar == '\u00C4') { // Ae
						newString = "_UA";
					}
					else if (newChar == '\u00D6') { // Oe
						newString = "_UO";
					}
					else if (newChar == '\u00DC') { // Ue
						newString = "_UU";
					}
					else {
						newString = Integer.toHexString(newChar).toUpperCase();
						if (newChar < 16) {
							newString = "_0" + newString;
						}
						else {
							newString = "_" + newString;
						}
					}
				}
			}
			else {
				String hexCode =  Integer.toHexString(newChar).toUpperCase();
				newString = "-" + (char)('A' + hexCode.length()) + hexCode;
			}
			codedString.append(newString);
		}
		String res = new String(codedString);
		return res;
	}


	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/***************************************************************************
	 * Return cleaned name of a windows filename (Replace forbidden character).
	 * 
	 * @param fileName:
	 *            name of file
	 * @return cleanedFileName: cleaned name of file
	 **************************************************************************/
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static String windowsFilename(String fileName) {
		/*                                                                            */
		char[] theChars = fileName.toCharArray();
		StringBuffer cleanedFileName = new StringBuffer();
		/*                                                                            */
		/* Mask forbidden character */
		/*                                                                            */
		for (int i = 0; i < fileName.length(); i++) {
			String newString = null;
			if (theChars[i] == '\\' || theChars[i] == '/' || theChars[i] == ':'
					|| theChars[i] == '*' || theChars[i] == '?'
					|| theChars[i] == '"' || theChars[i] == '<'
					|| theChars[i] == '>' || theChars[i] == '|')
				newString = "_";
			else
				newString = new String(new char[] { theChars[i] });
			/*                                                                            */
			cleanedFileName.append(newString);
		}
		return new String(cleanedFileName);
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/***************************************************************************
	 * Return cleaned windows string (Replace forbidden character).
	 * 
	 * @param windowsString:
	 *            windows string
	 * @return cleanedWinString: cleaned string for windows
	 **************************************************************************/
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static String cleanWindowsString(String windowsString) {
		/*                                                                            */
		char[] theChars = windowsString.toCharArray();
		StringBuffer cleanedWinString = new StringBuffer();
		/*                                                                            */
		/* Mask forbidden character */
		/*                                                                            */
		for (int i = 0; i < windowsString.length(); i++) {
			String newString = null;
			if (theChars[i] == '\\' || theChars[i] == '/' || theChars[i] == ':'
					|| theChars[i] == '*' || theChars[i] == '?'
					|| theChars[i] == '"' || theChars[i] == '<'
					|| theChars[i] == '>' || theChars[i] == '='
					|| theChars[i] == '\'' || theChars[i] == '?'
					|| theChars[i] == '!' || theChars[i] == '.'
					|| theChars[i] == '|')
				// newString = "_";
				newString = new String(new char[] { theChars[i] });
			else {
				if (Character.isLetter(theChars[i])
						|| Character.isDigit(theChars[i]) || theChars[i] == ' ') {
					newString = new String(new char[] { theChars[i] });
				} else {
					newString = "_";
				}
			}
			/*                                                                            */
			cleanedWinString.append(newString);
		}
		String res = new String(cleanedWinString);
		return res;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/***************************************************************************
	 * Return cleaned name of a filename (Replace special character).
	 * 
	 * @param fileName:
	 *            name of file
	 * @return cleanedFileName: cleaned name of file
	 **************************************************************************/
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static String cleanFilename(String fileName) {
		/*                                                                            */
		char[] theChars = fileName.toCharArray();
		StringBuffer cleanedFileName = new StringBuffer();
		/*                                                                            */
		for (int i = 0; i < fileName.length(); i++) {
			String newString = null;
			int asc = (int) theChars[i]; // ASCII-Code
			/*                                                                            */

			/* - Do not mask letters and digits and "." */
			/*                                                                            */
			if (theChars[i] == '.' || (asc > 47 && asc < 58) || // Ziffern 0..9
					(asc > 64 && asc < 91) || // Buchstaben A..Z
					(asc > 96 && asc < 123)) // Buchstaben a..z
				newString = new String(new char[] { theChars[i] });
			else {
				/*                                                                            */
				if (theChars[i] == '�')
					newString = "_Ua";
				else if (theChars[i] == '�')
					newString = "_Uo";
				else if (theChars[i] == '�')
					newString = "_Uu";
				else if (theChars[i] == '�')
					newString = "_UA";
				else if (theChars[i] == '�')
					newString = "_UO";
				else if (theChars[i] == '�')
					newString = "_UU";
				else
					newString = "_";
			}
			cleanedFileName.append(newString);
		}
		String res = new String(cleanedFileName);
		return res;
	}
	
	public static String cleanUtf8Filename(String fileNameString) {
    	char[] theChars = fileNameString.toCharArray();
		StringBuffer cleanedFileName = new StringBuffer();
		
		for(int i=0; i<theChars.length; i++){
			if(Character.isLetter(theChars[i]) || Character.isDigit(theChars[i]) || theChars[i] == '.'){
				cleanedFileName.append(theChars[i]);
			}else if(Character.isHighSurrogate(theChars[i]) || Character.isLowSurrogate(theChars[(i)])){
				cleanedFileName.append(theChars[i]);
			}else{
				cleanedFileName.append("_");
			}
		}
		
		String res = cleanedFileName.toString();
		return res;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/** Return the file size of a file in KB. * */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static long getFileSize(String path, String filename) {
		File theFile = new File(path, filename);
		long fs = theFile.length();
		if (fs > 0) {
			long filesize = (theFile.length() / 1024);
			if (filesize == 0) {
				return 1;
			} else {
				return filesize;
			}
		}
		return 0;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/***************************************************************************
	 * removes empty directories including f.
	 * 
	 * @param f
	 *            the directory to check for empty directories to remove
	 **************************************************************************/
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static void deleteEmptyDirectories(File f) {
		deleteEmptyDirectories(f, true);
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/***************************************************************************
	 * removes empty directories.
	 * 
	 * @param f
	 *            the directory to check for empty directories to remove
	 * @param withLocalRoot
	 *            also delete path to f if empty
	 **************************************************************************/
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static void deleteEmptyDirectories(File f, boolean withLocalRoot) {
		if (f.isDirectory()) {
			File sf[] = f.listFiles();
			for (int i = 0; i < sf.length; i++) {
				deleteEmptyDirectories(sf[i], true);
			}
			// create new File, perhaps it caches data, so that listFiles
			// delivers
			// wrong value.
			f = new File(f.toString());
			if (withLocalRoot && f.listFiles().length == 0) {
				f.delete();
			}
		}
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * retrieves a list containing all files (not directories) older than time.
	 * 
	 * @param f
	 *            the directory to check for old files
	 * @param time
	 *            the time in milliseconds since 1.1.1970
	 */
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static List searchFilesOlderThan(File f, long time) {
		ArrayList ret = new ArrayList();
		if (f.isFile()) {
			if (f.lastModified() < time) {
				ret.add(f);
			}
		} else if (f.isDirectory()) {
			File fs[] = f.listFiles();
			for (int i = 0; i < fs.length; i++) {
				ret.addAll(searchFilesOlderThan(fs[i], time));
			}
		}
		return ret;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/***************************************************************************
	 * Return cleaned name of a zip filename (Replace forbidden character).
	 * 
	 * @param fileName:
	 *            name of file
	 * @return cleanedFileName: cleaned name of file
	 **************************************************************************/
	/*----------------------------------------------------------------------------*/
	/*                                                                            */
	public static String filenameForCompress(String fileName) {
		/*                                                                            */
		char[] theChars = fileName.toCharArray();
		StringBuffer cleanedFileName = new StringBuffer();
		/*                                                                            */
		/* Mask forbidden character */
		/*                                                                            */
		for (int i = 0; i < fileName.length(); i++) {
			String newString = null;
			if (theChars[i] == '�')
				newString = "ae";
			else if (theChars[i] == '�')
				newString = "Ae";
			else if (theChars[i] == '�')
				newString = "oe";
			else if (theChars[i] == '�')
				newString = "Oe";
			else if (theChars[i] == '�')
				newString = "ue";
			else if (theChars[i] == '�')
				newString = "Ue";
			else if (theChars[i] == '�')
				newString = "ss";
			else
				newString = new String(new char[] { theChars[i] });
			/*                                                                            */
			cleanedFileName.append(newString);
		}
		String res = new String(cleanedFileName);
		return res;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * gets recursive all filenames of the source directory and of
	 * subdirectories
	 * 
	 * @param sourcedir
	 *            directory
	 * @return String[] with the filenames (with absolut path)
	 */
	/*----------------------------------------------------------------------------*/
	public static String[] listRecursiveFiles(String sourcedir) {
		// arrayList to store the filenames
		ArrayList allFiles = new ArrayList();

		if (sourcedir != null && sourcedir.length() > 0) {
			// fills the arrayList with the filenames
			getFilesRecursive(allFiles, sourcedir);
		}
		// store all filenames in a string array
		String[] allfiles = new String[allFiles.size()];
		for (int i = 0; i < allFiles.size(); i++) {
			allfiles[i] = allFiles.get(i).toString();
		}
		return allfiles;
	}

	/*                                                                            */
	/*----------------------------------------------------------------------------*/
	/**
	 * list recursiv all files of a directory
	 * 
	 * @param allFiles
	 *            ArrayList array with all filenames (with absolut path)
	 * @param dir
	 *            source Directory
	 */
	/*----------------------------------------------------------------------------*/
	private static void getFilesRecursive(ArrayList allFiles, String dir) {
		File f = new File(dir);
		File[] listFiles = f.listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			// subdirectory
			if (listFiles[i].isDirectory())
				getFilesRecursive(allFiles, listFiles[i].toString());
			else
				allFiles.add(listFiles[i].toString());
		}
	}

	/**
	 * Returns the files in a directory given by path (only files, not directories)
	 * @param path
	 * @return
	 */
	public static File[] getFiles( String path ) {
		
		List<File> aFiles = new ArrayList<File>();
		
		File dir = new File( path );
		
		if ( dir.exists() && dir.isDirectory() ) {
			File[] listFiles = dir.listFiles();
			
			for ( File file : listFiles ) {
				if ( file.isFile() ) {
					aFiles.add( file );
				}
			}
			File[] files = new File[ aFiles.size() ];
			files = aFiles.toArray( files );
			return files;
		}
	
		return null;
	}

	/**
	 * Write data of type Base64Binary in the file fileToWrite
	 * 
	 * @param fileToWrite
	 * @param base64Binary
	 * @throws IOException
	 */
	 

	public static void main(String[] args) {
		
		String froms = fromFilesystem("-E9078-E629E_20-E30D1-E30FC-E30C8-E30CA");
		System.out.println(froms);
		
		
		boolean bb=true;
		if(bb){
			return;
		}
		
		if (args.length >= 1 && args.length <= 2) {
			try {
				if (args.length == 2 && args[1].equalsIgnoreCase("from")) {
					String from = fromFilesystem(args[0]);
					System.out.println(from);
				} else {
					String to = toFilesystem(args[0]);
					System.out.println(to);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		} else {
			System.out.println("Usage: <text> [to|from]");
		}
	}
	
	public static void delay(long msec){
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			// ignore
		}
	}

	public static long getCollectiveFileSize(File[] files) {
		long fileSizeInBytes = 0L;
		
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
			    long fileSize = 0;
			    File currentFile = files[i];
			    if (files[i].isDirectory()) {
			        fileSize = getCollectiveFileSize(currentFile.listFiles());
			    } else {
			        fileSize = files[i].length();
			    }
				fileSizeInBytes = fileSizeInBytes + fileSize;
			}
		}
		
		return fileSizeInBytes;
	}
	
	public static String getFileSizeWithUnits(long fileSize) {
	    String[] sizeUnits = {"Kb", "Mb", "Gb", "Tb"};
        double convFileSize = 0.0;
        int convFactor = 0;
        if (fileSize > 1024) {
            convFileSize = fileSize / 1024.0;
            while (convFileSize > 1024 && convFactor < 3)  {
                convFileSize = convFileSize / 1024.0;
                convFactor++;
            }
        }
        
        DecimalFormat df = new DecimalFormat("#.##");
        
        String fileSizeWithUnits = df.format(convFileSize) + " " + sizeUnits[convFactor];
        
        return fileSizeWithUnits;
    }
	
	public static boolean recursivelyDeleteDirectory(File dir) {
	    boolean isDeleted = false;
	    if (dir != null && dir.exists()) {
	        if (dir.isDirectory()) {
	            File[] files = dir.listFiles();
	            if (files != null) {
	                for (File file : files) {
	                    if (file.isDirectory()) {
	                        recursivelyDeleteDirectory(file);
	                    } else {
	                        file.delete();
	                    }
	                }
	            }
	            isDeleted = dir.delete();
	        }
	    }
	    return isDeleted;
	}

	/*                                                                            */
	/*                                                                            */
	/* ============================================================================ */
	/*                                                                            */
}
/*                                                                            */
/* ============================================================================ */
/* ============================================================================ */

