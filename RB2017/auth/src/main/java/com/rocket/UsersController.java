package com.rocket;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

	private Logger logger ;
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public UsersController() {
		logger = Logger.getLogger(getClass().getName());
	}
	
	@RequestMapping("/users")
    public List<Map<String, Object>> listUsers(@RequestParam String name) {
		logger.info("listUsers invoked");
		List<Map<String, Object>> users = jdbcTemplate.queryForList("SELECT * FROM USERS");
		return users;
    }
	
	
	
}
