package com.lizx.wechat.bean;



/**
 * 
 * <pre>项目名称：1511BWechatTest    
 * 类名称：Menu    
 * 类描述：    菜单实体类，对应菜单表
 * 创建人：李泽兴 lizexing@163.com    
 * 创建时间：2018年5月25日 下午7:36:00    
 * 修改人：李泽兴 lizexing@163.com       
 * 修改时间：2018年5月25日 下午7:36:00    
 * 修改备注：       
 * @version </pre>
 */
public class Menu {

	/****/
	private String id;
	/**菜单名称**/
	private String name;
	/**菜单类型**/
	private String type;
	/**菜单地址**/
	private String url;
	/****/
	private String key;
	/**父id，对应id**/
	private String pid;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}

}
