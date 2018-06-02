package com.lizx.wechat.service.impl;



import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.lizx.utils.CommonUtil;
import com.lizx.utils.HttpClientUtils;
import com.lizx.utils.Result;
import com.lizx.utils.WechatConfig;
import com.lizx.wechat.mapper.MenuMapper;
import com.lizx.wechat.service.MenuService;

import net.sf.json.JSONObject;

@Service
public class MenuServiceImpl implements MenuService{

	@Autowired
	private MenuMapper menuMapper;
	
	/**
	 * 调用接口，修改菜单按钮
	 */
	@Override
	public Result changeMenu() {
		
		//首先调用获取tocken地址
		//https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxe4e4c89c099df28b&secret=APPSECRET
		String getTockenUrl = WechatConfig.GET_TOCKEN_URL.replace("APPID", WechatConfig.APPID).replace("APPSECRET",  WechatConfig.APP_SECRECT);
		String tockenStr = HttpClientUtils.getMethod(getTockenUrl);
		System.out.println(tockenStr);
		JSONObject tockenJson = JSONObject.fromObject(tockenStr);
		String access_token = (String) tockenJson.get("access_token");
		if(access_token==null) {
			return null;
		}
		
		//获取access_token的地址，调用access_token地址
		String changeMenuURL = WechatConfig.CHANGE_MENU_URL.replace("ACCESS_TOKEN", access_token);
		
		String menuParam = "";
//		String menuParam = "{\"button\":[{\"type\":\"click\",\"name\":\"今日歌曲\",\"key\":\"V1001_TODAY_MUSIC\"},{\"name\":\"菜单\",\"sub_button\":[{\"type\":\"view\",\"name\":\"搜索\",\"url\":\"http://www.soso.com/\"},{\"type\":\"click\",\"name\":\"赞一下我们\",\"key\":\"V1001_GOOD\"}]}]}";
		
		JSONArray parentMenuArray = new JSONArray();
		
		//查询父节点菜单数据
		List<Map<String, Object>> parentMenuList = menuMapper.getByPid("0");
		
		for (int i = 0; i < parentMenuList.size(); i++) {
			
			Map<String, Object> menuMap = parentMenuList.get(i);
			JSONObject parentMenu = new JSONObject();
			
			//如果等于null说明它的下面还有子节点
			if(menuMap.get("type")==null || menuMap.get("type").equals("")) {
				
				
				JSONArray childArray = new JSONArray();
				
				List<Map<String, Object>> childMenuList = menuMapper.getByPid(menuMap.get("id").toString());
				for (int j = 0; j < childMenuList.size(); j++) {
					Map<String, Object> map = childMenuList.get(j);
					
					JSONObject childMenu = new JSONObject();
					
					childMenu.put("type", map.get("type"));
					childMenu.put("name", map.get("name"));
					
					if(map.get("type").equals("click")) {
						childMenu.put("key", map.get("key"));
					}
					
					if(map.get("type").equals("view")) {
						childMenu.put("url", map.get("url"));
					}
					
					if(map.get("type").equals("scancode_push")) {
					}
					
					if(map.get("type").equals("scancode_waitmsg")) {
					}
					
					if(map.get("type").equals("pic_sysphoto")) {
					}
					if(map.get("type").equals("pic_weixin")) {
					}
					
					childArray.add(childMenu);
				}
				
				parentMenu.put("name", menuMap.get("name"));
				parentMenu.put("sub_button", childArray);
			}
			
			parentMenu.put("type", menuMap.get("type"));
			parentMenu.put("name", menuMap.get("name"));
			
			if(menuMap.get("type").equals("click")) {
				parentMenu.put("key", menuMap.get("key"));
			}
			
			if(menuMap.get("type").equals("view")) {
				parentMenu.put("url", menuMap.get("url"));
			}
			
			if(menuMap.get("type").equals("scancode_push")) {
				
			}
			
			if(menuMap.get("type").equals("scancode_waitmsg")) {
			}
			
			if(menuMap.get("type").equals("pic_sysphoto")) {
			}
			if(menuMap.get("type").equals("pic_weixin")) {
			}
			
			parentMenuArray.add(parentMenu);
		}
		
		
		JSONObject parentMenuJson = new JSONObject();
		parentMenuJson.put("button", parentMenuArray);
		System.out.println(parentMenuJson.toString());
		
		/**
		 * 这是测试代码开始
		 */
//		String menuParam = "";
		//************************菜单1开始******************************//
//		JSONObject parentMenu1 = new JSONObject();
//		parentMenu1.put("type", "click");
//		parentMenu1.put("name", "今日歌曲");
//		parentMenu1.put("key", "V1001_TODAY_MUSIC");
		//************************菜单1结束******************************//
		
		//************************菜单2开始******************************//
		
//		JSONObject childMenu1 = new JSONObject();
//		childMenu1.put("type", "view");
//		childMenu1.put("name", "搜索");
//		childMenu1.put("url", "http://www.soso.com/");
//		
//		JSONObject childMenu2 = new JSONObject();
//		childMenu2.put("type", "click");
//		childMenu2.put("name", "搜索");
//		childMenu2.put("url", "V1001_GOOD");
//		
//		JSONArray parentMenu2Array = new JSONArray();
//		parentMenu2Array.add(childMenu1);
//		parentMenu2Array.add(childMenu2);
//		
//		JSONObject parentMenu2 = new JSONObject();
//		parentMenu2.put("name", "菜单");
//		parentMenu2.put("sub_button", parentMenu2Array);
		
		//************************菜单2开始******************************//
		
//		JSONArray parentMenuArry = new JSONArray();
//		parentMenuArry.add(parentMenu1);
//		parentMenuArry.add(parentMenu2);
//		
//		JSONObject parentMenuJson = new JSONObject();
//		parentMenuJson.put("button", parentMenuArry);
		/**
		 * 这是测试代码结束
		 */
		
		
		JSONObject changeMenuResult = CommonUtil.httpsRequest(changeMenuURL, "POST", parentMenuJson.toString());
		System.out.println(changeMenuResult);
		
		return null;
	}
	
	
	
	/**
	 * 查询按钮表的内容列表
	 */
	@Override
	public Result list() {
		
		List<Map<String, Object>> list = menuMapper.getByPid("0");
		
		for (int i = 0; i < list.size(); i++) {
			
			Map<String, Object> map = list.get(i);
			String id = (String) map.get("id");
			
			List<Map<String, Object>> childList = menuMapper.getByPid(id);
			list.get(i).put("childList", childList);
		}
		
		Result result = new Result();
		result.put("list", list);
		return result;
	}

}
