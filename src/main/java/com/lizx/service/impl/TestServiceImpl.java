package com.lizx.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lizx.mapper.TestMapper;
import com.lizx.service.TestService;

@Service
public class TestServiceImpl implements TestService{

	@Autowired
	private TestMapper testMapper;
	
	@Override
	public List<Map<String, Object>> list() {
		
		String id = "04827d95-a817-44c1-9436-5eff3ac35575";
		
		return testMapper.list(id);
	}

}
