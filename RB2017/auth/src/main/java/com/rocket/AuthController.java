package com.rocket;

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

import com.procaess.ddxv6.serverDatabase.DDXDatabaseUtilities;

@RestController
public class AuthController {

	private Logger logger ;
	private String ddxDatabaseOwner = "ddxadmin";
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public AuthController() {
		logger = Logger.getLogger(getClass().getName());
	}
	
	@RequestMapping(value="/auth",method = RequestMethod.POST)
	@ResponseBody
    public Response auth(@RequestBody Request request) {
		logger.info("Request json");
		String account = request.getAccount();
		String password = request.getPassword();
		logger.info("account="+account);
		String passwordLine = " AND USERS_PASSWORD like " + DDXDatabaseUtilities.stringToCryptedString(password);
		String sql = "SELECT COMPANY_ID," +
	        "COMPANY_ENGDAT_QUAL," +
	        "COMPANY_ENGDAT_NAME," +
	        "COMPANY_ODETTE_SSID," +
	        "COMPANY_ODETTE_SFID," +
	        "COMPANY_NAME," +
	        "COMPANY_STREET," +
	        "COMPANY_TOWN," +
	        "COMPANY_POSTCODE," +
	        "COMPANY_FACTORY," +
	        "COMPANY_COUNTRY," +
	        "COMPANY_PGP_KEY_PRIVATE," +
	        "COMPANY_PGP_KEY_PUBLIC," +
	        "COMPANY_PGP_ENCRYPTION," +
	        "COMPANY_FACTOR_A," +
	        "COMPANY_FACTOR_C," +
	        "COMPANY_ENGDAT_DUNS_NO," +
	        "USERS_ID," +
	        "USERS_ID_SERVER," +
	        "USERS_ID_PROXY_SERVER," +
	        "USERS_ID_COMPANY," +
	        "USERS_ID_DEPARTMENT," +
	        "USERS_ID_PROFILE," +
	        "USERS_ID_USER_ROLE," +
	        "USERS_ACCOUNT," +
	        "USERS_PASSWORD," +
	        "USERS_LOGIN_HOST," +
	        "USERS_LOGIN_ACCOUNT," +
	        "USERS_LOGIN_PASSWORD," +
	        "USERS_FIRSTNAME," +
	        "USERS_FAMILYNAME," +
	        "USERS_DEF_ADDRESSCODE_5," +
	        "USERS_DEF_ADDRESSCODE_14," +
	        "USERS_PHONE," +
	        "USERS_FAX," +
	        "USERS_EMAIL," +
	        "USERS_EMAIL_CC," +
	        "USERS_EMAIL_EVENT," +
	        "USERS_MESSAGE_TYPE," +
	        "USERS_EMAIL_CC_TYPE," +
	        "USERS_USE_DATASERVER," +
	        "USERS_IS_PORTAL_USER," +
	        "USERS_LDAP_USER_TYPE," +
	        "USERS_COST_CENTER," +
	        "USERS_LAST_ACCESS," +
	        "USERS_SELFWEB_IPN," +
	        "USERS_SAVE_ONLYONDEFINED_DS," +
	        "USERS_NOTIFIC_PROF_ACTIVATED," +
	        "USERS_ID_DEFAULT_NOTIFIC_PROF," +
	        "USERS_PORTAL_LOGIN," + 
	        "USERS_PORTAL_PASSWORD," + 
	        "USERS_USE_DOWNLOAD_HOST," + 
	        "C_DEPARTMENT_ID," +
	        "C_DEPARTMENT_ID_COMPANY," +
	        "C_DEPARTMENT_DEPARTMENT_NAME," +
	        "ROLE_ID," +
	        "ROLE_NAME," +
	        "ROLE_ADMIN," +
	        "ROLE_CREATE_JOB," +
	        "ROLE_SEND_JOB," +
	        "ROLE_SEND_INTERNAL_JOB," +
	        "ROLE_RECEIVE_JOB," +
	        "ROLE_OFFLINE_RECEIVE," +
	        "ROLE_PORTAL_USER," +
	        "ROLE_PROJECT_MANAGER," +
	        "ROLE_KVS_RECEIVE," +
	        "ROLE_INQUIRY," +
	        "ROLE_INQUIRY_ONLY_OWN_JOBS," +
	        "ROLE_V2_INQUIRY," +
	        "ROLE_RESPONSE," +
	        "ROLE_ACCOUNTING_INTERN," +
	        "ROLE_ACCOUNTING_EXTERN," +
	        "ROLE_CHANGE_SKELETON," +
	        "ROLE_ADMIN_CONFIGURATION," +
	        "ROLE_CONFIGURATION_MAIL," +
	        "ROLE_CONFIGURATION_FAX," +
	        "ROLE_CONFIGURATION_DESCR_FIELD," +
	        "ROLE_CONFIGURATION_ARCHIVING," +
	        "ROLE_CONFIGURATION_USEREXIT," +
	        "ROLE_CONFIGURATION_MISC," +
	        "ROLE_CONFIGURATION_LDAP," +
	        "ROLE_CONFIGURATION_OFFLINE," +
	        "ROLE_CONFIGURATION_EDI," +
	        "ROLE_CONFIGURATION_SERVER," +
	        "ROLE_ADMIN_DATABASE," +
	        "ROLE_DB_PARTNER_CONTACT," +
	        "ROLE_DB_COMPANY_USER," +
	        "ROLE_DB_CADSYSTEM_PROJECT," +
	        "ROLE_DB_SKELETON," +
	        "ROLE_DB_DATASERVER," +
	        "ROLE_ADMIN_DDX_STATE," +
	        "ROLE_STATE_QUEUE," +
	        "ROLE_STATE_DB_OPTIMIZER," +
	        "ROLE_STATE_ARCHIVING," +
	        "ROLE_STATE_SERVER," +
	        "ROLE_STATE_EDIRECEIVER," +
	        "ROLE_STATE_EDICONFIRMER," +
	        "ROLE_STATE_CONVERTER," +
	        "ROLE_STATE_KVS_SERVER" +
	 " FROM "+ddxDatabaseOwner+".V_COMPANY_USER_INFO "
	 + "WHERE USERS_ACCOUNT like "+DDXDatabaseUtilities.stringToTableString(account) +
	 passwordLine;
		
		List<Map<String, Object>> company_user_info = jdbcTemplate.queryForList(sql);
		if (company_user_info.isEmpty()) {
			return new Response("Login failed for user: "+account, HttpStatus.UNAUTHORIZED);
		} else {
			Response res = new Response("Login successfully.", HttpStatus.OK);
			res.setCompany_user_info(company_user_info);
			return res;
		}
    }
}
