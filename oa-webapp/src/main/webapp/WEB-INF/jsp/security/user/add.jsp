<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>用户添加</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<%@ include file="/common/include-jquery-easyui.jsp"%>
<%@ include file="/common/include-bootstap.jsp"%>
<%@ include file="/common/include-jquery-validation.jsp"%>
<%@ include file="/common/include-jquery-kindeditor.jsp"%>
<script type="text/javascript" src="${ctx }/js/edit2Editor.js"></script>
<%@ include file="/common/include-styles.jsp"%>
</head>
<script type="text/javascript">
	$(function() {
		$("#deptId").combotree({
			url : ctx + "/security/dept!getAllDept.action",
			id : "id",
			lines : true,
			width : 550
		});
		$(".panel .combo-p").css("width", '362px');
	})
</script>
<body class="editBody">
	<button class="btn btn-info btn-sm pull-left"    onclick="javascript:location.href='${ctx}/security/user!findPage.action'">
		<span class="glyphicon glyphicon-backward"></span> 返回列表
	</button>
	<div class="btn-group pull-right btn-group-sm">
		<button type="button" class="btn btn-info" id="saveButton">
			<span class="glyphicon glyphicon-ok"></span> 保存
		</button>
		<button type="button" class="btn btn-info" id="resetButton">
			<span class="glyphicon glyphicon-repeat"></span> 重置
		</button>
	</div>
	<div class="clearfix" style="margin-bottom: 20px;"></div>
	<form action="${ctx}/security/user!save.action" method="post"
		id="editForm">
		<table class="formTable table">
			<tr>
				<Td class="control-label" style="width: 3%"><label for="userName">登录名：</label></Td>
				<Td class="query_input" colspan="3"><input type="text" name="userName"
					placeholder="请输入登陆名" class="form-control validate[required]"
					id="userName"></Td>
				
			</tr>
			<tr>
				<Td class="control-label"><label for="pwd">密码：</label></Td>
				<Td class="query_input"><input type="password" name="password"
					placeholder="请输入密码" class="form-control validate[required]"
					id="pwd"></Td>
				<Td class="control-label"><label for="confirmPwd">确认密码：</label></Td>
				<Td class="query_input"><input type="password" name="confirmPwd"
					placeholder="请输入确认密码" class="form-control validate[required,equals[pwd]]"
					id="confirmPwd"></Td>
			</tr>
			<tr>
				<Td class="control-label"><label for="realName">真实姓名：</label></Td>
				<Td class="query_input"><s:textfield name="realName"
						placeholder="请输入真实姓名" cssClass="form-control validate[required]"
						id="realName"></s:textfield></Td>
				<Td class="control-label"><label for="deptId">所属部门：</label></Td>
				<Td class="query_input"><select name="dept.id" id="deptId"
					style="width: 250px;" class="validate[required]">
				</select></Td>
			</tr>
			<tr>
				<Td class="control-label"><label for="desc">描述：</label></Td>
				<Td class="query_input" colspan="3"><s:textarea name="desc"></s:textarea>
				</Td>
			</tr>
		</table>
	</form>
</body>
<style>
.combo {
	background-color: #FFFFFF;
	border: 1px solid #CCCCCC;
	border-radius: 4px;
	box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
	color: #555555;
	display: block;
	font-size: 12px;
	line-height: 1.42857;
	padding: 0px;
	transition: border-color 0.15s ease-in-out 0s, box-shadow 0.15s
		ease-in-out 0s;
	vertical-align: middle;
	width: 100%;
}

.panel .combo-p {
	width: 362px;
}

.combo-value {
	border: 1px solid #CCCCCC;
}
</style>

</html>
