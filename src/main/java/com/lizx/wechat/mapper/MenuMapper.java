package com.lizx.wechat.mapper;



import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MenuMapper {

	/**
	 * <pre>list(根据父id查询出当前数据集合)   
	 * 创建人：李泽兴 lizexing@163.com      
	 * 创建时间：2018年5月26日 下午2:44:54    
	 * 修改人：李泽兴 lizexing@163.com       
	 * 修改时间：2018年5月26日 下午2:44:54    
	 * 修改备注： 
	 * @param id
	 * @return</pre>
	 */
	@Select("select * from menu m where pid = #{id}")
	public List<Map<String, Object>> getByPid(@Param("id")String id);

}
