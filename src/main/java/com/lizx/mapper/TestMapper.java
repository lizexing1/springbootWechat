package com.lizx.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TestMapper {

	@Select("<script>select * from app where id = #{id}</script>")
	public List<Map<String, Object>> list(@Param("id")String id);
	
	@Insert("<script>sql语句</script>")
	public void add(@Param("id")String id);
	
	
}
