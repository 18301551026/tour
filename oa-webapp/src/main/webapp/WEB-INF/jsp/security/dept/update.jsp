<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/common/global.jsp"%>
<%@ include file="/common/meta.jsp"%>
<title>编辑节点</title>
<%@ include file="/common/include-jquery.jsp"%>
<%@ include file="/common/include-jquery-easyui.jsp"%>

</head>
<body>
<style type="text/css">
.hidd {
	display: none;
}
</style>
	<script type="text/javascript" src="${ctx }/js/dept-update.js"></script>
	<form id="updateDeptForm" method="post" style="margin: 10px;"
		role="form">
		<s:hidden name="id"></s:hidden>
		<label>名称：</label>
		<s:textfield name="text" cssClass="easyui-validatebox form-control"
			data-options="required:true" placeholder="请输入名称"></s:textfield>
		<c:if test="${deptLevel=='企业' }">
			<br class="t" />
			<label class="t">类型：</label>
			<s:select list="allFactoryType" onchange="isOperate()"  id='allFactoryType' name="factoryType.id" cssClass="form-control" listKey="id" listValue="name"></s:select>
			<Br class='op hidd' />
			<label class='op hidd'>是否经营：</label>
			<input class='op hidd' type="radio"
				<c:if test="${operate }">
				 checked="checked"
				</c:if>
			 name="operate" value="true"><font  class='op hidd'>是&nbsp;</font>
			<input class='op hidd' type="radio"
				<c:if test="${!operate }">
					 checked="checked"
				</c:if>
			 name="operate" value="false"><font class='op hidd'>否</font>
			 <Br class='op hidd' />
		</c:if>
		<br /> <label>描述：</label>
		<s:textarea name="deptDesc" cssClass="form-control"
			cssStyle="height:80px"></s:textarea>
	</form>
	<style>
.form-control {
	background-color: #FFFFFF;
	border: 1px solid #CCCCCC;
	border-radius: 4px;
	box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
	color: #555555;
	display: block;
	font-size: 12px;
	height: 25px;
	line-height: 1.42857;
	padding: 2px;
	transition: border-color 0.15s ease-in-out 0s, box-shadow 0.15s
		ease-in-out 0s;
	vertical-align: middle;
	line-height: 1.42857;
	padding: 2px;
	transition: border-color 0.15s ease-in-out 0s, box-shadow 0.15s
		ease-in-out 0s;
	vertical-align: middle;
	width: 67%;
	padding: 2px;
	transition: border-color 0.15s ease-in-out 0s, box-shadow 0.15s
		ease-in-out 0s;
	vertical-align: middle;
	line-height: 1.42857;
	padding: 2px;
	transition: border-color 0.15s ease-in-out 0s, box-shadow 0.15s
		ease-in-out 0s;
	vertical-align: middle;
	transition: border-color 0.15s ease-in-out 0s, box-shadow 0.15s
		ease-in-out 0s;
	vertical-align: middle;
	line-height: 1.42857;
	padding: 2px;
	transition: border-color 0.15s ease-in-out 0s, box-shadow 0.15s
		ease-in-out 0s;
	vertical-align: middle;
}

label {
	height: 30px;
	line-height: 30px;
	vertical-align: middle;
	float: left;
	text-align: right;
	width: 80px;
}
</style>
</body>
</html>