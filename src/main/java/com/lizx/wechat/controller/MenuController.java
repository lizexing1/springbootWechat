package com.lizx.wechat.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lizx.utils.Result;
import com.lizx.wechat.service.MenuService;

@Controller
@RequestMapping("menu")
public class MenuController {

	@Autowired
	private MenuService menuService;
	
	/**
	 * <pre>changeMenu(调用接口，修改按钮信息)   
	 * 创建人：李泽兴 lizexing@163.com      
	 * 创建时间：2018年5月25日 下午7:48:34    
	 * 修改人：李泽兴 lizexing@163.com       
	 * 修改时间：2018年5月25日 下午7:48:34    
	 * 修改备注： 
	 * @return</pre>
	 */
	@RequestMapping("changeMenu")
	public Result changeMenu() {
		Result result = menuService.changeMenu();
		return result;
	}
	/**
	 * <pre>list(查询按钮表的内容列表)   
	 * 创建人：李泽兴 lizexing@163.com      
	 * 创建时间：2018年5月25日 下午11:06:54    
	 * 修改人：李泽兴 lizexing@163.com       
	 * 修改时间：2018年5月25日 下午11:06:54    
	 * 修改备注： 
	 * @return</pre>
	 */
	@RequestMapping("list")
	public String list(Model model) {
		Result result = menuService.list();
		model.addAttribute("list", result);
		return "menu/list";
	}
	
}
