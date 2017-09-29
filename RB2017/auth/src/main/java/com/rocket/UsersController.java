package com.rocket;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

	private Logger logger ;
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public UsersController() {
		logger = Logger.getLogger(getClass().getName());
	}
	
	@RequestMapping(value="/auth",method = RequestMethod.POST)
	@ResponseBody
    public Response auth(@RequestBody Request request) {
		
		logger.info("Request json");
		String account = request.getUserName();
		logger.info("account="+account);
		
		List<Map<String, Object>> users = jdbcTemplate.queryForList("SELECT * FROM USERS WHERE ACCOUNT='"+account+"'");
		if (users.isEmpty()) {
			return new Response("", HttpStatus.UNAUTHORIZED);
		} else {
			
			return new Response("this is a token", HttpStatus.OK);
		}
    }
	
	
	
}
