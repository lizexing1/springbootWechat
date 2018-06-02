package com.lizx.wechat.bean.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * <pre>项目名称：efilm    
 * 类名称：TextImgMessage    
 * 类描述：    关注完成微信后推送模板信息
 * 创建人：李泽兴 lizexing@huiyihuiying.com    
 * 创建时间：2017年6月8日 下午4:43:40    
 * 修改人：李泽兴 lizexing@huiyihuiying.com
 * 修改时间：2017年6月8日 下午4:43:40    
 * 修改备注：       
 * @version </pre>
 */
public class TextImgMessage extends BaseMessage{
	
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	
	public List<Map<String, Object>> getList() {
		return list;
	}
	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
	
	public String toXML() {
		
		StringBuffer xml = new StringBuffer();
		xml.append("<xml>");
		xml.append(super.toXML());
		
		xml.append("<ArticleCount>"+list.size()+"</ArticleCount>");
		xml.append("<Articles>");
		
		for (int i = 0; i < list.size(); i++) {
			
			String Title= (String) list.get(i).get("Title");
			String PicUrl= (String) list.get(i).get("PicUrl");
			String Url= (String) list.get(i).get("Url");
			String Description= (String) list.get(i).get("Description");
			
			xml.append("<item>");
			xml.append("<Title><![CDATA["+Title+"]]></Title>");
			xml.append("<Description><![CDATA["+Description+"]]></Description>");
			xml.append("<PicUrl><![CDATA["+PicUrl+"]]></PicUrl>");
			xml.append("<Url><![CDATA["+Url+"]]></Url>");
			xml.append("</item>");
		}
		
		xml.append("</Articles>");
		xml.append("</xml>");
		
		System.out.println("**********************");
		System.out.println(xml.toString());
		System.out.println("**********************");
		return xml.toString();
	}
	
	
	
}
