package com.lizx.wechat.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import org.jdom2.JDOMException;
import org.springframework.stereotype.Service;

import com.lizx.utils.CommonUtil;
import com.lizx.utils.WechatConfig;
import com.lizx.utils.XMLUtil;
import com.lizx.wechat.service.WechatPayService;

import net.sf.json.JSONObject;

@Service
public class WechatPayServiceImpl implements WechatPayService{

	@Override
	public String wechatOrder(String goodsId, int goodsCont, String openid, String tocken) {
		
		/**
		 * 1、进入之前自己先做一个tocken，防止用户重复提交表单
		 * 
		 * 2、判断支付时间是否超过了十五分钟，超过了十五分钟，执行重新下单的操作，不超过十五分钟，执行第三步
		 * 
		 * 3、从数据库查询下订单表和wechat_order表是否有数据，有数据直接返回，没数据才开始做下面的操作
		 * 
		 */
		
		
		/**
		 * 根据goodsid去数据库查询商品价格，优惠卷等信息
		 * 根据用户的唯一标识，从数据库查看它的身份，看看是否给他打折
		 * 根据商品的购买数量和商品的价格，和折扣价格，优惠卷或用户是会员或什么级别等一系列信息
		 * 算出最终价格
		 */
		//这是一个订单号，先生成一个订单号
		String out_trade_no = UUID.randomUUID().toString().substring(0, 30);
		
		//
		String nonceStr = WechatConfig.CreateNoncestr();
		String body = "JSAPI支付测试";
		String trade_type = "JSAPI";
		
		/**
		 * 准备支付时需要的参数
		 */
		SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
	    parameters.put("appid", WechatConfig.APPID);
	    parameters.put("mch_id", WechatConfig.MCH_ID);
	    parameters.put("spbill_create_ip", WechatConfig.SPBILL_CREATE_IP);
	    parameters.put("nonce_str", nonceStr);
	    parameters.put("openid", openid);
	    parameters.put("body", body);
	    parameters.put("out_trade_no", out_trade_no);
	    parameters.put("total_fee", "100");
	    parameters.put("notify_url", WechatConfig.NOTIFY_URL);
	    parameters.put("trade_type", trade_type);
	    
	    /*********************生成签名开始***************************/
		//生成签名
		String sign= WechatConfig.createSign(parameters);
		
		parameters.put("sign", sign);
		/*********************生成签名结束***************************/
		
		/**
		 * 开始准备调用支付下单接口
		 */
		String requestXml = WechatConfig.getRequestXml(parameters);
		//调用统一下单的接口
		String orderUrl = WechatConfig.UNIFIEDORDER;
		
		System.out.println("stringBuffer===="+requestXml);
		
		String preparyIdXml = CommonUtil.httpRequestResult(orderUrl, "POST", requestXml);
		
		System.out.println("====preparyIdXml========preparyIdXml=="+preparyIdXml);
		
		/**
		 * 根据请求微信下单接口后返回的参数，转换成map类型，最后返回给前台
		 */
		Map<String, String> prepayIdMap =new HashMap<String, String>();
	    try {
	    	//
	    	prepayIdMap = XMLUtil.doXMLParse(preparyIdXml);
	    } catch (JDOMException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    System.out.println("****************************"+prepayIdMap.toString());
	    /***************************准备支付时需要的参数结束************************************/
		 
	    String prepayId = "";
	    //解析微信返回的信息，以Map形式存储便于取值
	    if (null != prepayIdMap) {
	    	prepayId =prepayIdMap.get("prepay_id");
		}else {
			return "";
		}
	    System.out.println("============prepayId==prepayId===prepayId======"+prepayId);
	    
	    
	   /**
	    * 处理生成签名
	    */
	    //时间戳得转换成秒，微信支付系统才能用
	    long timeStamp = System.currentTimeMillis() /1000;
	    //这个字符串一定要和上面的不能用同一个
	    String newNonceStr = WechatConfig.CreateNoncestr();
	    //重新生成一个签名，否则签名无效，必须新生成一个签名
	    SortedMap<Object,Object> signParams = new TreeMap<Object,Object>();  
	    signParams.put("appId", WechatConfig.APPID);  
	    signParams.put("nonceStr",newNonceStr);  
	    signParams.put("package", "prepay_id="+prepayId);  
	    signParams.put("timeStamp", timeStamp);  
	    signParams.put("signType", "MD5");  
	    //生成一个新的签名，必须跟调用微信支付时的订单签名不一样
		String newSign = WechatConfig.createSign(signParams);
		System.out.println("newSign.toString()--"+newSign);
		/***********************生成签名结束************************/
		
		/**
	     * 给前台返回参数，让前台支付
	     */
		JSONObject jsonObject = new JSONObject();
	    jsonObject.put("appId", WechatConfig.APPID);
	    jsonObject.put("timeStamp", String.valueOf(timeStamp));
	    jsonObject.put("nonceStr", newNonceStr);
	    jsonObject.put("packageVal", "prepay_id="+prepayId);
	    jsonObject.put("paySign", newSign);
	    
	    /**********************处理往前台返回参数结束******************************/
	    /**
	     * 往数据库插入数据，做一个判断，判断调用统一下单接口是否成功，如果失败，就不插入数据了
	     * 调用统一下单成功后再插入数据
	     */
	    /**
	     * 往数据库订单表里插入一条数据
	     * 
	     *  id
			goods_id 商品id
			user_id  用户id或openid
			price    价格
			type     类型，1、支付宝支付；2、微信支付
			status   订单状态：待支付、支付成功、代发货、已发货、收货
			create_time
			update_time
	     * 
	     * 
	     */
	    /**
	     * 首先往数据库插入一个wechat_order表的数据
	     * 	id
			appid
			time_stamp
			nonce_str
			package
			pay_sign
			goods_id
			order_id
			create_time
			update_time
	     *  把这些信息插入到数据库中，为什么要插入这些信息呢？
	     *  1、因为用户统一下单完成后，用户弹出输入密码框后，后悔了，不想支付了，这时订单状态应该时待支付状态
	     *  2、用户不小心刷新了下页面，这时订单状态也应该是待支付状态
	     */
	    
	    /**
	     * 写一个算法，这个算法你自己写
	     * 写个什么算法？返回一个时间戳，做倒计时十五分钟的操作，把时间戳放入redis里
	     */
	    
	    
		return jsonObject.toString();
	}
	
	@Override
	public boolean checkPayMessage(Map<String, String> map) {
		//商品id拿到
	    String orderId = map.get("out_trade_no");
	    //商品价格
        String totalFee = map.get("total_fee");
        //商品的状态
        String returnCode = map.get("return_code");
        //返回一个用户的openId
        String openid = map.get("openid");
        //返回一个签名算法
        String sign = map.get("sign");
        
        /**
         * 拿着返回的orderId，并拿回用户的openId去order表里查数据，进行一系列判断，
         * 目的是防止支付金额与订单本身金额不一致
         */
        Map<String, Object> orderMap = null;//this.findById(orderId);
		String rorderId = (String) orderMap.get("orderId");
		int price = (int) orderMap.get("price");
		int openId = (int) orderMap.get("openId");
		
		//判断，如果支付人和支付的订单和支付的价格全部都不一致，说明支付条件不成立，执行下面操作，返回错误
		if(!orderId.equals(rorderId) || !totalFee.equals(price) || !openid.equals(openId)) {
			return false;
		}
		
		/**
		 * 更改数据库订单状态，更改为已支付，待发货
		 */
		
		/********************************下面的业务逻辑是在医疗系统用的业务开始***********************************/
		String doctorName = "";
		String doctorMobile = "";
		String patientName = "";
		//根据订单id拿到患者手机号、患者姓名和医生姓名
//		Map<String, Object> paidQuestion = iPaidQuestionMapper.getByQuestionid(rorderId);
		Map<String, Object> paidQuestion = new HashMap<String, Object>();
		if(paidQuestion!=null) {
			doctorName = (String) paidQuestion.get("doctorName");
			doctorMobile = (String) paidQuestion.get("doctorMobile");
			patientName = (String) paidQuestion.get("patientName");
		}
		
        if(returnCode.equals("SUCCESS") && orderId.equals(rorderId) && totalFee.equals(String.valueOf(price))){
        	//调用云之讯接口 发送验证码
        	//短信发送内容是：刘医生您好，患者小明向您发起了咨询，请您及时为小明诊断。
//        	smsUtil.patientAdvisorySendNotify(doctorMobile, patientName, doctorName);
//        	smsUtil.patientAdvisorySendNotify("13243432764", patientName, "刘老师");
        	return true;
        }else{
        	return false;
        }
        /***************************************医疗以通业务逻辑结束*******************************************/
        
        
	}

}
