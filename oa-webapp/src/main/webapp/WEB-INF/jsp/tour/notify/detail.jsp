<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>推送详情</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<%@ include file="/common/include-bootstap.jsp"%>
<%@ include file="/common/include-styles.jsp"%>
</head>
<body class="editBody">
	<button class="btn btn-info btn-sm pull-left"   onclick="javascript:history.back()">
		<span class="glyphicon glyphicon-backward"></span> 返回列表
	</button>
	<div class="clearfix" style="margin-bottom: 20px;"></div>
	<form action="" method="post"
		id="editForm">
		<table class="formTable table">
			<tr>
				<Td class="control-label"style="width: 3%"><label
					for="userNames">接收企业：</label></Td>
				<Td class="query_input" colspan="3">
				<s:textfield name="userNames" readonly="true" cssClass="form-control"></s:textfield>
				</Td>
			</tr>
			<tr>
				<Td class="control-label"  ><label for="title">标题：</label></Td>
				<Td class="query_input"><s:textfield name="title"
						readonly="true" cssClass="form-control validate[required]"
						id="title"></s:textfield></Td>
				<Td class="control-label"  ><label for="createDate">发送时间：</label></Td>
				<Td class="query_input"><s:textfield name="createDate" readonly="true"
					 cssClass="form-control"
						id="createDate"></s:textfield></Td>
			</tr>
			<tr>
				<Td class="control-label"><label for="desc">描述：</label></Td>
				<Td class="query_input" colspan="3"><s:textarea readonly="true" cssClass="readyonlyTextarea" name="content"></s:textarea>
				</Td>
			</tr>
		</table>
	</form>
</body>
</html>
