package com.lizx.alipay.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.lizx.utils.AlipayConfig;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("alipay")
public class AlipayController {
	
	

	@RequestMapping("doPay")
	public void doPay(HttpServletResponse response) {
		
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",AlipayConfig.APP_ID, AlipayConfig.APP_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET,AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGN_TYPE); //获得初始化的AlipayClient
		
	    AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
	    alipayRequest.setReturnUrl("http://www.lizexing.cn/alipay/returnUrl");
	    alipayRequest.setNotifyUrl("http://www.lizexing.cn/alipay/notifyUrl");//在公共参数中设置回跳和通知地址
	    
	    JSONObject sys_service_provider_id = new JSONObject();
	    sys_service_provider_id.put("sys_service_provider_id", "");
	    
	    JSONObject json = new JSONObject();
	    json.put("out_trade_no", "20150320010101001");
	    json.put("product_code", "FAST_INSTANT_TRADE_PAY");
	    json.put("total_amount", "88.88");
	    json.put("subject", "Iphone6 16G");
	    json.put("body", "Iphone6 16G");
	    System.out.println();
	    alipayRequest.setBizContent(json.toString());//填充业务参数
	    String form="";
	    try {
	    	
	        form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
	        response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
		    response.getWriter().write(form);//直接将完整的表单html输出到页面
		    response.getWriter().flush();
		    response.getWriter().close();
		    
	    } catch (AlipayApiException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
	
	@RequestMapping("returnUrl")
	public String returnUrl() {
		return "success";
	}
	
	@RequestMapping("notifyUrl")
	public String notifyUrl() {
		return "success";
	}
	
}
