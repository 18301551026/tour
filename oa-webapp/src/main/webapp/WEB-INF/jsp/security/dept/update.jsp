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
	<script type="text/javascript" src="${ctx }/js/dept-update.js"></script>
	<form id="updateDeptForm" method="post" style="margin: 10px;"
		role="form">
		<s:hidden name="id"></s:hidden>
		<label>名称：</label>
		<s:textfield name="text" cssClass="easyui-validatebox form-control"
			data-options="required:true" placeholder="请输入名称"></s:textfield>
		<%--<br />  <label>级别：</label>
		<s:select list="#{'区级':'区级','镇级':'镇级','企业':'企业' }" name="deptLevel"
			id="deptLevel" cssClass="form-control"></s:select> --%>
		<c:if test="${deptLevel=='企业' }">
			<br class="t" />
			<label class="t">类型：</label>
			<s:select cssClass="form-control t"
				list="#{'观光园':'观光园','民俗旅游':'民俗旅游','工业旅游':'工业旅游','旅游住宿':'旅游住宿','风景旅游':'风景旅游' }"
				name="deptType"></s:select>
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