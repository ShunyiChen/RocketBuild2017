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
//  Project:    ErrorHandler
//  Source:     ErrorHandler.java
//  Language:   Java
//  Author:     $Author: rb $
//  Date:       $Date: 2007/05/03 11:37:47 $
//  Revision:   $Revision: 1.8 $
//  State:      $State: Exp $
/*                                                                            */
/*============================================================================*/
/*                                                                            */
/*    Package:                                                                */ 
/*    ========                                                                */ 
/*                                                                            */
      package com.procaess.common2.utils;
/*                                                                            */
/*============================================================================*/
/*                                                                            */
/*    Import:                                                                 */ 
/*    =======                                                                 */ 
/*                                                                            */
      import java.io.*;
import java.util.*;
/*                                                                            */
/**   Common error handler for all ProCAEss products.
 *
 *    <p>
 *    Imports:
 *    <ul>
 *      <li> java.io.*
 *      <li> java.util.*
 *    </ul>
 *
 *    ErrorHandler implements a class containing a synchronized method
 *    <STRONG>logMessage(...)</STRONG><P>
 *    to generate an error message. The error logfile's handle is static and so
 *    created only once. Thus the ErrorHandler class cannot be instantiated by a
 *    constructor. One must create a local ErrorHandler instance by call the
 *    <STRONG>getInstance()</STRONG> method.
 *
 *    If the <STRONG>close()</STRONG> method was called the next
 *    <STRONG>getInstance()</STRONG> call creates an empty error log file.
 *    <H2>Program exits after storing a fatal error message.</H2><P>
 *
 *    Properties for ErrorHandler class can be set at program call with
 *    <STRONG>-D</STRONG> option.<P>
 *    <UL>
 *      <LI>DEBUGLEVEL - debug level, default = WARNING
 *      <LI>ERRORLOG - name and path of error log file, default = error.log
 *    </UL>
 *    <STRONG>Debug levels:</STRONG>
 *    <UL>
 *      <LI> -1 = ALWAYS: print dispite of current level
 *      <LI> 0 = INFORMATION: print all messages
 *      <LI> 1 = WARNING: warnings an more severe messages
 *      <LI> 2 = ERROR: errors an more severe messages
 *      <LI> 3 = FATAL ERROR: print only fatal error messages
 *    </UL>
 *
 *    <STRONG>Example for creating an error log message:</STRONG>
 *    <PRE>
 *     ErrorHandler errorHandler;
 *     errorHandler = ErrorHandler.getInstance();
 *     errorHandler.logMessage( errorHandler.WARNING, 123, 456,
 *                              "Tester", "Handler",
 *                              "Test 1 2 3." );
 *    </PRE>
 *
 *    <STRONG>Example for closing an error log file:</STRONG>
 *    <PRE>
 *     ErrorHandler.close();
 *    </PRE>                                                                 **/
