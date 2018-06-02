package com.lizx.wechat.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.JDOMException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lizx.utils.CommonUtil;
import com.lizx.utils.HttpClientUtils;
import com.lizx.utils.WechatConfig;
import com.lizx.utils.XMLUtil;
import com.lizx.wechat.bean.message.TextImgMessage;
import com.lizx.wechat.bean.message.TextMessage;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("wechat")
public class WechatHandler {

	
	private final static Logger logger = LogManager.getLogger(WechatHandler.class);
	 /**
     * 微信接入
     * @param wc
     * @return
     * @throws IOException 
     */
//    @RequestMapping(value="/connect",method = {RequestMethod.GET, RequestMethod.POST})
//    @ResponseBody
//    public void connectWeixin(HttpServletRequest request, HttpServletResponse response) throws IOException{
//        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）  
//        request.setCharacterEncoding("UTF-8");  //微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
//        response.setCharacterEncoding("UTF-8"); //在响应消息（回复消息给用户）时，也将编码方式设置为UTF-8，原理同上；
//        boolean isGet = request.getMethod().toLowerCase().equals("get"); 
//      
//         
//        try {
//            if (isGet) {
//                String signature = request.getParameter("signature");// 微信加密签名  
//                String timestamp = request.getParameter("timestamp");// 时间戳  
//                String nonce = request.getParameter("nonce");// 随机数  
//                String echostr = request.getParameter("echostr");//随机字符串  
//                
//                System.out.println("echostr==="+echostr);
//                
//                response.getWriter().write(echostr);  
//            }
//        } catch (Exception e) {
//        	logger.error("Connect the weixin server is error.");
//        }
//        
//        System.out.println("111111111111111111111111111111");
//        
//        boolean isPost = request.getMethod().toLowerCase().equals("post");
//        
//        if(isPost) {
//        	 InputStream inStream = request.getInputStream();
//     		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
//     		byte[] buffer = new byte[1024];
//     		int len = 0;
//     		while ((len = inStream.read(buffer)) != -1) {
//     			outSteam.write(buffer, 0, len);
//     		}
//     		outSteam.close();
//     		
//     		//把微信发来的xml文件信息转成String类型
//     		//result=<xml><ToUserName><![CDATA[toUser]]></ToUserName><FromUserName>< ![CDATA[fromUser] ]></FromUserName>  <CreateTime>1348831860</CreateTime>  <MsgType>< ![CDATA[text] ]></MsgType>  <Content>< ![CDATA[this is a test] ]></Content>  <MsgId>1234567890123456</MsgId>  </xml>
//     		String result = new String(outSteam.toByteArray(), "UTF-8");
//     		System.out.println(result);
//     		
//     		Map<String, String> map = null;
//     		try {
//     			//通过sax解析xml技术，把xml文本解析成map对象
//     			map = XMLUtil.doXMLParse(result);
//     		} catch (JDOMException e) {
//     			e.printStackTrace();
//     		}
//     		
//     		String msgType = map.get("MsgType");
//     		String content = map.get("Content");
//     		String event = map.get("Event");
//     		
//     		System.out.println(msgType);
//     		System.out.println(content);
//     		
//     		String responseMessage = "";
//     		if (msgType.equals("text") && content.equals("你好")) {
//
//                responseMessage = "<xml><ToUserName><![CDATA[" + map.get("FromUserName")
//     					+ "]]></ToUserName><FromUserName><![CDATA[" + map.get("ToUserName")
//     					+ "]]></FromUserName><CreateTime>" + System.currentTimeMillis()
//     					+ "</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[一点也不好]]></Content></xml>";
//
//     			System.out.println("responseMessage========"+responseMessage);
//     			
////     			 PrintWriter writer = response.getWriter();
////     			 writer.write(responseMessage);
////     			 writer.flush();
////     			 inStream.close();
////     			 writer.close();
//
//     		}
//     		
//     		/**
//     		 * 用户关注公众号后进行的处理
//     		 */
//     		if (event != null && event.equals("subscribe")) {
//
//     			System.out.println("event!=null && event.equals===================================");
//     			StringBuffer stringBuffer = new StringBuffer();
//     			stringBuffer.append("<xml>");
//     			stringBuffer.append("<ToUserName><![CDATA[" + map.get("FromUserName") + "]]></ToUserName>");
//     			stringBuffer.append("<FromUserName><![CDATA[" + map.get("ToUserName") + "]]></FromUserName>");
//     			stringBuffer.append("<CreateTime>" + System.currentTimeMillis() + "</CreateTime>");
//     			stringBuffer.append("<MsgType><![CDATA[news]]></MsgType>");
//     			stringBuffer.append("<ArticleCount>2</ArticleCount>");
//     			
//     			stringBuffer.append("<Articles>");
//
//     			stringBuffer.append("<item>");
//     			stringBuffer.append("<Title><![CDATA[这是一首歌]]></Title>");
//     			stringBuffer.append("<Description><![CDATA[这是一首快乐的歌曲！！！]]></Description>");
//     			stringBuffer.append(
//     					"<PicUrl><![CDATA[http://g.hiphotos.baidu.com/image/pic/item/78310a55b319ebc4a99197708826cffc1f171669.jpg]]></PicUrl>");
//     			stringBuffer.append("<Url><![CDATA[www.baidu.com]]></Url>");
//     			stringBuffer.append("</item>");
//
//     			stringBuffer.append("<item>");
//     			stringBuffer.append("<Title><![CDATA[这是一个美女]]></Title>");
//     			stringBuffer.append("<Description><![CDATA[你猜是哪个美女？？？]]></Description>");
//     			stringBuffer.append(
//     					"<PicUrl><![CDATA[http://b.hiphotos.baidu.com/image/pic/item/54fbb2fb43166d220612c57c4c2309f79152d2f5.jpg]]></PicUrl>");
//     			stringBuffer
//     					.append("<Url><![CDATA[https://zhidao.baidu.com/question/1732094237675716987.html]]></Url>");
//     			stringBuffer.append("</item>");
//
//     			stringBuffer.append("</Articles>");
//     			stringBuffer.append("</xml>");
//
//     			responseMessage = stringBuffer.toString();
//     		}
//     		
//     		 PrintWriter writer = response.getWriter();
//     		 writer.write(responseMessage);
//     		 writer.flush();
//     		 inStream.close();
//     		 writer.close();
//             
//             response.getWriter().write("success"); 
//        }
//        
//        
//    }
    
    
    @RequestMapping(value="/connect",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void connectWeixin(HttpServletRequest request, HttpServletResponse response) throws IOException{
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）  
        request.setCharacterEncoding("UTF-8");  //微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
        response.setCharacterEncoding("UTF-8"); //在响应消息（回复消息给用户）时，也将编码方式设置为UTF-8，原理同上；
        boolean isGet = request.getMethod().toLowerCase().equals("get"); 
      
         
        try {
            if (isGet) {
                String signature = request.getParameter("signature");// 微信加密签名  
                String timestamp = request.getParameter("timestamp");// 时间戳  
                String nonce = request.getParameter("nonce");// 随机数  
                String echostr = request.getParameter("echostr");//随机字符串  
                
                System.out.println("echostr==="+echostr);
                
                response.getWriter().write(echostr);  
            }
        } catch (Exception e) {
        	logger.error("Connect the weixin server is error.");
        }
        
        System.out.println("111111111111111111111111111111");
        
        boolean isPost = request.getMethod().toLowerCase().equals("post");
        
        if(isPost) {
        	 InputStream inStream = request.getInputStream();
     		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
     		byte[] buffer = new byte[1024];
     		int len = 0;
     		while ((len = inStream.read(buffer)) != -1) {
     			outSteam.write(buffer, 0, len);
     		}
     		outSteam.close();
     		
     		//把微信发来的xml文件信息转成String类型
     		//result=<xml><ToUserName><![CDATA[toUser]]></ToUserName><FromUserName>< ![CDATA[fromUser] ]></FromUserName>  <CreateTime>1348831860</CreateTime>  <MsgType>< ![CDATA[text] ]></MsgType>  <Content>< ![CDATA[this is a test] ]></Content>  <MsgId>1234567890123456</MsgId>  </xml>
     		String result = new String(outSteam.toByteArray(), "UTF-8");
     		System.out.println(result);
     		
     		Map<String, String> map = null;
     		try {
     			//通过sax解析xml技术，把xml文本解析成map对象
     			map = XMLUtil.doXMLParse(result);
     		} catch (JDOMException e) {
     			e.printStackTrace();
     		}
     		
     		String msgType = map.get("MsgType");
     		String content = map.get("Content");
     		String event = map.get("Event");
     		
     		System.out.println(msgType);
     		System.out.println(content);
     		
     		String responseMessage = "";
     		if (msgType.equals("text")) {
     			
     			/**
     			 * 根据用户传入进来的文本内容，从solr中找到符合条件最高的文本内容，进行返回
     			 * 这样就可以做到智能回复了
     			 * 
     			 * 用户发消息：我拍的CT片，骨折的位置裂缝为多少毫米，请问有什么影响吗？
     			 * 
     			 * 两种解决方案
     			 * （1）solr接收到用户的消息后，会从solr库中找到最符合的一条内容进行文本回复。
     			 * （2）solr接收到用户的消息后，会从solr库中找到最符合的前八条内容进行图文回复
     			 */
     			String solrResult = "";//solrService.search(content);
     			
     			TextMessage textMessage = new TextMessage();
     			textMessage.setContent(solrResult);
     			textMessage.setCreateTime(System.currentTimeMillis());
     			textMessage.setMsgType("text");
     			textMessage.setFromUserName(map.get("ToUserName"));
     			textMessage.setToUserName(map.get("FromUserName"));
     			
     			responseMessage =textMessage.toXML();
     			
     			System.out.println("responseMessage========"+responseMessage);

     		}
     		
     		/**
     		 * 用户关注公众号后进行的处理
     		 */
     		if (event != null && event.equals("subscribe")) {
     			
     			TextImgMessage imgMessage = new TextImgMessage();
     			
     			imgMessage.setFromUserName(map.get("ToUserName"));
     			imgMessage.setToUserName(map.get("FromUserName"));
     			imgMessage.setMsgType(msgType);
     			imgMessage.setCreateTime(System.currentTimeMillis());
     			
     			Map<String, Object> itemMap = new HashMap<String, Object>();
     			itemMap.put("Description", "这是一首快乐的歌曲！！！");
     			itemMap.put("Title", "这是一首歌");
     			itemMap.put("PicUrl", "http://g.hiphotos.baidu.com/image/pic/item/78310a55b319ebc4a99197708826cffc1f171669.jpg");
     			itemMap.put("Url", "www.baidu.com");
     			imgMessage.getList().add(itemMap);
     			
     			Map<String, Object> itemMap2 = new HashMap<String, Object>();
     			itemMap2.put("Description", "这是一个美女");
     			itemMap2.put("Title", "你猜是哪个美女？？？");
     			itemMap2.put("PicUrl", "https://zhidao.baidu.com/question/1732094237675716987.html");
     			itemMap2.put("Url", "www.baidu.com");
     			imgMessage.getList().add(itemMap2);
     			
     			responseMessage = imgMessage.toXML();

     			System.out.println("event!=null && event.equals===================================");
     		}
     		
     		 PrintWriter writer = response.getWriter();
     		 writer.write(responseMessage);
     		 writer.flush();
     		 inStream.close();
     		 writer.close();
             
             response.getWriter().write("success"); 
        }
        
        
    }
    
//    public static void main(String[] args) {
//    	
//    	
//    	     TextImgMessage imgMessage = new TextImgMessage();
//			
//			imgMessage.setFromUserName("1111");
//			imgMessage.setToUserName("2222222");
//			imgMessage.setMsgType("3333333");
//			imgMessage.setCreateTime(System.currentTimeMillis());
//			
//			Map<String, Object> itemMap = new HashMap<String, Object>();
//			itemMap.put("Description", "这是一首快乐的歌曲！！！");
//			itemMap.put("Title", "这是一首歌");
//			itemMap.put("PicUrl", "http://g.hiphotos.baidu.com/image/pic/item/78310a55b319ebc4a99197708826cffc1f171669.jpg");
//			itemMap.put("Url", "www.baidu.com");
//			imgMessage.getList().add(itemMap);
//			
//			Map<String, Object> itemMap2 = new HashMap<String, Object>();
//			itemMap2.put("Description", "这是一个美女");
//			itemMap2.put("Title", "你猜是哪个美女？？？");
//			itemMap2.put("PicUrl", "https://zhidao.baidu.com/question/1732094237675716987.html");
//			itemMap2.put("Url", "www.baidu.com");
//			imgMessage.getList().add(itemMap2);
//			
//			System.out.println(imgMessage.toXML());;
//	}
    
//    public static void main(String[] args) {
//    		String solrResult = "";//solrService.search(content);
			
//			TextMessage textMessage = new TextMessage();
//			textMessage.setContent(solrResult);
//			textMessage.setCreateTime(System.currentTimeMillis());
//			textMessage.setMsgType("text");
//			textMessage.setFromUserName("111111111");
//			textMessage.setToUserName("22222222222");
//			
//			String responseMessage =textMessage.toXML();
//			System.out.println(responseMessage);
			
//			String string = textMessage.toString(4);
//			System.out.println(string);
//	}
	
    
//    public static void main(String[] args) {
//		
//    	
//    	
//    	try {
//			InputStream inStream = new FileInputStream("C:\\Users\\lizexing\\Desktop\\test.txt");
//			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
//			byte[] buffer = new byte[1024];
//			int len = 0;
//			while ((len = inStream.read(buffer)) != -1) {
//				outSteam.write(buffer, 0, len);
//			}
//			outSteam.close();
//			
//			String result = new String(outSteam.toByteArray(),"UTF-8");
//			
//			Map<String, String> map = null;
//			try {
//				//通过sax解析xml技术，把xml文本解析成map对象
//				map = XMLUtil.doXMLParse(result);
//			} catch (JDOMException e) {
//				e.printStackTrace();
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
    
    public static void main(String[] args) {
		
    	String getTockenUrl = WechatConfig.GET_TOCKEN_URL.replace("APPID", WechatConfig.APPID).replace("APPSECRET", WechatConfig.APP_SECRECT);
    	String tockenStr = HttpClientUtils.getMethod(getTockenUrl);
    	System.out.println(tockenStr);
    	JSONObject tockenJson = JSONObject.fromObject(tockenStr);
    	String access_token = (String) tockenJson.get("access_token");
    	
    	String sendTempleteUrl = WechatConfig.SEND_TEMPLETE_URL.replace("ACCESS_TOKEN", access_token);
    	
    	
    	JSONObject firstJson = new JSONObject();
    	firstJson.put("value", "恭喜你面试成功啦！！！");
    	firstJson.put("color", "#FFF68F");
    	
    	JSONObject keyword1Json = new JSONObject();
    	keyword1Json.put("value", "看厕所的保安");
    	keyword1Json.put("color", "#FF34B3");
    	
    	JSONObject keyword2Json = new JSONObject();
    	keyword2Json.put("value", "你看着来！");
    	keyword2Json.put("color", "#FFA07A");
    	
    	JSONObject keyword3Json = new JSONObject();
    	keyword3Json.put("value", "测试！！！");
    	keyword3Json.put("color", "#CD853F");
    	
    	JSONObject keyword4Json = new JSONObject();
    	keyword4Json.put("value", "所长！！！");
    	keyword4Json.put("color", "");
    	
    	JSONObject remarkJson = new JSONObject();
    	remarkJson.put("value", "点击查看详情！！！");
    	remarkJson.put("color", "#CD00CD");
    	
    	JSONObject dataJson = new JSONObject();
    	dataJson.put("first", firstJson);
    	dataJson.put("keyword1", keyword1Json);
    	dataJson.put("keyword2", keyword2Json);
    	dataJson.put("keyword3", keyword3Json);
    	dataJson.put("keyword4", keyword4Json);
    	dataJson.put("remark", remarkJson);
    	
    	JSONObject jsonParam = new JSONObject();
    	/**
    	 * touser后面的参数是用户的唯一标识，openid，这个值可以从数据库中获取
    	 * 既然要推送模板，那用户的openid必须在数据库存着，否则无法推送模板
    	 * 
    	 * 用户去医院看病后，医生给患者做宣传，患者观众公众号，再用手机号访问下我们的项目
    	 * 手机号和用户的openid都对应上了，患者的CT片子出来后，CT的地址信息会从医院传送到我们平台上
    	 * 我们平台根据用户的手机号，查找到用户的openid，然后再把对应的CT信息地址放到url位置里
    	 * 给患者推送一个模板
    	 * 
    	 */
    	jsonParam.put("touser", "oKOSgw7LW7FW_K5WBal5N3ij2qS8");
    	jsonParam.put("template_id", "I6LUFw3q_MG2bIs8uN9GgD6Qkv0M1Og5A25BDYN4ynE");
    	jsonParam.put("url", "http://www.mm131.com/qingchun/");
    	jsonParam.put("data", dataJson);
    	
    	String httpRequestResult = CommonUtil.httpRequestResult(sendTempleteUrl, "POST", jsonParam.toString());
    	System.out.println(httpRequestResult);
	}
    
    /**
     * 获取授权的code
     */
    @RequestMapping("getCode")
	public void getCode(HttpServletResponse response,@RequestParam("status") String status) {
    	
		String redirectUrl = URLEncoder.encode(WechatConfig.REDIRECT_GETCODE_URI);
		
		String codeUrl = WechatConfig.GETCODE_URI.replace("APPID", WechatConfig.APPID)
				.replace("REDIRECT_URI", redirectUrl).replace("SCOPE", "snsapi_userinfo").replace("STATE", status);

		System.out.println("codeUrl====="+codeUrl);
		try {
			response.sendRedirect(codeUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("获取code进行转发时出现异常：codeUrl==" + codeUrl);
		}
	}
    
    @RequestMapping("getOpenId")
	public void getOpenId(@RequestParam("code") String code, 
			@RequestParam("state") String state,HttpServletResponse response,
			HttpServletRequest request) {
    	
    	// 根据返回来的code去访问微信，最后返回accesstoken，里面包含openid和access_token
		String getAccessTokenUrl = WechatConfig.GET_OPENID_URI.replace("APPID", WechatConfig.APPID)
				.replace("SECRET", WechatConfig.APP_SECRECT).replace("CODE", code);
		
		//请求获取tocken和openid
		String accessToken = HttpClientUtils.getMethod(getAccessTokenUrl);
		System.out.println("getAccessTokenUrl====" + accessToken);

		// 把相应回来的json字符串转换成json格式，用户键值对获取里面具体的值
		JSONObject openIdJson = JSONObject.fromObject(accessToken);

		/**
		 * 对返回的数据一定要做容错机制处理，防止获取openid时报错
		 */
		
		// 获取openid
		String openid = (String) openIdJson.get("openid");
		System.out.println("openid===" + openid);
		// 获取access_token
		String access_token = (String) openIdJson.get("access_token");
		System.out.println("access_token===" + access_token);

		/**
		 * 把openid和access_token放入到session中
		 * 为什么要放到session中呢？
		 * 因为openid以后从数据库查数据和微信支付、获取用户信息都需要，
		 * 相当重要的字段
		 */
		request.getSession().setAttribute("openid", openid);
		request.getSession().setAttribute("access_token", access_token);
		
		/**
		 * 返回页面，进入个人中心里或是返回到前台页面里
		 */
		try {
			//用jsp你可以返回的jsp页面
			//return "";                                       
			response.sendRedirect("http://www.lizexing.cn/wechat/redirect.html?status="+state);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @RequestMapping("getWechatUserInfo")
    @ResponseBody
	public String getWechatUserInfo(HttpServletRequest request) {
    	
    	String openid = (String) request.getSession().getAttribute("openid");
    	String access_token = (String) request.getSession().getAttribute("access_token");

		// 拼接获取用户信息的地址，再httpClient请求下
		String getUserInfo = WechatConfig.GET_USERINFO_URI.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);

		String userInfo = HttpClientUtils.getMethod(getUserInfo);
		System.out.println("userInfo====" + userInfo);
		return userInfo;
	}
    
    
    @RequestMapping("testSetSession")
    @ResponseBody
	public void testSession(HttpServletRequest request) {
    	
    	System.out.println("我开始放session");
    	request.getSession().setAttribute("list", "1111111111111111");
    	
	}
    
    @RequestMapping("testGetSession")
    @ResponseBody
	public void testGetSession(HttpServletRequest request) {
    	
    	System.out.println("我开始接收session");
    	String attribute = (String) request.getSession().getAttribute("list");
    	System.out.println("attribute====="+attribute);
	}
    
}
