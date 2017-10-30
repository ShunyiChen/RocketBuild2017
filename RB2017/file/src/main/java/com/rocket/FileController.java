package com.rocket;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rocket.tpa.model.tpackage.TpaPackage;

@RestController
public class FileController {

	private Logger logger ;
//	private TpaContext context;
	@Autowired
    private JdbcTemplate jdbcTemplate;
	private final static String tempDir = "C:/Demo/Files/";
	private String ddxDatabaseOwner = "ddxadmin";
	
	public FileController() {
//		System.out.println("=========== Load <tpa-sdk-config.win.xml> =============");
		logger = Logger.getLogger(getClass().getName());
		// Setup TPA SDK
//    	context = new TpaContext(getClass().getResourceAsStream("/tpa-sdk-config.dev.xml"));
		File tempFolder = new File(tempDir);
		if(!tempFolder.exists()) {
			tempFolder.mkdirs();
		}
	}
	
	@RequestMapping(value="/send",method = RequestMethod.POST)
	@ResponseBody
    public Response send(@RequestParam("file") MultipartFile file) {
		String randomId = UUID.randomUUID().toString();
		if (!file.isEmpty()) {
			String name = file.getOriginalFilename();
            try {
                byte[] bytes = file.getBytes();
                new File(tempDir+""+randomId).mkdirs();
                BufferedOutputStream stream = 
                        new BufferedOutputStream(new FileOutputStream(tempDir+""+randomId+"/"+name));
                stream.write(bytes);
                stream.close();
                
                logger.info("You successfully uploaded " + name + " into " +tempDir+""+randomId+"!");
                Response res = new Response(HttpStatus.OK);
                res.setRandomId(randomId);
                return res;
            } catch (Exception e) {
            	logger.info("You failed to upload " + name + " => " + e.getMessage());
            	Response res = new Response(HttpStatus.FAILED_DEPENDENCY);
            	return res;
            }
        } else {
        	Response res = new Response(HttpStatus.BAD_REQUEST);
            logger.info("You failed to upload, because the file was empty.");
            return res;
        }
		
    }
	
