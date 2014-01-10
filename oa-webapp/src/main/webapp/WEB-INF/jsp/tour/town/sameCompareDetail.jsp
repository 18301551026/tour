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
<script type="text/javascript">
	$(function(){
		$("#reportHtmlButton").click(function(){
			$("#sameCompareDetailChartForm").attr("action",ctx+"/tour/detailChart!sameCompareDetailToHtml.action");
			$("#sameCompareDetailChartForm").submit();
		})
		$("#exportWordButton").click(function(){
			$("#sameCompareDetailChartForm").attr("action",ctx+"/tour/detailChart!sameCompareDetailToWord.action");
			$("#sameCompareDetailChartForm").submit();
		})
	})
</script>
<body class="editBody">
	<div class="btn-group btn-group-sm">
		<button class="btn btn-info btn-sm pull-left" onclick="javascript:location.href='${ctx}/tour/townSameCompare!townSameCompare.action'">
			<span class="glyphicon glyphicon-backward"></span> 返回列表
		</button>
		<button id="reportHtmlButton" class="btn btn-info">
			<span class="glyphicon glyphicon-print"></span> html查看
		</button>
		<button id="exportWordButton" class="btn btn-info">
			<span class="glyphicon glyphicon-print"></span> 导出word
		</button>
	</div>
	<s:form action="" method="post" id="sameCompareDetailChartForm" target="_blank">
		<s:hidden name="nowIds"></s:hidden>
		<s:hidden name="lastIds"></s:hidden>
		<s:hidden name="tempReportDate"></s:hidden>
	</s:form>
	<div class="clearfix" style="margin-bottom: 20px;"></div>
	<form method="post" id="deleteForm" >
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>时间</th>
					<th>子项名称</th>
					<th colspan="3">子项收入&nbsp;<font color="green">(万元)</font></th>
				</tr>
			</thead>
			<tbody>
				<tr align="center">
					<Td></Td>
					<Td></Td>
					<td>本年</td>
					<td>上一年</td>
					<td>百分比</td>
				</tr>
				<c:forEach items="${sameCompareDetaiList }" var="d">
					<tr>
						<td>${d.time}</td>
						<td>${d.name }</td>
						<Td>${d.nowMoney }</Td>
						<Td>${d.lastMoney }</Td>
						<td>
							
							<c:if test="${fn:startsWith(d.percent, '-') }">
								<font color="red">${d.percent }%</font>
							</c:if>
							<c:if test="${!fn:startsWith(d.percent, '-') }">
								<font color="green">${d.percent }%</font>
							</c:if>
							
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form>
</body>
</html>
