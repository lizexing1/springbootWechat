package com.lizx.wechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.lizx.wechat.service.RedisService;

@Component
public class QuartzController {
	
	 @Autowired
	 RedisService redisService;

	@Scheduled(cron="* * * * * ?") //每分钟执行一次  
    public void statusCheck() {      
        //statusTask.healthCheck();  
		System.out.println("0 0/1 * * * ?"+redisService.getStr("1511B"));
    }    
  
    @Scheduled(fixedRate=2000)  
    public void testTasks() {   
    	System.out.println("我是每两秒执行一次");
    }    
	
}
