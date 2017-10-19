package com.rocket;

import java.util.ArrayList;
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

@RestController
public class PartnerController {

	private Logger logger ;
	private String ddxDatabaseOwner = "ddxadmin";
	final public static short DELETED_NO  = (short)1;
//	private TpaContext context;
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public PartnerController() {
//		System.out.println("=========== Load <tpa-sdk-config.win.xml> =============");
		logger = Logger.getLogger(getClass().getName());
		// Setup TPA SDK
//    	context = new TpaContext(getClass().getResourceAsStream("/tpa-sdk-config.win.xml"));
	}
	
	@RequestMapping(value="/partnerfactory",method = RequestMethod.POST)
	@ResponseBody
    public Response getPartnerFactory(@RequestBody Request request) {
//		int idPartnerFactory = request.getIdPartnerFactory();
		int userId = request.getUserId();
//		logger.info("idPartnerFactory="+idPartnerFactory);
		logger.info("userId="+userId);
		List<Map<String, Object>> partnerFactorys = new ArrayList<Map<String, Object>>();
		// Doing SQL query for profile 'ALL'
		String sql1 = "SELECT ID FROM ddxadmin.PROFILE_REFS WHERE PROFILE_NAME IN (SELECT PROFILE_NAME FROM ddxadmin.PROFILES WHERE ID_USER="+userId+" AND DELETED=1) AND ID_PARTNER IS NULL AND DELETED=1";
		logger.info("sql1="+sql1);
		List<Map<String, Object>> all = jdbcTemplate.queryForList(sql1);
		if (all != null && all.size() > 0) {
			// Build where statement for TRUcore locations
			String sql2 = "SELECT ID,ID_COMPANY,LOCATION_CODE,TRUCORE_COMPANY_ID,TRUCORE_LOCATION_ID,DATE_LASTUPDATE,DATE_LASTUPDATE_COMP,DELETED,LOCKED,LOCK_DATE FROM ddxadmin.TRUCORE_COMPANY WHERE ID_COMPANY IN (SELECT ID_COMPANY FROM ddxadmin.USERS WHERE ID="+userId+" AND DELETED=1) AND DELETED=1";
			logger.info("sql2="+sql2);
			List<Map<String, Object>> currentTRUcoreCompanyData = jdbcTemplate.queryForList(sql2);
			if (currentTRUcoreCompanyData != null && currentTRUcoreCompanyData.size() > 0) {
				// If profile 'ALL' exists for user, get all partners
				int companyId = Integer.parseInt(currentTRUcoreCompanyData.get(0).get("ID").toString());
				String sql3 = "SELECT ID,NAME_CODED,NAME,STREET,TOWN,POSTCODE,FACTORY,COUNTRY,INFO_FILE,INFO_SHORT FROM ddxadmin.PARTNER_FACTORY WHERE ID IN (SELECT ID_PART_FACT FROM ddxadmin.DEFAULT_MEDIA) AND DELETED=1 AND (ID IN (SELECT ID_PART_FACT_DDX FROM ddxadmin.PARTNER_FACTORY_TRUCORE WHERE ID_TRUCORE_COMPANY="+companyId+" AND DELETED=1) OR ID NOT IN (SELECT ID_PART_FACT_DDX FROM ddxadmin.PARTNER_FACTORY_TRUCORE WHERE DELETED=1)) AND ID_PART_FACT IS NOT NULL";
				logger.info("sql3="+sql3);
				partnerFactorys = jdbcTemplate.queryForList(sql3);
			}
		}
		Response res = new Response(HttpStatus.OK);
		res.setResults(partnerFactorys);
		return res;
    }
	
	@RequestMapping(value="/partners",method = RequestMethod.POST)
	@ResponseBody
    public Response getPartners(@RequestBody Request request) {
		int idPartnerFactory = request.getIdPartnerFactory();
		String sql = "SELECT PART_FACT_ID,PART_FACT_ID_PART_FACT,PART_FACT_NAME_CODED,PART_FACT_NAME,PART_FACT_STREET,PART_FACT_TOWN,PART_FACT_POSTCODE,PART_FACT_FACTORY,PART_FACT_COUNTRY,PART_FACT_INFO_FILE,PART_FACT_INFO_SHORT,PART_FACT_COMMENTS,PART_FACT_PROJECT_REC,PART_FACT_COMPRESSION,PART_FACT_ABSTRACT_TYPE,PART_FACT_PGP_KEY_PUBLIC,PART_FACT_PGP_ENCRYPTION,PART_FACT_LAST_ACCESS,PART_FACT_FACTOR_A,PART_FACT_EXCHANGE_ASSEMBLY,PART_FACT_ENGDAT_DUNS_NO,PART_FACT_DELETED,PART_FACT_LOCKED,PART_FACT_LOCK_DATE,CONTACT_ID,CONTACT_FIRSTNAME,CONTACT_FAMILYNAME,CONTACT_ID_DEPARTMENT,CONTACT_PHONE,CONTACT_FAX,CONTACT_EMAIL,CONTACT_EMAIL_CC,CONTACT_MESSAGE_TYPE,CONTACT_EMAIL_CC_TYPE,CONTACT_EMAIL_EVENT,CONTACT_LAST_ACCESS,CONTACT_DEF_ADDRESS_5,CONTACT_DEF_ADDRESS_14,CONTACT_PORTAL_LOGIN,CONTACT_PORTAL_PASSWORD,CONTACT_PORTAL_CONTACT_TYPE,CONTACT_DELETED,CONTACT_LOCKED,CONTACT_LOCK_DATE,PART_FACT_DEPART_ID,PART_FACT_DEPART_ID_PART_FACT,PART_FACT_DEPARTMENT_NAME FROM ddxadmin.V_PARTNER_CONTACT_INFO WHERE PART_FACT_ID="+idPartnerFactory;
		logger.info("sql="+sql);
		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
		Response res = new Response(HttpStatus.OK);
		res.setResults(results);
		return res;
	}
}
