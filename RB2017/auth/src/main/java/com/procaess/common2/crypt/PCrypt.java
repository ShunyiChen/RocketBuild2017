/*==============================================================================
 *=====                                                                    =====
 *====     PPPP                     CCCC    AAA     EEEE                    ====
 *===      P   P                   C       A   A   E                         ===
 *==       P   P                   C       A   A   E                          ==
 *=        PPPP    rrrrr    ooo    C       AAAAA   EEE      ssss    ssss       =
 *==       P        rr     o   o   C       A   A   E       sss     sss        ==
 *===      P        r      o   o   C       A   A   E         sss     sss     ===
 *====     P        r       ooo     CCCC   A   A    EEEE   ssss    ssss     ====
 *=====                                                                    =====
 *==============================================================================
 *        ProCAEss GmbH                   email: Info@procaess.com
 *        Klaus-von-Klitzing-Str. 3       phone: +49 6341/954-183
 *        76829 Landau                    fax:   +49 6341/954-184
 *==============================================================================
 *
 *  Project:    common2
 *  Language:   Java
 *  Filename:   $RCSfile: PCrypt.java,v $
 *  Author:     $Author: mt $
 *  Date:       $Date: 2009/07/27 13:41:41 $
 *  Revision:   $Revision: 1.9 $
 *  State:      $State: Exp $
 *
 *============================================================================*/
/*                                                                            */
/*    Package:                                                                */
/*    ========                                                                */
/*                                                                            */
package com.procaess.common2.crypt;
/*                                                                            */
/*============================================================================*/
/*                                                                            */
/*    Import:                                                                 */
/*    =======                                                                 */
/*                                                                            */
import java.io.*;
import java.lang.*;
import java.security.*;
import xjava.security.*;
import cryptix.provider.key.*;
import cryptix.provider.cipher.*;
import cryptix.provider.padding.*;
import cryptix.provider.rsa.*;
import com.procaess.common2.utils.*;
/*                                                                            */
/**   Encryption/Decryption class.
 *
 *    <p>
 *    Imports:
 *    <ul>
 *      <li> java.io.*
 *      <li> java.lang.*
 *      <li> java.security.*
 *      <li> com.procaess.common2.utils.*
 *    </ul>                                                                   */