/*                                                                            */
/*============================================================================*/
/*                                                                            */
/*    Class: ErrorHandler                                                     */
/*    ======                                                                  */
/*                                                                            */
      public class ErrorHandler {
/*                                                                            */
/*============================================================================*/
/*                                                                            */
/*    - Public Members:                                                       */
/*      ===============                                                       */
/*                                                                            */
        public static final int INFORMATION   = 0;
        public static final int WARNING       = 1;
        public static final int ERROR         = 3;
        public static final int FATAL_ERROR   = 4;
        public static final int ALWAYS        = -1;
/*                                                                            */
        protected static String           errorLogFilename  = null;
/*                                                                            */
/*============================================================================*/
/*                                                                            */
/*    - Protected Members:                                                    */
/*      ==================                                                    */
/*                                                                            */
/**   - default debug level, can be changed by property "DEBUGLEVEL"
          0 = INFORMATION, displays all messages
          1 = WARNING,     displays warnings and more severe messages
          2 = ERROR,       displays errors and more severe messages
          3 = FATAL ERROR, displays only fatal errors and exits after message
                                                                              */
/*                                                                            */
        protected static final int        defaultDebugLevel = 2;
        /** Default maximum log file size in kilobytes(KB) */
        protected static final int        defaultMaxLogFileLength  = 10240;
        protected static int			  maxLogFileLength	= -1;
/*                                                                            */
        protected static final String     defaultErrorLog   = "error.log";
/*                                                                            */
        protected static int              debugLevel        = -1;
/*                                                                            */
        protected static FileOutputStream errorLogStream    = null;
        protected static PrintWriter      errorLogWriter    = null;
/*                                                                            */
        protected static boolean          appendMode        = false;
/*                                                                            */
        protected static final int 		  defaultRollingFilesNumber = 5;
        protected static int          	  rollingFilesNumber= -1;
/*                                                                            */
        protected static ErrorHandler     errorHandler      = null;
/*                                                                            */
/*============================================================================*/
/*                                                                            */
/*    - Constructors:                                                         */
/*      =============                                                         */
/*                                                                            */
/*----------------------------------------------------------------------------*/
/**     ErrorHandler Default Contructor.                                     **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
        public ErrorHandler() {
/*                                                                            */
          InitErrorHandler((Properties) null);
        }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/**     ErrorHandler Contructor with appendMode.                             **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
        public ErrorHandler(boolean anAppendMode) {
/*                                                                            */
          appendMode = anAppendMode;   
/*                                                                            */
          InitErrorHandler((Properties) null);
          errorHandler=this;
        }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/**     ErrorHandler Contructor with appendMode.                             **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
        public ErrorHandler(boolean anAppendMode, Properties properties) {
/*                                                                            */
          appendMode = anAppendMode;   
/*                                                                            */
          InitErrorHandler(properties);
          errorHandler=this;
        }        
/*                                                                            */
/*----------------------------------------------------------------------------*/
/**     ErrorHandler Contructor with given properties.                       **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
        public ErrorHandler(Properties properties) {
          InitErrorHandler (properties);
          errorHandler=this;
        }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/**     ErrorHandler Contructor with given properties.                       **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
        public ErrorHandler(int i) {
        }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/**     ErrorHandler Initialization                                          **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
        protected void InitErrorHandler(Properties properties) {
/*                                                                            */
          String propertyDebugLevel;
          String propertyMaxLogFileLength;
          String propertyRollingLogFilesNumber;
/*                                                                            */
/*        Read Properties                                                     */
/*                                                                            */
          propertyDebugLevel = System.getProperty( "DEBUGLEVEL",
                                                   Integer.toString(defaultDebugLevel));
          errorLogFilename   = System.getProperty( "ERRORLOG",
                                                   defaultErrorLog );
          propertyMaxLogFileLength = System.getProperty("MAX_LOG_FILE_LENGTH",
        		  										Integer.toString(defaultMaxLogFileLength));
          propertyRollingLogFilesNumber = System.getProperty("ROLLING_LOG_FILES",
        		  										     Integer.toString(defaultRollingFilesNumber));
          if (properties != null) {
            propertyDebugLevel = properties.getProperty( "DEBUGLEVEL",
                                                         propertyDebugLevel).trim();
            errorLogFilename   = properties.getProperty( "ERRORLOG",
                                                         errorLogFilename).trim();
            propertyMaxLogFileLength = properties.getProperty("MAX_LOG_FILE_LENGTH",
            												  propertyMaxLogFileLength).trim();
            propertyRollingLogFilesNumber = properties.getProperty("ROLLING_LOG_FILES",
            													   propertyRollingLogFilesNumber).trim();
          }
/*                                                                            */
/*        Set Debug Level                                                     */
/*                                                                            */
          try {
            debugLevel = Integer.parseInt(propertyDebugLevel);
          } catch (NumberFormatException e) {
            debugLevel = defaultDebugLevel;
          }
