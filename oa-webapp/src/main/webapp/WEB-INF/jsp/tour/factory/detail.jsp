<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>详情</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<%@ include file="/common/include-jquery-validation.jsp"%>
<script type="text/javascript" src="${ctx }/js/edit.js"></script>
<%@ include file="/common/include-bootstap.jsp"%>
<%@ include file="/common/include-styles.jsp"%>
<style type="text/css">
.form-control {
	background-color: #FFFFFF;
	border: 1px solid #CCCCCC;
	border-radius: 4px;
	box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
	color: #555555;
	display: inline;
	font-size: 12px;
	height: 30px;
	line-height: 1.42857;
	padding: 6px 12px;
	transition: border-color 0.15s ease-in-out 0s, box-shadow 0.15s
		ease-in-out 0s;
	vertical-align: middle;
	width: 95%;
}

font {
	color: green;
	width: 20px;
	display: inline;
}
</style>
</head>
<body class="editBody">
	<button class="btn btn-info btn-sm pull-left" id="backButton">
		<span class="glyphicon glyphicon-backward"></span> 返回列表
	</button>
	<div class="clearfix" style="margin-bottom: 20px;"></div>
	<form action="${ctx}/tour/noReported!save.action" method="post"
		id="editForm">
		<table class="formTable table">
			<c:forEach items="${details }" var="d">
				<Tr>
					<Td class="control-label" style="width: 3%"><label
						for="voteOptions">${d.name }：</label></Td>
					<Td class="query_input" colspan="3"><input type="text"
						readonly="readonly"
						class="form-control validate[required] pull-left"
						value="${d.money }"> <font>(万元)</font>
				</Tr>
			</c:forEach>
			<tr>
				<Td class="control-label"><label for="desc">备注：</label>
				<Td class="query_input" colspan="3">
					<div class="readyonlyTextarea">${desc }</div>
				</Td>
			</tr>
		</table>
	</form>
</body>
</html>