/*                                                                            */
/*============================================================================*/
/*                                                                            */
/*    Class:                                                                  */
/*    ======                                                                  */
/*                                                                            */
/*                                                                            */
public class PCrypt {
/*============================================================================*/
/*                                                                            */
/*    - Private Members:                                                      */
/*      ================                                                      */
/*                                                                            */
/** Error handler.                                                            */
  private ErrorHandler  errorHandler  = null;
/*                                                                            */
/** idea key                                                                  */
  private String ideaKey  = "1234567890123456";
  private String ideaKey2 = "2npmA55W7coqWy52";
/*                                                                            */
/*                                                                            */
/*============================================================================*/
/*                                                                            */
/*    - Constructors:                                                         */
/*      =============                                                         */
/*                                                                            */
/*----------------------------------------------------------------------------*/
/** Constructor.
 *
 *  @param errorHandler error handler
 *                                                                            */
/*----------------------------------------------------------------------------*/
/*                                                                            */
  public PCrypt (
    ErrorHandler errorHandler
  ) {
    if (errorHandler != null) {
      this.errorHandler = errorHandler;
    } else {
      this.errorHandler = ErrorHandler.getInstance();
    }
  }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/** Encrypt a hex string.
 *
 *  @param chiffre hex string
 *  @return dechiffrierter String
 *  @throws PJavaException in case of error
 *                                                                            */
/*----------------------------------------------------------------------------*/
/*                                                                            */
  public String encode(
    String plain
  ) throws PJavaException {

    int    errorCode;
    String errorMessage;

    // IDEA
    IDEAKeyGenerator keygen = new IDEAKeyGenerator();
    Key              key;
    IDEA             encrypt = new IDEA();

    try {
      // Construct key for IDEA
      key = keygen.generateKey(ideaKey.getBytes());

      // Initialize encryption
      encrypt.initEncrypt(key);

      if (plain.length() == 0 ||
          plain.length() % encrypt.getInputBlockSize() > 0) {
        for (int currentPad = plain.length() % encrypt.getInputBlockSize();
             currentPad < encrypt.getInputBlockSize();
             ++currentPad) {
          plain += " ";
        }
      }

      // encrypt
      byte[] encrypted = encrypt.update(plain.getBytes());

      // return HEX of encrypted
      return cryptix.util.core.Hex.toString(encrypted);
    } catch (InvalidKeyException invalidKeyExc) {
      errorCode    = 10;
      errorMessage = "Invalid Key";
      errorHandler.logMessage( ErrorHandler.ERROR, errorCode, 0,
                               "PCrypt.encryptIdea", "",
                               errorMessage
                             );
      throw new PJavaException( errorMessage );
    } catch (WeakKeyException weakExc) {
      errorCode    = 11;
      errorMessage = "Weak Key";
      errorHandler.logMessage( ErrorHandler.ERROR, errorCode, 0,
                               "PCrypt.encryptIdea", "",
                               errorMessage
                             );
      throw new PJavaException( errorMessage );
    } catch (KeyException keyExc) {
      errorCode    = 12;
      errorMessage = "KeyException";
      errorHandler.logMessage( ErrorHandler.ERROR, errorCode, 0,
                               "PCrypt.encryptIdea", "",
                               errorMessage
                             );
      throw new PJavaException( errorMessage );
    }
  }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/** Decrypt a hex string.
 *
 *  @param chiffre hex string
 *  @return dechiffrierter String
 *  @throws PJavaException in case of error
 *                                                                            */
/*----------------------------------------------------------------------------*/
/*                                                                            */
  public String decode(
    String chiffre
  ) throws PJavaException {

    int    errorCode;
    String errorMessage;

    // IDEA
    IDEAKeyGenerator keygen = new IDEAKeyGenerator();
    Key              key;
    IDEA             decrypt = new IDEA();
    byte[] decrypted = null;

    try {
      // Construct key for IDEA
      key = keygen.generateKey(ideaKey.getBytes());

      // Initialize encryption
      decrypt.initDecrypt(key);

      // encrypt
      decrypted = decrypt.update(cryptix.util.core.Hex.fromString(chiffre));

      // return HEX of encrypted
      String str1 = new String(decrypted, "ISO_8859-1");
      //String str1 = new String(decrypted);
      String res = str1.trim();
      return res;
      //return (new String(decrypted, "ISO_8859-1")).trim();
    } catch (InvalidKeyException invalidKeyExc) {
      errorCode    = 20;
      errorMessage = "Invalid Key";
      errorHandler.logMessage( ErrorHandler.ERROR, errorCode, 0,
                               "PCrypt.decryptIdea", "",
                               errorMessage,
                               invalidKeyExc);
      throw new PJavaException( errorMessage );
    } catch (WeakKeyException weakExc) {
      errorCode    = 21;
      errorMessage = "Weak Key";
      errorHandler.logMessage( ErrorHandler.ERROR, errorCode, 0,
                               "PCrypt.decryptIdea", "",
                               errorMessage,
                               weakExc);
      throw new PJavaException( errorMessage );
    } catch (KeyException keyExc) {
      errorCode    = 22;
      errorMessage = "KeyException";
      errorHandler.logMessage( ErrorHandler.ERROR, errorCode, 0,
                               "PCrypt.decryptIdea", "",
                               errorMessage,
                               keyExc);
      throw new PJavaException( errorMessage );
    } catch (Exception ex) {
      errorCode    = 23;
      errorMessage = "Exception" + ex.toString();
      errorHandler.logMessage( ErrorHandler.ERROR, errorCode, 0,
	            				"PCrypt.decryptIdea", 
	            				"",
	            				errorMessage
	          				);
		
      throw new PJavaException( errorMessage );
    }
  }
  
/*                                                                            */
/*----------------------------------------------------------------------------*/
/** Methode to set Key
 *
 *  @key The key must have 16 chars, and be careful, the key can also be week, 
 *       which causes an Exception
 *                                                                            */
/*----------------------------------------------------------------------------*/
/*                                                                            */
  public void setKey(String key){
    this.ideaKey = key;
  }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/** Encrypt a hex string.
 *
 *  @param chiffre hex string
 *  @return dechiffrierter String
 *  @throws PJavaException in case of error
 *                                                                            */
/*----------------------------------------------------------------------------*/
/*                                                                            */
  public String encode2(
    String plain
  ) throws PJavaException {
    setKey(ideaKey2);
    return encode(plain);
  }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/** Decrypt a hex string.
 *
 *  @param chiffre hex string
 *  @return dechiffrierter String
 *  @throws PJavaException in case of error
 *                                                                            */
/*----------------------------------------------------------------------------*/
/*                                                                            */
  public String decode2(
    String chiffre
  ) throws PJavaException {
    setKey(ideaKey2);
    return decode(chiffre);
  }
    


  public static void main(String[] args)
  {
    if ( args.length >= 1 && args.length <= 2 ) { 
      try {
        PCrypt chiffre = new PCrypt(ErrorHandler.getInstance());
        if ( args.length == 2 && args[1].equalsIgnoreCase("decode") ) {
          String decrypted = chiffre.decode(args[0]);
          System.out.println(decrypted);
        } else {
          String encrypted = chiffre.encode(args[0]);
          System.out.println(encrypted);
        }
      } catch ( Throwable t ) {
        t.printStackTrace();
      }
    } else {
      System.out.println("Usage: <text> [encode|decode]");
    }
  }

}