	@RequestMapping(value="/submit",method = RequestMethod.POST)
	@ResponseBody
    public Response submit(@RequestBody Request request) {
		String randomId = request.getRandomId();
		logger.info("================================================submit:randomId="+randomId);
		File p = new File(tempDir+""+randomId);
		if (p.exists()) {
			
			// exist check
			File[] files = p.listFiles();
			String[] fileNames = new String[files.length];
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					fileNames[i] = files[i].getPath();
				}
				Response res = new Response(HttpStatus.OK);
				res.setRandomId(randomId);
				
				Callback cb = new Callback() {

					@Override
					public void call(TpaPackage pack) {
						// Set refnumber
						res.setRefNumber(pack.TransactionRefNumber);
						// TODO Auto-generated method stub
						DBinsertForSending(request, pack.PackageID, pack.TransactionRefNumber);
						logger.info("DB inserted("+pack.PackageID+", "+pack.TransactionRefNumber+").");
					}
				};
				
				TestOnDev t = new TestOnDev();
				t.setUp();
				t.setUploadFiles(fileNames);
				t.upload(cb);
				logger.info("TPA Uploading file.");
				logger.info("Sent response to client.");
				
				return res;
			} else {
				
				logger.info("File list is empty.");
				Response res = new Response(HttpStatus.FAILED_DEPENDENCY);
				return res;
			}
		} else {
			logger.info("The random ID doesn't exist.");
			Response res = new Response(HttpStatus.FAILED_DEPENDENCY);
			return res;
		}
    }
	
	private void DBinsertForSending(Request request, String packId, String refNumber) {
		String sql1 = "SELECT MAX(ID) AS MAXID FROM "+ddxDatabaseOwner+".JOB";
		List<Map<String, Object>> max = jdbcTemplate.queryForList(sql1);
		int jobId = 1;
		if (max.size() > 0 && max.get(0).get("MAXID") != null) {
			jobId = Integer.parseInt(max.get(0).get("MAXID").toString());
			jobId = jobId + 1;
		}
		int userId = request.getUserId();
		String d1 = request.getDescription_1();
		String d2 = request.getDescription_2();
		String d3 = request.getDescription_3();
		String d4 = request.getDescription_4();
		String d5 = request.getDescription_5();
		String dt = System.currentTimeMillis()+"";
		dt = dt.substring(0, dt.length() -3);
		int datetime = Integer.parseInt(dt);
		String sql2 = "INSERT INTO ddxadmin.job" + 
				"	(id, id_user, id_server, multi_job, pdm_xml_job" + 
				"	, engdat_abstract, job_type, job_date, job_nr, description_5" + 
				"	, number_src, number_dst, datasize, status, autosend" + 
				"	, hidden, released, archived, last_access, temporary_job" + 
				"	, delete_time, ddx_version, deleted, job_forward, pdm_teamcenter_job" + 
				"	, is_ti, sub_directory_job_num, last_send)" + 
				"VALUES ("+jobId+", "+userId+", 1, 1, 1" + 
				"	, 1, 1, "+datetime+", 1, '"+d5+"'" + 
				"	, 1, 1, 860, 2, 2" + 
				"	, 1, 1, 1, "+datetime+", 1" + 
				"	, 1, 1, 1, 1, 1" + 
				"	, 0, 0, "+datetime+")" + 
				"";
		logger.info("SQL="+sql2);
		jdbcTemplate.execute(sql2);
		
		
		String sql3 = "INSERT INTO DDXADMIN.JOB_CONTACT" + 
				"	(ID, ID_JOB, ID_RECEIVER, RECEIVER_TYPE, MEDIUM_TYPE" + 
				"	, ENGDAT_ABSTRACT, ENGDAT_CONFIRM, DELETED, FIRSTNAME, FAMILYNAME, DEPARTMENT, PHONE,FAX, EMAIL,ID_MEDIUM)" + 
				"VALUES ("+jobId+", "+jobId+", 4, 1,10, 1, 1, 1, 'Vladtest', 'Test','_2D','123456789','_22','Dummy_40Dummy.com1', 1)";
		logger.info("SQL3="+sql3);
		jdbcTemplate.execute(sql3);
		
		
		String sql4 = "INSERT INTO DDXADMIN.JOB_TRUCORE" + 
				"	(ID, ID_JOB, TRANS_ID, TRANS_REF, STATUS_TEXT" + 
				"	, DELETED, STATUS_TRANS, STATUS_SENDER)" + 
				"VALUES ("+jobId+", "+jobId+", '"+packId+"', '"+refNumber+"', 'Delivered' , 1, 1, 1)";
		logger.info("SQL4="+sql4);
		jdbcTemplate.execute(sql4);
		
		
		String sql5 = "INSERT INTO DDXADMIN.JOB_ACCOUNTING" + 
				"	(ID, ID_JOB, TOTAL_MODELS, TOTAL_SIZE, TOTAL_SENT" + 
				"	, INITIALIZED, DELETED)" + 
				"VALUES ("+jobId+", "+jobId+", 2, 852, 1" + 
				"	, 2, 1)";
		logger.info("SQL5="+sql5);
		jdbcTemplate.execute(sql5);
		
		
		String sql6 = "INSERT INTO DDXADMIN.JOB_RESPONSE" + 
				"	(ID, ID_JOB, CREATION, START_FIRST, FINISH_FIRST" + 
				"	, RESTART, DELETED)" + 
				"VALUES ("+jobId+", "+jobId+", 1508859887, 1508859888, 1508859892" + 
				"	, 0, 1)";
		logger.info("SQL6="+sql6);
		jdbcTemplate.execute(sql6);
		
		String fileName = request.getFilename();
		String filesuffix = request.getFilesuffix();
		int filesize = request.getFilesize();
		
		String sql7 = "INSERT INTO DDXADMIN.JOB_MODEL" + 
				"	(ID, ID_JOB, ID_SKELETON, ID_PROJECT_0, ID_JOB_CONTACT" + 
				"	, ID_SOURCE_FORMAT, FKEY_SOURCE_FORMAT, NAME_SOURCE_FORMAT, MODEL_COUNT, SERIAL_COUNT" + 
				"	, SOURCE_PATH, SOURCE_NAME, DATA_ACCESS_MODE, ARCHIVE_NAME, SEND_NAME" + 
				"	, VIRTUAL_NAME, DISPLAY_NAME, TRACE, CONVERTED, STATUS" + 
				"	, PARENT_MODEL, DATASIZE, DOCUMENT_ID, DELETED, ID_USER_PROFILE" + 
				"	, PDM_TEAMCENTER_MODEL)" + 
				"VALUES ("+jobId+", "+jobId+", 1, 1, "+jobId+"" + 
				"	, 39, 4003, '"+filesuffix+"', 1, 1" + 
				"	, 'job_5C000000000006_5Csrc', '"+fileName+"', 0, '"+fileName+"', '"+fileName+"'" + 
				"	, 'ENG1710250336161D3BC002001', '"+fileName+"', 1, 2, 2" + 
				"	, 1, "+filesize+", '"+packId+"', 1, 1" + 
				"	, 1)";
		logger.info("SQL7="+sql7);
		jdbcTemplate.execute(sql7);
	}
	
	@RequestMapping(value="/receive",method = RequestMethod.POST)
	@ResponseBody
    public Response receive(@RequestBody Request request) {
		String refNumber = request.getRefNumber();
		if (refNumber == null) {
			return new Response(HttpStatus.BAD_REQUEST);
		}
		Response res = new Response(HttpStatus.OK);
		/// ===================================== time1
		final TestOnDev2 t = new TestOnDev2();
		t.setUp();
		Callback callback = new Callback() {
			@Override
			public void call(TpaPackage tpa) {
				String[] fileNames = new String[tpa.Payloads.size()];
				long[] fileSizes = new long[fileNames.length];
				for (int i = 0; i < tpa.Payloads.size(); i++) {
					fileNames[i] = tpa.Payloads.get(i).FileName;
					fileSizes[i] = new File(t.getTargetDir()+""+fileNames[i]).length();
				}
				res.setFileNames(fileNames);
				res.setFileSize(fileSizes);
			}
		};
		t.download(refNumber, callback);
		
		return res;
	}
	
	@RequestMapping(value="/receive2",method = RequestMethod.POST)
	@ResponseBody
    public Response receive2(@RequestBody Request request) {
		String refNumber = request.getRefNumber();
		if (refNumber == null) {
			return new Response(HttpStatus.BAD_REQUEST);
		}
		Response res = new Response(HttpStatus.OK);
		/// ===================================== time1
		final TestOnDev t = new TestOnDev();
		t.setUp();
		Callback callback = new Callback() {
			@Override
			public void call(TpaPackage tpa) {
				String[] fileNames = new String[tpa.Payloads.size()];
				long[] fileSizes = new long[fileNames.length];
				for (int i = 0; i < tpa.Payloads.size(); i++) {
					fileNames[i] = tpa.Payloads.get(i).FileName;
					fileSizes[i] = new File(t.getTargetDir()+""+fileNames[i]).length();
				}
				res.setFileNames(fileNames);
				res.setFileSize(fileSizes);
			}
		};
		t.download(refNumber, callback);
		
		return res;
	}
	
	@RequestMapping(value = "/files", method = RequestMethod.POST)
    public ResponseEntity<byte[]> downloadFile(@RequestBody Request request) throws IOException {
        String filename = "C:\\Rocket\\Trubiquity\\Truexchange\\tmp\\download\\"+request.getFilename();//"D:/tmp/" + name + ".jpg";
        logger.info("==================Downloading file filename="+filename+"==================");
        InputStream inputImage = new FileInputStream(filename);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        int l = inputImage.read(buffer);
        while(l >= 0) {
            outputStream.write(buffer, 0, l);
            l = inputImage.read(buffer);
        }
        inputImage.close();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", getContentType(filename.substring(filename.lastIndexOf(".")+1)));
        headers.set("Content-Disposition", "attachment; filename=\"" + request.getFilename() + "\"");
        return new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }
    
    private String getContentType(String suffix) {
    	 String [][]  MIME_MapTable={

    	        //{后缀名，MIME类型}
    	        {".3gp",    "video/3gpp"},
    	        {".apk",    "application/vnd.android.package-archive"},
    	        {".asf",    "video/x-ms-asf"},
    	        {".avi",    "video/x-msvideo"},
    	        {".bin",    "application/octet-stream"},
    	        {".bmp",    "image/bmp"},
    	        {".c",  "text/plain"},
    	        {".class",  "application/octet-stream"},
    	        {".conf",   "text/plain"},
    	        {".cpp",    "text/plain"},
    	        {".doc",    "application/msword"},
    	        {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
    	        {".xls",    "application/vnd.ms-excel"},
    	        {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
    	        {".exe",    "application/octet-stream"},
    	        {".gif",    "image/gif"},
    	        {".gtar",   "application/x-gtar"},
    	        {".gz", "application/x-gzip"},
    	        {".h",  "text/plain"},
    	        {".htm",    "text/html"},
    	        {".html",   "text/html"},
    	        {".jar",    "application/java-archive"},
    	        {".java",   "text/plain"},
    	        {".jpeg",   "image/jpeg"},
    	        {".jpg",    "image/jpeg"},
    	        {".js", "application/x-javascript"},
    	        {".log",    "text/plain"},
    	        {".m3u",    "audio/x-mpegurl"},
    	        {".m4a",    "audio/mp4a-latm"},
    	        {".m4b",    "audio/mp4a-latm"},
    	        {".m4p",    "audio/mp4a-latm"},
    	        {".m4u",    "video/vnd.mpegurl"},
    	        {".m4v",    "video/x-m4v"},
    	        {".mov",    "video/quicktime"},
    	        {".mp2",    "audio/x-mpeg"},
    	        {".mp3",    "audio/x-mpeg"},
    	        {".mp4",    "video/mp4"},
    	        {".mpc",    "application/vnd.mpohun.certificate"},
    	        {".mpe",    "video/mpeg"},
    	        {".mpeg",   "video/mpeg"},
    	        {".mpg",    "video/mpeg"},
    	        {".mpg4",   "video/mp4"},
    	        {".mpga",   "audio/mpeg"},
    	        {".msg",    "application/vnd.ms-outlook"},
    	        {".ogg",    "audio/ogg"},
    	        {".pdf",    "application/pdf"},
    	        {".png",    "image/png"},
    	        {".pps",    "application/vnd.ms-powerpoint"},
    	        {".ppt",    "application/vnd.ms-powerpoint"},
    	        {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
    	        {".prop",   "text/plain"},
    	        {".rc", "text/plain"},
    	        {".rmvb",   "audio/x-pn-realaudio"},
    	        {".rtf",    "application/rtf"},
    	        {".sh", "text/plain"},
    	        {".tar",    "application/x-tar"},
    	        {".tgz",    "application/x-compressed"},
    	        {".txt",    "text/plain"},
    	        {".wav",    "audio/x-wav"},
    	        {".wma",    "audio/x-ms-wma"},
    	        {".wmv",    "audio/x-ms-wmv"},
    	        {".wps",    "application/vnd.ms-works"},
    	        {".xml",    "text/plain"},
    	        {".z",  "application/x-compress"},
    	        {".zip",    "application/x-zip-compressed"},
    	        {"",        "*/*"}
    	};

    	for (int i = 0; i < MIME_MapTable.length; i++) {
    		if (MIME_MapTable[i][0].equals(suffix)) {
    			logger.info("====================content-type:"+MIME_MapTable[i][1]);
    			return MIME_MapTable[i][1];
    		}
    	}
    	
    	return "application/octet-stream";
    }
    
	
}
