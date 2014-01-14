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
			placeholder="请输入名称" /><br />
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
		<s:select list="allFactoryType" onchange="isOperate()" id="allFactoryType" name="factoryType.id" cssClass="form-control" listKey="id" listValue="name"></s:select>
		<Br/>
		</c:if>
		<label class='op hidd'>是否经营：</label>
		<input class='op hidd' type="radio" checked="checked" name="operate" value="true"><font  class='op hidd'>是&nbsp;</font>
		<input class='op hidd' type="radio" name="operate" value="false"><font class='op hidd'>否</font>
		<br class="op hidd" />
		<br class="op hidd" />
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