<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>用户修改</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<%@ include file="/common/include-jquery-easyui.jsp"%>
<script type="text/javascript" src="${ctx }/js/edit.js"></script>
<%@ include file="/common/include-bootstap.jsp"%>
<%@ include file="/common/include-jquery-validation.jsp"%>
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
	})
	function checkUser(field, rules, i, options){
			var userName = field.val();
			var tempUserName=$("#tempUserName").val();
			var result;
			if(userName!=tempUserName){
				if (userName) {
					$.ajax({
							type : "POST",
							async: false,
							url : ctx
									+ "/security/user!checkUserIsRepeat.action",
							data : "userName="
									+ userName,
							success : function(msg) {
								if (msg == '存在') {
									options.allrules.validate2fields.alertText="用户名"+ userName+ "已经存在了，请更换用户名"
									result= options.allrules.validate2fields.alertText;
								}
						}
							});
				}
			}else{
				return true;
			}
			return result;
		}
</script>
<body class="editBody">
	<s:hidden name="userName" id="tempUserName"></s:hidden>
	<button class="btn btn-info btn-sm pull-left"    onclick="javascript:location.href='${ctx}/security/user!findPage.action'">
		<span class="glyphicon glyphicon-backward"></span> 返回列表
	</button>
	<div class="btn-group pull-right btn-group-sm">
		<button type="button" class="btn btn-info" id="saveButton">
			<span class="glyphicon glyphicon-ok"></span> 保存
		</button>
		<button type="button" class="btn btn-info"
			actionUrl="${ctx }/security/user!toUpdate.action?id=${id}"
			id="resetButton">
			<span class="glyphicon glyphicon-repeat"></span> 重置
		</button>
	</div>
	<div class="clearfix" style="margin-bottom: 20px;"></div>
	<form action="${ctx}/security/user!save.action" method="post"
		id="editForm">
		<s:hidden name="id"></s:hidden>
		<table class="formTable table">
			<tr>
				<Td class="control-label"><label for="userName">登录名：</label></Td>
				<Td class="query_input"><s:textfield name="userName"
						placeholder="请输入登陆名" cssClass="form-control validate[required,funcCall[checkUser]]"
						id="userName"></s:textfield></Td>
				<Td class="control-label"><label for="pwd">密码：</label></Td>
				<Td class="query_input"><s:password name="password"
						placeholder="请输入密码" cssClass="form-control validate[required]"
						id="pwd"></s:password></Td>
			</tr>
			<tr>
				<Td class="control-label"><label for="realName">真实姓名：</label></Td>
				<Td class="query_input"><s:textfield name="realName"
						placeholder="请输入真实姓名" cssClass="form-control validate[required]"
						id="realName"></s:textfield></Td>
				<Td class="control-label"><label for="deptId">所属部门：</label></Td>
				<Td class="query_input"><s:textfield name="dept.id" id="deptId">

					</s:textfield></Td>
			</tr>
		</table>
	</form>
	<table style="width:100%; margin-left: 1px; float: left;"
		class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>角色名称</th>
				<th width="1%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${roles}" var="o">
				<tr>
					<td>${o.roleName }</td>
					<td><input type="button" class="btn btn-info btn-xs "
						value="删除"
						onclick="location.href='${ctx }/security/user!deleteRole.action?id=${id }&roleId=${o.id }'">
					</td>
				</tr>
			</c:forEach>
			<s:if test="#roleList.size>0">
				<form action="${ctx }/security/user!addRole.action" method="post">
					<s:hidden name="id"></s:hidden>
					<tr>
						<td><s:select name="roleId" list="#roleList" listKey="id"
								listValue="roleName" cssClass="form-control-mini"></s:select></td>
						<td><input type="submit" class="btn btn-info btn-xs "
							value="保存"></td>
					</tr>
				</form>
			</s:if>
		</tbody>
	</table>
	<%-- <table style="width: 50%; margin-left: 1px; float: right;"
		class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>职位名称</th>
				<th width="1%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${jobs}" var="o">
				<tr>
					<td>${o.jobName }</td>
					<td><input type="button" class="btn btn-info btn-xs "
						value="删除"
						onclick="location.href='${ctx }/security/user!deleteJob.action?id=${id }&jobId=${o.id }'">
					</td>
				</tr>
			</c:forEach>
			<s:if test="#jobList.size>0">
				<form action="${ctx }/security/user!addJob.action" method="post">
					<s:hidden name="id"></s:hidden>
					<tr>
						<td><s:select name="jobId" cssClass="form-control-mini"
								list="#jobList" listKey="id" listValue="jobName"></s:select></td>
						<td><input type="submit" class="btn btn-info btn-xs"
							value="保存"></td>
					</tr>
				</form>
			</s:if>
		</tbody>
	</table> --%>
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
	margin-left: 6px;
}

.combo-value {
	border: 1px solid #CCCCCC;
}
</style>
</body>
</html>
