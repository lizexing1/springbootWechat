package com.lizx.wechat.service;



import com.lizx.utils.Result;

public interface MenuService {

	/**
	 * <pre>changeMenu(调用接口，修改菜单按钮)   
	 * 创建人：李泽兴 lizexing@163.com      
	 * 创建时间：2018年5月25日 下午7:48:51    
	 * 修改人：李泽兴 lizexing@163.com       
	 * 修改时间：2018年5月25日 下午7:48:51    
	 * 修改备注： 
	 * @return</pre>
	 */
	Result changeMenu();
	/**
	 * <pre>list(查询按钮表的内容列表)   
	 * 创建人：李泽兴 lizexing@163.com      
	 * 创建时间：2018年5月25日 下午11:08:36    
	 * 修改人：李泽兴 lizexing@163.com       
	 * 修改时间：2018年5月25日 下午11:08:36    
	 * 修改备注： 
	 * @return</pre>
	 */
	Result list();

}
