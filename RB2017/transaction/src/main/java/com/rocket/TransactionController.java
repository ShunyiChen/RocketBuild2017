package com.rocket;

import java.util.List;
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
import com.rocket.tpa.FindPartners;
import com.rocket.tpa.model.tpackage.Recipient;

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
	
	@RequestMapping(value="/findPartners",method = RequestMethod.POST)
	@ResponseBody
    public Response findPartners(@RequestBody Request request) {
		FindPartners fp = new FindPartners(context);
		List<Recipient> partners = fp.call();
		
		Response res = new Response(HttpStatus.OK);
		res.setPartners(partners);
		
		
		return res;
    }
	
}
