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
 *=============================================================================
 *
 *  Project:    utils
 *  Language:   Java
 *  Filename:   $RCSfile: PDate.java,v $
 *  Author:     $Author: js $
 *  Date:       $Date: 2005/03/01 12:27:40 $
 *  Revision:   $Revision: 1.5 $
 *  State:      $State: Exp $
 *
 *============================================================================*/
/*                                                                            */
package com.procaess.common2.utils;
/*                                                                            */
import java.text.*;
import java.util.*;
/*                                                                            */
/*============================================================================*/
/**  Documentation: This class defines a simple date utility                  */
/*============================================================================*/
/*                                                                            */
public class PDate {
/*                                                                            */
  public static String defaultPDateFormat = "dd.MM.yyyy HH:mm:ss";
/*                                                                            */
/*============================================================================*/
/**  Returns the current date and time formatted for the default locale.
  *  @return current date and time in short format                            */
/*============================================================================*/
/*                                                                            */
  public static String getCurrentDateTime() {

    Date date = new Date();
    SimpleDateFormat sdf =  new SimpleDateFormat(defaultPDateFormat);
    return sdf.format(date);
  }
/*                                                                            */
/*============================================================================*/
/**  Returns the current date formatted for the default locale.
  *  @return current date and time in short format                            */
/*============================================================================*/
/*                                                                            */
  public static String getCurrentDate_DDMMYYYY() {

    Date date = new Date();
    SimpleDateFormat sdf =  new SimpleDateFormat("dd.MM.yyyy");
    return sdf.format(date);
  }
/*                                                                            */
/*============================================================================*/
/**  Returns the current date formatted for the default locale.
  *  @return current date and time in short format                            */
/*============================================================================*/
/*                                                                            */
  public static String getDateOfNextDay_DDMMYYYY() {
/*     Initialize the current day, month and year                             */
    GregorianCalendar calendar = new GregorianCalendar();
    int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);       
    Calendar currentDate = Calendar.getInstance();
    currentDate.set(year, month, day);
    Date date = currentDate.getTime();
    SimpleDateFormat sdf =  new SimpleDateFormat("dd.MM.yyyy");
    return sdf.format(date);
  }
/*                                                                            */
/*============================================================================*/
/**  Returns the current date and time formatted for the default locale.
  *  @return current date and time as long value                              */
/*============================================================================*/
/*                                                                            */
  public static long getCurrentDateTimeAsLong() {

    Date date = new Date();
    return date.getTime();
  }
/*                                                                            */
/*============================================================================*/
/**  Returns the current date and time.
  *  @return current date and time in format YYMMDDhhmmss                     */
/*============================================================================*/
/*                                                                            */
  public static String getDateTime_YYMMDDhhmmss() throws PJavaException {

    return getDate_YYMMDD() + getTime_HHMMSS();
  }
/*                                                                            */
/*============================================================================*/
/**  Returns the current date.
  *  @return current date in format YYMMDD                                    */
/*============================================================================*/
/*                                                                            */
  public static String getDate_YYMMDD() {

    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
    return sdf.format(date);
  }
/*                                                                            */
/*============================================================================*/
/**  Returns the current date.
  *  @return current date in format YDDD                                      */
/*============================================================================*/
/*                                                                            */
  public static String getDate_YDDD() {

    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yDDD");
    return sdf.format(date).substring(1);
  }
/*                                                                            */
/*============================================================================*/
/**  Returns the current date.
  *  @return current date in format MMDD                                      */
/*============================================================================*/
  public static String getDate_MMDD() {

    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
    return sdf.format(date);
  }
/*                                                                            */
/*============================================================================*/
/**  Returns the current date.
  *  @return current date in format YYYYMMDD                                  */
/*============================================================================*/
/*                                                                            */
  public static String getDate_YYYYMMDD() throws PJavaException {

    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    return sdf.format(date);
  }
/*============================================================================*/
/**  Returns the current date.
  *  @return current date in format YYYYMMDD                                  */
