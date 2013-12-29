<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>详情</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<%@ include file="/common/include-bootstap.jsp"%>
<script src="${ctx }/js/grid.js"></script>
<%@ include file="/common/include-styles.jsp"%>
</head>
<body class="editBody">
	<button class="btn btn-info btn-sm pull-left" id="backButton">
		<span class="glyphicon glyphicon-backward"></span> 返回列表
	</button>
	<div class="clearfix" style="margin-bottom: 20px;"></div>
	<form method="post" id="deleteForm">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>时间</th>
					<th>子项名称</th>
					<th colspan="2">子项收入&nbsp;<font color="green">(万元)</font></th>
				</tr>
			</thead>
			<tbody>
				<tr align="center">
					<Td></Td>
					<Td></Td>
					<td>本年</td>
					<td>上一年</td>
				</tr>
				<c:forEach items="${sameCompareDetaiList }" var="d">
					<tr>
						<td>${d.time}</td>
						<td>${d.name }</td>
						<Td>${d.nowMoney }</Td>
						<Td>${d.lastMoney }</Td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form>
</body>
</html>
