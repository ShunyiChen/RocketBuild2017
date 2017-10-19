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

import com.rocket.client.TpaContext;

@RestController
public class TransactionController {

	private Logger logger ;
	private TpaContext context;
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public TransactionController() {
		System.out.println("=========== Load <tpa-sdk-config.win.xml> =============");
		logger = Logger.getLogger(getClass().getName());
		// Setup TPA SDK
    	context = new TpaContext(getClass().getResourceAsStream("/tpa-sdk-config.win.xml"));
	}
	
	@RequestMapping(value="/jobs",method = RequestMethod.POST)
	@ResponseBody
    public Response getJobs(@RequestBody Request request) {
		int userId = request.getUserId();
		String sql = "SELECT * FROM DDXADMIN.JOB WHERE ID_USER = "+userId+" AND DELETED = 1 ORDER BY ID DESC";
		logger.info("sql="+sql);
		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
		Response res = new Response(HttpStatus.OK);
		res.setResults(results);
		return res;
    }
	
}
