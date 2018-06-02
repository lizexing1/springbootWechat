package com.lizx.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lizx.service.TestService;

@Controller
public class TestController {

	@Autowired
	private TestService testService;
	
	private static Logger log = LogManager.getLogger(TestController.class);
	
	@RequestMapping("list")
	@ResponseBody
	public List<Map<String, Object>> list() {
		
		log.info("jafsdljkfds");
		log.error("升级到封建案发当时");
		
		List<Map<String, Object>> list = testService.list();
		
		return list;
	}
	
}
