package com.lizx.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class HttpClientUtils {

	
	public static void main(String[] args) {
		String method = getMethod("http://comp.umetrip.com/UmeSDK/FlightStatus/GetFlightStatusByFlightNo.do");
		System.out.println(method);
	}
	
	public static String getMethod(String url) {
		 CloseableHttpClient httpclient = new DefaultHttpClient(); 
		 
		 String access_token ="";
		 String content = "";
        try {
			// 创建http请求(get方式)  
			HttpGet httpget = new HttpGet(url);  
			CloseableHttpResponse response = httpclient.execute(httpget);  
			try {  
			    System.out.println("----------------------------------------");  
			    System.out.println(response.getStatusLine());  
			     // 获取服务端响应的数据  
	                content = EntityUtils.toString(response.getEntity(),  
	                        "UTF-8");  
			} finally {  
					response.close();
					httpclient.close(); 
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return content;
	}
	
	public static String postMethod(String url,List formparams) {
		 CloseableHttpClient httpclient = new DefaultHttpClient(); 
        // 创建httppost    
        HttpPost httppost = new HttpPost(url);  
        UrlEncodedFormEntity uefEntity;  
        String content = "";
        try {  
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
            httppost.setEntity(uefEntity);  
            System.out.println("executing request " + httppost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httppost);  
            try {  
                    System.out.println("--------------------------------------");  
                    content = EntityUtils.toString(response.getEntity(),  
	                        "UTF-8");  
                    System.out.println("--------------------------------------");  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
		return content;
	}
	
	
}