/*                                                                            */
/*        Set maximum log file length                                         */
/*                                                                            */
		  try {
			/* Converting kilobytes(KB) value to bytes(B) value */
			maxLogFileLength = Integer.parseInt(propertyMaxLogFileLength) * 1024;
		  } catch (NumberFormatException e) {
			maxLogFileLength = defaultMaxLogFileLength * 1024;
		  }
/*                                                                            */
/*        Set rolling log files number                                        */
/*                                                                            */
		  try {
      		rollingFilesNumber = Integer.parseInt(propertyRollingLogFilesNumber);
      	  } catch (NumberFormatException e) {
      		rollingFilesNumber = defaultRollingFilesNumber;
      	  }
/*                                                                            */
/*        Roll the old log files                                              */
/*                                                                            */
		  rollLogFiles();
/*                                                                            */
/*        Set and open LogFile                                                */
/*                                                                            */
          try {
            openErrorLogFile(errorLogFilename, appendMode, true);
          } catch ( IOException filenameError ) {
            try {
              System.err.println( "WARNING: Could not open log file: " + errorLogFilename + ". " +
                                  "Using default file: " + defaultErrorLog );
              errorLogFilename = defaultErrorLog;
              openErrorLogFile(errorLogFilename, false, true);
            } catch ( IOException defaultFilenameError ) {
              System.err.println( "ERROR: Could not open default log file: "
                                 + errorLogFilename );
            }
          }
/*                                                                            */
          
          System.out.println("LOGGIN: " + translateDebugLevel(debugLevel) + " " + new File(errorLogFilename).getAbsolutePath());
          if (debugLevel == INFORMATION && appendMode == false) {
        	  ErrorHandler.println("Error logging started at " + PDate.getCurrentDateTime());
          }
          
        }
/*                                                                            */
/*============================================================================*/
/*                                                                            */
/*    - Finalizer:                                                            */
/*      ==========                                                            */
/*                                                                            */
        protected void finalize() throws Throwable {
/*                                                                            */
          /*
          if ( errorHandler != null ) {
            errorHandler.close();
            errorHandler = null;
          }
          errorLogStream = null;
          errorLogWriter = null;
          */
          try {
            super.finalize();
          } catch (Throwable e) {
            throw new Throwable( "ErrorHandler Finalizer error: " + e );
          }
        }
/*                                                                            */
/*============================================================================*/
/*                                                                            */
/*    - Public Methods:                                                       */
/*      ===============                                                       */
/*                                                                            */
/*----------------------------------------------------------------------------*/
/** Change debug level
 *
 * @param newDebugLevel new debug level
 *                                                                           **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
  public void changeDebugLevel(
    int newDebugLevel
  ) {
    if (errorHandler != null) {
      println(PDate.getCurrentDateTime() + " DebugLevel has been changed from " + translateDebugLevel(debugLevel) + " to " + translateDebugLevel(newDebugLevel) );
      if ((newDebugLevel >= 0) &&
          (newDebugLevel <= 3)) {
        debugLevel = newDebugLevel;
      }
      
    } else {
      System.out.println("ErrorHandler: Kein ErrorHandler vorhanden.");
    }
  }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/** Get name for log level value.
 *
 *  @param logLevel log level
 *                                                                           **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
  private static String translateDebugLevel(
    int logLevel
  ) {
    switch (logLevel) {
      case INFORMATION:
        return("INFORMATION");
      case WARNING:
        return("WARNING");
      case ERROR:
        return("ERROR");
      case FATAL_ERROR:
        return("FATAL ERROR");
      default:
        return("Wrong DebugLevel");
    }
  }
  
  /*                                                                            */
  /*----------------------------------------------------------------------------*/
  /**     Print a error message to the log file.
   *
   *      @param severity severity of error (INFORMATION, WARNING, ERROR,
   *             FATAL ERROR)
   *      @param parentErrorCode error code of parent
   *      @param childErrorCode error code of child
   *      @param parent name of parent method
   *      @param child name of child method or command
   *      @param message error message                                         **/
  /*----------------------------------------------------------------------------*/
  /*                                                                            */
    public synchronized void logMessage(int    severity,
  									  int    parentErrorCode,
  									  int    childErrorCode,
  									  String parent,
  									  String child,
  									  String message) {
  	  
  	  logMessage(severity,
                	parentErrorCode,
                	childErrorCode,
                	parent,
                	child,
                	message,
                	null);
    }
  
