package com.lizx.utils;

import java.util.HashMap;
import java.util.Map;

public class Result {

	/**返回成功**/
	public static final String STATUS_SUCCESS = "0";
	
	/**
	 * 100000指的是mongodb的错误
	 */
	/**mongodb插入错误**/
	public static final String STATUS_MONGODB_INSERT_ERROR = "100000";
	/**mongodb查询错误**/
	public static final String STATUS_MONGODB_SEARCH_ERROR = "100001";
	
	/**
	 * 101000用户表操作的错误
	 */
	/**mongodb用户信息错误**/
	public static final String STATUS_USER_INSERT_ERROR = "101000";
	
	
	/**返回状态**/
	private String status;
	/**错误原因**/
	private String message;
	/**返回的数据**/
	private Map<String,Object> data = new HashMap<String, Object>();
	
	public Result() {
		super();
		this.status = STATUS_SUCCESS;
		this.message = null;
		this.data = new HashMap<String, Object>();
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	/**
	 * <pre>put(用于专门防止请求成功后返回的查询后的数据)   
	 * 创建人：李泽兴 lizexing@163.com      
	 * 创建时间：2018年5月25日 下午3:47:25    
	 * 修改人：李泽兴 lizexing@163.com       
	 * 修改时间：2018年5月25日 下午3:47:25    
	 * 修改备注： 
	 * @param key
	 * @param value</pre>
	 */
	public void put(String key,Object value){
		this.data.put(key, value);
	}
	
	
}
