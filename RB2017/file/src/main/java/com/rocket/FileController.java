package com.rocket;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	}
	
	@RequestMapping(value="/send",method = RequestMethod.POST)
	@ResponseBody
    public Response send(@RequestParam("file") MultipartFile[] files) {
		Response res = new Response(HttpStatus.OK);
		if (files == null) {
			logger.info("The files ware empty.");
        	res = new Response(HttpStatus.BAD_REQUEST);
        	return res;
		}
		for (int i = 0; i < files.length; i++) {
			String name = files[i].getOriginalFilename();
			try {
                byte[] bytes = files[i].getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(tempDir+""+name));
                stream.write(bytes);
                stream.close();
                res = new Response(HttpStatus.OK);
                logger.info("You successfully uploaded " + name + " into " + tempDir);
                return res;
            } catch (Exception e) {
            	logger.info("You failed to upload \" + name + \" => \" "+ e.getMessage());
            	res = new Response(HttpStatus.INTERNAL_SERVER_ERROR);
            }
		}
		return res;
    }
	
	@RequestMapping(value="/submit",method = RequestMethod.POST)
	@ResponseBody
    public Response submit(@RequestBody Request request) {
		Response res = new Response(HttpStatus.OK);
		String[] fileNames = request.getFileNames();
		if (fileNames != null && fileNames.length > 0) {
			//File exist check
			boolean allexist = true;
			for (int i = 0; i < fileNames.length; i++) {
				if (!new File(tempDir, fileNames[i]).exists()) {
					allexist = false;
				}
			}
			if (allexist) {
				// Invoke the SDK to upload files.
				TestOnDev t = new TestOnDev();
				t.setUp();
				t.setUploadFiles(fileNames);
				t.testcase2();
				
				DBUpdate(request);
				
			} else {
				logger.info("You failed to submit.");
            	res = new Response(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} else {
			logger.info("You failed to submit, because the filenames was empty.");
        	res = new Response(HttpStatus.BAD_REQUEST);
		}
		return res;
    }
	
	private void DBUpdate(Request request) {
		String sql1 = "SELECT MAX(ID) AS MAXID FROM "+ddxDatabaseOwner+".JOB";
		List<Map<String, Object>> max = jdbcTemplate.queryForList(sql1);
		int maxid = 1;
		if (max.size() > 0) {
			maxid = Integer.parseInt(max.get(0).get("MAXID").toString());
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
		
		
		String sql2 = "INSERT INTO ddxadmin.JOB (ID,ID_USER,ID_USER_PROFILE,ID_SERVER,ID_PARENT_JOB,ID_JOB_EXTERNAL,MULTI_JOB,PDM_XML_JOB,PDM_TEAMCENTER_JOB,ENGDAT_ABSTRACT,JOB_TYPE,JOB_DATE,DESCRIPTION_1,DESCRIPTION_2,DESCRIPTION_3,DESCRIPTION_4,DESCRIPTION_5,NUMBER_SRC,NUMBER_DST,DATASIZE,STATUS,AUTOSEND,SENDTIME,COMMENTS,HIDDEN,RELEASED,ID_DATASERVER,ARCHIVED,DOCUMENT_ID,LAST_ACCESS,HOLD_BACK_TIME,TEMPORARY_JOB,EMAIL,DELETE_TIME,KVS_ACCESS_PERMISSION,KVS_SERVER_NAME,PDM_SYS_ID,DDX_VERSION,EMAIL_CC,ID_NOTIFICATION_PROFILE,DELETED,LOCKED,LOCK_DATE,JOB_FORWARD,IS_TI,ID_DOC_SUBMITED,SUB_DIRECTORY_JOB_NUM,ERROR_REASON, SEND_RETRY, LAST_SEND) VALUES ("+maxid+","+userId+",NULL,1,NULL,NULL,1,1,1,1,1,"+datetime+",'"+d1+"','"+d2+"','"+d3+"','"+d4+"','"+d5+"',0,NULL,NULL,7,2,NULL,NULL,1,1,NULL,1,NULL,"+datetime+",NULL,1,NULL,1,NULL,NULL,NULL,1,NULL,NULL,1,NULL,NULL,1,0,NULL,0,NULL,NULL,NULL)";
	
		System.out.println(sql2);
		logger.info("sql2="+sql2);
		
		// 
//		jdbcTemplate.execute(action)
	}
	
}
