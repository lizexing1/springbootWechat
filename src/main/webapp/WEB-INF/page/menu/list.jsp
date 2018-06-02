<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>


		<input type="button" onclick="changeMenu()" value="修改按钮" />
		<br/>

		<!-- 微信菜单按钮只准三个，如果超过三个只允许修改或删除，不允许添加 -->
		<c:if test="${list.data.list.size()>2}">
			<input type="button" onclick="add()" value="添加一级菜单" disabled="true"/>
		</c:if>
		
		<c:if test="${list.data.list.size()<=2}">
			<input type="button" onclick="add()" value="添加一级菜单" />
			
		</c:if>

	<table border="1">
		<tr>
			<td>一级菜单</td>
			<td>按钮名称</td>
			<td>操作</td>
		</tr>
		
		
		
		<c:forEach items="${list.data.list}" var="model" >
			<tr>
				<td>${model.pid}</td>
				<td>${model.name}</td>
				<td>
					
					<input type="button" onclick="edit(${model.id})" value="修改" />
					<input type="button" onclick="delete(${model.id})" value="删除" />
				</td>
			</tr>
			
			<c:forEach items="${model.childList}" var="childModel" >
				<tr>
					<td>${childModel.pid}</td>
					<td>${childModel.name}</td>
					<td>
						<input type="button" onclick="edit(${childModel.id})" value="修改" />
						<input type="button" onclick="delete(${childModel.id})" value="删除" />
					</td>
				</tr>
			</c:forEach>
			
		</c:forEach>
	</table>

</body>
</html>