/*                                                                            */
/*----------------------------------------------------------------------------*/
/**     Print a error message to the log file.
 *
 *      @param severity severity of error (INFORMATION, WARNING, ERROR,
 *             FATAL ERROR)
 *      @param parentErrorCode error code of parent
 *      @param childErrorCode error code of child
 *      @param parent name of parent method
 *      @param child name of child method or command
 *      @param message error message
 *      @param t - the throwable that was raised. Stacktrace will be 
 *      		printed also 												**/
/*----------------------------------------------------------------------------*/
/*                                                                            */
        public synchronized void logMessage(int    severity,
                                            int    parentErrorCode,
                                            int    childErrorCode,
                                            String parent,
                                            String child,
                                            String message,
                                            Throwable t) {
/*                                                                            */
/*----------------------------------------------------------------------------*/
/*      - Do not create an error message if severity under debug level        */
/*----------------------------------------------------------------------------*/
/*                                                                            */
          if ( severity != ALWAYS &&
               severity < debugLevel ) return;
/*                                                                            */
/*----------------------------------------------------------------------------*/
/*      - If ErrorHandler is not initialized, write into standard output      */
/*----------------------------------------------------------------------------*/
/*                                                                            */
          if ( errorLogStream == null ||
               errorLogWriter == null ) {
            System.out.println("ErrorHandler: " + message);
            return;
          }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/*      - Check for length of log-File                                        */
/*----------------------------------------------------------------------------*/
/*                                                                            */
          checkLogfileLength();
/*                                                                            */
/*----------------------------------------------------------------------------*/
/*      - Print severity of error message                                     */
/*----------------------------------------------------------------------------*/
/*                                                                            */
          switch ( severity ) {
            case ALWAYS :
            	ErrorHandler.print("(A) ");
            	break;
            case INFORMATION :
            	ErrorHandler.print("(I) ");
            	break;
            case WARNING :
            	ErrorHandler.print("(W) ");
            	break;
            case ERROR :
            	if (t == null) {
            		t = new Throwable().fillInStackTrace();
            	}
            	ErrorHandler.print("(E) ");
            	break;
            case FATAL_ERROR :
            	if (t == null) {
            		t = new Throwable().fillInStackTrace();
            	}
            	ErrorHandler.print("(F) ");
            	break;
          }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/*      - Print error message                                                 */
/*----------------------------------------------------------------------------*/
/*                                                                            */
          ErrorHandler.println( PDate.getCurrentDateTime() + " " +
                        parent + "\t (error " + parentErrorCode + ") called " +
                        child +  "\t (error " + childErrorCode + ") - " +
                        message );
          
          if (t != null) {
        	  t.printStackTrace(errorLogWriter);
          }
        }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/**     Return the debug level.                                              **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
        protected static void checkLogfileLength() {
          errorLogWriter.flush();
          File logFile = new File(errorLogFilename);
          if (logFile.length() > maxLogFileLength) {
            cleanUpLogFile(true);
          }
        }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/**     Return the debug level.                                              **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
        public static int getDebugLevel() {
          return debugLevel;
        }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/**     Print a single line to the error log file.
 *
 *      @param line (text to print to error log file)                        **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
        protected static void println( String line ) {
          errorLogWriter.println(line);
          errorLogWriter.flush();
        }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/**     Print a single line to the error log file.
 *
 *      @param line (text to print to error log file)                        **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
        protected static void print( String line ) {
          errorLogWriter.print(line);
          errorLogWriter.flush();
        }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/**     Open error log file for writing.
 *
 *      @param filename filename (with path) for log file
 *      @param appendMode open file in append mode if true
 *      @param autoFlush automatic flush after println
 *      @exception IOException when open of file failed                      **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
        protected void openErrorLogFile( String filename,
                                         boolean appendMode,
                                         boolean autoFlush)
          throws IOException {
/*                                                                            */
          try {
            errorLogStream = new FileOutputStream (filename, appendMode);
            errorLogWriter = new PrintWriter(new OutputStreamWriter(errorLogStream, "UTF-8"), autoFlush);
/*                                                                            */
          } catch ( IOException e ) {
            throw e;
          }
        }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/**     Close error log file.                                                **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
        public synchronized static void close() {
/*                                                                            */
          if (debugLevel == 0) {
            println("Error logging finished at " + PDate.getCurrentDateTime());
          }
          try {
            errorLogWriter.close();
            errorLogStream.close();
/*                                                                            */
          } catch ( IOException e ) {
            System.out.println("ERROR: Could not close log file.");
          }
          errorHandler = null;
        }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/**     Create an instance of the error handler.
 *
 *      @return ErrorHandler instance
 *      @return null if an error occurred                                    **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
        public static ErrorHandler getInstance() {
/*                                                                            */
          if ( errorHandler == null ) {
            errorHandler = new ErrorHandler();
          }
          return errorHandler;
        }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/**     Cleans up, i.e. removes current log file.                            **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
        public synchronized static void cleanUpLogFile() {
        	cleanUpLogFile(false);
        }
        
        public synchronized static void cleanUpLogFile(boolean withRolling) {
/*                                                                            */
          if ( errorHandler != null ) {
            try {
              errorLogWriter.close();
              errorLogStream.close();
              if (withRolling) {
            	  rollLogFiles();
              } else {
            	  dleteOldLogFiles();
              }
              errorHandler.openErrorLogFile(errorLogFilename, false, true);
              println("(I) " + PDate.getCurrentDateTime() + " Logfile has been truncated");
/*                                                                            */
            } catch ( IOException ex ) {
              System.err.println( "ERROR: Could not clean log file. " +
                                  ex );
            }
          }
