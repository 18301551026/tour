<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/common/global.jsp"%>
<%@ include file="/common/meta.jsp"%>
<title>添加子节点</title>
<%@ include file="/common/include-jquery.jsp"%>
<%@ include file="/common/include-jquery-easyui.jsp"%>

</head>
<body>
	<style type="text/css">
.hidd {
	display: none;
}
</style>
	<script type="text/javascript" src="${ctx }/js/dept-add.js"></script>
	<form id="addDeptForm" method="post" style="margin: 10px;" role="form">
		<s:hidden name="pid" value="%{id}"></s:hidden>
		<label>名称：</label><input name="text" type="text"
			class="easyui-validatebox form-control" data-options="required:true"
			placeholder="请输入名称" /><br /><!--  <label>级别：</label> <select
			id="deptLevel" name="deptLevel" id="deptLevel" class="form-control">
			<option value="区级"selected="selected">区级</option>
			<option value="镇级">镇级</option>
			<option value="企业" >企业</option>
		</select><br /> -->
		<input type="hidden" name="deptLevel"
		<c:if test="${tempLevel==0 }">
			 value="区级"
		</c:if>
		<c:if test="${tempLevel==1 }">
			 value="镇级"
		</c:if>
		<c:if test="${tempLevel==2 }">
			 value="企业"
		</c:if>
		/>
		<c:if test="${tempLevel==2  }">
		<label >类型：</label>
		 <select name="deptType" class="form-control">
				<option value="观光园">观光园</option>
				<option value="民俗旅游">民俗旅游</option>
				<option value="旅游住宿">旅游住宿</option>
				<option value="旅游风景">旅游风景</option>
				<option value="工业旅游">工业旅游</option>
		</select>
		<Br/>
		</c:if>
		 <label>描述：</label>
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
	height: 25px; line-height : 1.42857; padding : 2px;
	transition: border-color 0.15s ease-in-out 0s, box-shadow 0.15s
		ease-in-out 0s;
	vertical-align: middle;
	line-height: 1.42857;
	padding: 2px; transition : border-color 0.15s ease-in-out 0s,
	box-shadow 0.15s ease-in-out 0s; vertical-align : middle;
	width: 67%;
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