/*============================================================================*/
/*                                                                            */
  public static String getDate_DDMMYYYY() throws PJavaException {

    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    return sdf.format(date);
  }
/*                                                                            */
/*============================================================================*/
/**  Returns the current time.
  *  @return current time in format HHMMSS                                    */
/*============================================================================*/
/*                                                                            */
  public static String getTime_HHMMSS() throws PJavaException {

    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
    return sdf.format(date);
  }
/*                                                                            */
/*============================================================================*/
/**  Returns the current time.
  *  @return current time in format MMSS                                      */
/*============================================================================*/
/*                                                                            */
  public static String getTime_MMSS() throws PJavaException {

    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("mmss");
    return sdf.format(date);
  }
/*                                                                            */
/*============================================================================*/
/**  Returns the current time.
  *  @return current time in format HHMM                                      */
/*============================================================================*/
/*                                                                            */
  public static String getTime_HHMM() throws PJavaException {

    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
    return sdf.format(date);
  }
/*                                                                            */
/*============================================================================*/
/**  Returns the current time.
  *  @return current time in format HHMM                                      */
/*============================================================================*/
/*                                                                            */
  public static String getTime_HH_MM() throws PJavaException {

    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    return sdf.format(date);
  }
/*                                                                            */
/*============================================================================*/
/**  Returns the current date formatted for ENGDAT.
  *  @return current date in EngDat style                                     */
/*============================================================================*/
/*                                                                            */
  public static String getCurrentEngDate () throws PJavaException {
    return getDate_YYMMDD();
  }
/*                                                                            */
/*============================================================================*/
/**  Returns the current time formatted for ENGDAT                            */
/*============================================================================*/
/*                                                                            */
  public static String getCurrentEngTime ()throws PJavaException  {
    return getTime_HHMMSS();
  }
/*                                                                            */
/*============================================================================*/
/**  Returns a date string in PDate formatted style.
  *  @param dateString string of date
  *  @return date in given PDate style
  *  @exception PJavaException                                                */
/*============================================================================*/
/*                                                                            */
  public static String parsePDate(String dateString)
    throws PJavaException {

    Date date = new Date();
    SimpleDateFormat sdf =
      new SimpleDateFormat(defaultPDateFormat);
    try {
      date = sdf.parse(dateString);
    }
    catch (ParseException e) {
      throw new PJavaException(e.toString());
    }
    return sdf.format( date );
  }
/*                                                                            */
/*============================================================================*/
/**  Transforms a date into its milliseconds representation.
  *  @return date in long variable
  *  @exception PJavaException                                                */
/*============================================================================*/
/*                                                                            */
  public static long parsePDateToLong (String dateString)
    throws PJavaException {

    Date date = new Date();
    SimpleDateFormat sdf =
      new SimpleDateFormat(defaultPDateFormat);
    try {
      date = sdf.parse(dateString);
    }
    catch (ParseException e) {
      throw new PJavaException(e.toString());
    }
    return date.getTime();
  }
/*                                                                            */
/*============================================================================*/
/**  Transforms a date from its milliseconds representation.
  *  @return date in String variable                                          */
/*============================================================================*/
/*                                                                            */
  public static String parseLongToPDate (long dateAsLong) {

    Date date = new Date(dateAsLong);
    SimpleDateFormat sdf =
      new SimpleDateFormat(defaultPDateFormat);
    return sdf.format(date);
  }

 /*============================================================================*/
  /**  Returns the given date as formatted string.
    *  @param date date to format
    *  @return date in String variable                                          */
  /*============================================================================*/
  /*                                                                            */
    public static String formatDateToPDate (Date date) {
      SimpleDateFormat sdf =
        new SimpleDateFormat(defaultPDateFormat);
      return sdf.format(date);
    }/*                                                                            */
/*============================================================================*/
/*                                                                            */
}
/*                                                                            */
/*============================================================================*/