/*                                                                            */
        }
        
		protected static void rollLogFiles() {
			for (int i = rollingFilesNumber - 1; i >= 1; i--) {
				String newFileName = errorLogFilename + "." + i;
				File newFile = new File(newFileName);
				newFile.delete();
				String fileToBeMovedName = errorLogFilename;
				if (i > 1) {
					fileToBeMovedName = fileToBeMovedName + "." + (i - 1);
				}
				File fileToBeMoved = new File(fileToBeMovedName);
				if (fileToBeMoved.exists()) {
					fileToBeMoved.renameTo(newFile);
				}
			}
		}
        
		/**
		 * Removes all old log files with names ending with '.1', '.2' etc.
		 */
		protected static void dleteOldLogFiles() {
			for (int i = rollingFilesNumber; i >= 1; i--) {
				String logFileName = errorLogFilename + "." + i;
				File fileToDelete = new File(logFileName);
				fileToDelete.delete();
			}
		}
/*                                                                            */
/*----------------------------------------------------------------------------*/
/**     Create an instance of the error handler.
 *
 *      @param properties properties to use
 *      @return ErrorHandler instance
 *      @return null if an error occurred                                    **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
        public static ErrorHandler getInstance(Properties properties) {

          if (errorHandler == null) {
            errorHandler = new ErrorHandler(properties);
          }
          return errorHandler;
        }
/*                                                                            */
/*----------------------------------------------------------------------------*/
/** Return name of current log-file.
 *
 *  @return name of current log-file
 *                                                                           **/
/*----------------------------------------------------------------------------*/
/*                                                                            */
  public String getLogfileName(
  ) {
    return errorLogFilename;
  }
/*                                                                            */
/*============================================================================*/
/*                                                                            */
      }
