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
<body class="editBody">
<script type="text/javascript">
$(function() {
	$("#selectReceiveUsersButton")
			.click(
					function() {
						parent.$
						.modalDialog({
							title : "选择收件人",
							width : 300,
							height : 500,
							href : ctx
									+ "/tour/notify!toSelectReceiveUsers.action",
							buttons : [
									{
										text : '全体人员',
										handler : function() {
											$("#ids").val(0);
											$("#userNames").val(
													"全体人员");
											parent.$.modalDialog.handler
													.dialog('close');
										}
									},
									{
										text : '确定',
										iconCls : 'icon-save',
										handler : function() {
											var ids = parent.$.modalDialog.handler
													.find('#ids');
											var receivedUserNames = parent.$.modalDialog.handler
													.find('#receiveUsersName');
											$("#userNames").val(
													receivedUserNames
															.val());
											$("#ids").val(ids.val());
											parent.$.modalDialog.handler
													.dialog('close');
										}
									} ]
						});
					})
})	
</script>
	<button class="btn btn-info btn-sm pull-left" id="backButton" onclick="javascript:history.back()">
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
	<form action="${ctx}/tour/notify!save.action" method="post"
		id="editForm">
		<s:hidden name="receiveIds" id="ids"></s:hidden>
		<table class="formTable table">
			<tr>
				<Td class="control-label"style="width: 3%"><label
					for="userNames">接收企业：</label></Td>
				<Td class="query_input" colspan="3"><input
					name="userNames" placeholder="请选择接企业" readonly="readonly"
					class="form-control pull-left validate[required]"
					style="width: 95%;" id="userNames" />
					<button class="btn btn-info btn-xs pull-right"
						id="selectReceiveUsersButton" style="margin-top: 2px;">选择</button></Td>
			</tr>
			<tr>
				<Td class="control-label"  width="3%" style="width: 3%"><label for="title">标题：</label></Td>
				<Td class="query_input" colspan="3"><s:textfield name="title"
						placeholder="请输入标题" cssClass="form-control validate[required]"
						id="title"></s:textfield></Td>
			</tr>
			<tr>
				<Td class="control-label"><label for="desc">描述：</label></Td>
				<Td class="query_input" colspan="3"><s:textarea name="desc"></s:textarea>
				</Td>
			</tr>
		</table>
	</form>
</body>
</html>
