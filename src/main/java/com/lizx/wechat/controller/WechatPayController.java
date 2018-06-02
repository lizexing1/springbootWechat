package com.lizx.wechat.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lizx.utils.WechatConfig;
import com.lizx.utils.XMLUtil;
import com.lizx.wechat.service.WechatPayService;

@RestController
@RequestMapping("wechatPay")
public class WechatPayController {
	
	private static Logger logger = LogManager.getLogger(WechatPayController.class);

	@Autowired
	private WechatPayService wechatPayService;
	
	@RequestMapping("wechatPay")
	public String wechatOrder(HttpServletRequest request,@RequestParam("goodsCont")int goodsCont,
			@RequestParam("goodsId")String goodsId,@RequestParam("tocken")String tocken) {
		
		//从session中获取用户的唯一标识
		String openid = (String) request.getSession().getAttribute("openid");
		
		String result = wechatPayService.wechatOrder(goodsId,goodsCont,openid,tocken);
		return result;
		
	}
	
	/**
	 * <pre>notifyUrl(这个方法不管支付成功或失败，只要微信调用到这个方法后，
	 *   你必须给微信返回一个xml的字符串，里面包含success，否则微信会连续三连接调用这个接口，
	 *   如果三次都调用失败，说明你的平台没有接收到用户支付成功的通知，在微信端，微信收到钱了，
	 *   你可以做查询订单的操作，看看用户是否支付过，对你的数据库再进行更改)   
	 * 创建人：李泽兴 lizexing@163.com      
	 * 创建时间：2018年5月31日 下午4:00:42    
	 * 修改人：李泽兴 lizexing@163.com       
	 * 修改时间：2018年5月31日 下午4:00:42    
	 * 修改备注： 
	 * @param request
	 * @param response
	 * @return</pre>
	 */
	@RequestMapping("notifyUrl")
	public void notifyUrl(HttpServletRequest request,HttpServletResponse response) {
		
		 try {
			 		/**
			 		 * 由于微信通知的时候返回的是一个xml字符串，所以不能用request.getParemter方法接收
			 		 * 只能用流接收，因为它是xml格式的
			 		 */
					InputStream inStream = request.getInputStream();
				    ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
				    byte[] buffer = new byte[1024];
				    int len = 0;
				    while ((len = inStream.read(buffer)) != -1) {
				        outSteam.write(buffer, 0, len);
				    }
				    outSteam.close();
				    inStream.close();
				    
				    //上面的操作是把xml流接收到，然后解析，这行代码是把xml流转换成字符串
				    String result = new String(outSteam.toByteArray(), "utf-8");
				    
				    logger.info("============result=========="+result);
				    
				    Map<String, String> map = null;
				    try {
				    	//通过sax解析xml的技术，把返回的结果转换成了map对象
				        map = XMLUtil.doXMLParse(result);
				    } catch (JDOMException e) {
				        e.printStackTrace();
				    }
				    
				    
				    // 此处调用订单查询接口验证是否交易成功
				    boolean isSucc = wechatPayService.checkPayMessage(map);
				    
				    // 支付成功，商户处理后同步返回给微信参数
				    PrintWriter writer = response.getWriter();
				    if (isSucc) {
				    	logger.info("===============付款成功，业务处理完毕==============");
				        // 通知微信已经收到消息，不要再给我发消息了，否则微信会8连击调用本接口
				        String noticeStr = WechatConfig.setXML("SUCCESS", "");
				        writer.write(noticeStr);
				        writer.flush();
				    } 
			    	// 支付失败， 记录流水失败
				    logger.info("===============支付失败==============");
				    
				    String noticeStr = WechatConfig.setXML("FAIL", "");
				    writer.write(noticeStr);
				    writer.flush();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	/**
	 * <pre>getRePay(根据商品id查找回待支付订单的信息，进行支付操作)   
	 * 创建人：李泽兴 lizexing@163.com      
	 * 创建时间：2018年5月30日 上午11:03:46    
	 * 修改人：李泽兴 lizexing@163.com       
	 * 修改时间：2018年5月30日 上午11:03:46    
	 * 修改备注： 
	 * @param request
	 * @param response
	 * @param goodsId
	 * @return</pre>
	 */
	@RequestMapping("getRePay")
	public String getRePay(HttpServletRequest request,HttpServletResponse response,
			@RequestParam("goodsId")String goodsId) {
		/**
		 * 在这个方法里把订单表信息和wechat_order表信息查出来返回到前台
		 */
		String openid = (String) request.getSession().getAttribute("openid");
		String result = "";//orderService.getRePay(openid,goodsId);
		return result;
	}
	
	/**
	 * <pre>getWechatOrderStatus(根据商品id把商品的待支付状态拿回来，保证支付的幂等性)   
	 * 创建人：李泽兴 lizexing@163.com      
	 * 创建时间：2018年5月30日 上午10:51:53    
	 * 修改人：李泽兴 lizexing@163.com       
	 * 修改时间：2018年5月30日 上午10:51:53    
	 * 修改备注： 
	 * @param request
	 * @param response
	 * @param goodsId
	 * @return</pre>
	 */
	@RequestMapping("getWechatOrderStatus")
	public String getWechatOrderStatus(HttpServletRequest request,HttpServletResponse response,
			@RequestParam("goodsId")String goodsId) {
		
		String openid = (String) request.getSession().getAttribute("openid");
		//把order表和wecaht_order表的数据查出来，并且看看这个状态是否是已支付或未支付状态
		String result = "";//orderService.getOrderStatus(openid,goodsId);
		
		/**
		 * 假如说数据库是已支付状态，那我调用下微信接口，看看是否我的平台支付状态和微信平台的状态是否一致
		 * 如果我平台是未支付状态，微信那边是已支付状态，那我接收到微信的信息后，把我这边信息进行更改，更改为已支付状态
		 * 
		 * 查询订单接口：https://api.mch.weixin.qq.com/pay/orderquery
		 */
		
		return result;
	}
	
}
