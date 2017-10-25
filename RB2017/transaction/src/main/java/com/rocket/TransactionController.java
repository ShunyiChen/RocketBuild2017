package com.rocket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rocket.tpa.TestOnDev;
import com.rocket.tpa.model.tpackage.TpaPackage;

@RestController
public class TransactionController {

	private Logger logger ;
	@Autowired
    private JdbcTemplate jdbcTemplate;
	private String ddxDatabaseOwner = "ddxadmin";
	private TestOnDev dev;
	public TransactionController() {
		logger = Logger.getLogger(getClass().getName());
		logger.info("Setup TPA SDK.");
	    dev = new TestOnDev();
		dev.setUp();
	}
	
	@RequestMapping(value="/jobs",method = RequestMethod.POST)
	@ResponseBody
    public Response getJobs(@RequestBody Request request) {
		int userId = request.getUserId();
		logger.info("Get jobs by userId "+userId);
		if (userId == -1) {
			return new Response(HttpStatus.BAD_REQUEST);
		}
		String sql1 = "SELECT MAX(ID) AS MAXID FROM "+ddxDatabaseOwner+".JOB";
		List<Map<String, Object>> max = jdbcTemplate.queryForList(sql1);
		int jobId = 1;
		if (max.size() > 0 && max.get(0).get("MAXID") != null) {
			jobId = Integer.parseInt(max.get(0).get("MAXID").toString());
		}
		// Get available transactions, If the transaction doesn't exist then to create.
		List<TpaPackage> lst = dev.getAvailableTrans();
		String sql = "SELECT A.id as ID,B.trans_ref as REFNUMBER,A.JOB_TYPE as JOB_TYPE,A.STATUS as status,A.DESCRIPTION_1 as desc1,C.firstname as firstname,C.familyname as familyname,A.JOB_DATE as jobdate,D.display_name as filename,D.datasize as filesize FROM DDXADMIN.JOB A LEFT JOIN DDXADMIN.JOB_TRUCORE B ON A.ID = B.ID_JOB LEFT JOIN DDXADMIN.JOB_CONTACT C ON A.ID=C.ID_JOB LEFT JOIN DDXADMIN.JOB_MODEL D ON A.ID=D.ID_JOB WHERE A.ID_USER = "+userId+" AND A.DELETED = 1 ORDER BY A.ID DESC";
		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
		for (TpaPackage tp : lst) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			String createDate = sdf.format(tp.CreatedDate);
			String currentDate = sdf.format(new Date());
			if (!createDate.equals(currentDate)) {
				continue;
			}
			if (!existCheck(tp.TransactionRefNumber, results)) {
				logger.info("You received a new transaction which the trans_ref is "+tp.TransactionRefNumber+"!");
				jobId++;
				DBinsertForReceived(jobId, userId, tp);
			}
		}
		List<Map<String, Object>> finalList = jdbcTemplate.queryForList(sql);
		Response res = new Response(HttpStatus.OK);
		res.setResults(finalList);
		return res;
    }
	
	private void DBinsertForReceived(int jobId, int userId, TpaPackage pack) {
		String dt = System.currentTimeMillis()+"";
		dt = dt.substring(0, dt.length() -3);
		int datetime = Integer.parseInt(dt);
		String sql1 = "INSERT INTO ddxadmin.job" + 
				"	(id, id_user, id_server, multi_job, pdm_xml_job" + 
				"	, engdat_abstract, job_type, job_date, job_nr, description_5" + 
				"	, number_src, number_dst, datasize, status, autosend" + 
				"	, hidden, released, archived, last_access, temporary_job" + 
				"	, delete_time, ddx_version, deleted, id_job_external, pdm_teamcenter_job" + 
				"	, is_ti, sub_directory_job_num, last_send)" + 
				"VALUES ("+jobId+", "+userId+", 1, 1, 1" + 
				"	, 1, 2, "+datetime+", 1, '"+pack.Comments+"'" + 
				"	, 2, 2, 860, 2, 1" + 
				"	, 1, 1, 1, "+datetime+", 1" + 
				"	, 1, 1, 1, 0, 1" + 
				"	, 0, 0, "+datetime+")" + 
				"";
		this.jdbcTemplate.execute(sql1);
		logger.info("SQL1="+sql1);
		

		String sql2 = "INSERT INTO DDXADMIN.JOB_CONTACT" + 
				"	(ID, ID_JOB, ID_RECEIVER, RECEIVER_TYPE, MEDIUM_TYPE" + 
				"	, ENGDAT_ABSTRACT, ENGDAT_CONFIRM, DELETED, FIRSTNAME, FAMILYNAME, DEPARTMENT, PHONE,FAX, EMAIL,ID_MEDIUM)" + 
				"VALUES ("+jobId+", "+jobId+", 4, 1,10, 1, 1, 1, 'Vladtest', 'Test','_2D','123456789','_22','Dummy_40Dummy.com1', 1)";
		logger.info("SQL2="+sql2);
		jdbcTemplate.execute(sql2);

		String sql3 = "INSERT INTO DDXADMIN.JOB_TRUCORE" + 
				"	(ID, ID_JOB, TRANS_ID, TRANS_REF, STATUS_TEXT" + 
				"	, DELETED)" + 
				"VALUES ("+jobId+", "+jobId+", '"+pack.PackageID+"', '"+pack.TransactionRefNumber+"', 'RECEIVED'" + 
				"	, 1)";
		this.jdbcTemplate.execute(sql3);
		logger.info("SQL3="+sql3);
		
		String sql4 = "INSERT INTO DDXADMIN.JOB_ACCOUNTING" + 
				"	(ID, ID_JOB, TOTAL_MODELS, TOTAL_SIZE, TOTAL_SENT" + 
				"	, INITIALIZED, DELETED)" + 
				"VALUES ("+jobId+", "+jobId+", 2, 856, 0" + 
				"	, 2, 1)";
		this.jdbcTemplate.execute(sql4);
		logger.info("SQL4="+sql4);
		
		String sql5 = "INSERT INTO DDXADMIN.JOB_RESPONSE" + 
				"	(ID, ID_JOB, CREATION, START_FIRST, FINISH_FIRST" + 
				"	, RESTART, DELETED)" + 
				"VALUES ("+jobId+", "+jobId+", "+datetime+", "+datetime+", "+datetime+"" + 
				"	, 0, 1)" + 
				"";
		this.jdbcTemplate.execute(sql5);
		logger.info("SQL5="+sql5);
		
		String fileName = pack.Payloads.get(0).FileName;
		String filesuffix = getFileSuffix(fileName);
		int filesize = (int)pack.Payloads.get(0).FileSize;
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
				"	, 1, "+filesize+", '"+pack.PackageID+"', 1, 1" + 
				"	, 1)";
		this.jdbcTemplate.execute(sql7);
		logger.info("SQL7="+sql7);
	}
	
	private boolean existCheck(String refNumber, List<Map<String, Object>> results) {
		for (int i = 0; i < results.size(); i++) {
			Map<String, Object> m = results.get(i);
			String ref = m.get("REFNUMBER").toString();
			if (refNumber.equals(ref)) {
				return true;
			}
		}
		return false;
	}
	
	private String getFileSuffix(String fileName) {
        String prefix = fileName.substring(fileName.lastIndexOf(".")+1);
        return prefix;
	}
	
}
