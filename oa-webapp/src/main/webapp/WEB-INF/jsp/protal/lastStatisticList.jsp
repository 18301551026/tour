<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>镇政府统计</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<script type="text/javascript"
	src="${ctx }/js/My97DatePicker/WdatePicker.js"></script>
<%@ include file="/common/include-bootstap.jsp"%>
<script src="${ctx }/js/grid.js"></script>
<%@ include file="/common/include-styles.jsp"%>
</head>
<body>
<style type="text/css">
.table {
	margin-bottom: 4px;
	width: 100%;
	font-size: 12px;
}
.table thead {
	background-color: #e0edff
}
.table thead>tr>th,.table tbody>tr>th,.table tfoot>tr>th,.table thead>tr>td,.table tbody>tr>td,.table tfoot>tr>td
	{
	border-top: 1px solid #DDDDDD;
	line-height: 1.42857;
	vertical-align: top;
}
.table thead>tr>th {
	border-top: 1px solid #DDDDDD;
	line-height: 1.42857;
	padding: 2px;
	vertical-align: top;
}

.table-striped>tbody>tr:nth-child(odd)>td,.table-striped>tbody>tr:nth-child(odd)>th
	{
	background-color: #F5F8FA;
}

.table-hover>tbody>tr:hover>td,.table-hover>tbody>tr:hover>th {
	background-color: #e0edff;
}

</style>
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>类型</th>
					<th>个数</th>
					<th>接待人次&nbsp;<font color="green">(人次)</font></th>
					<th>总收入&nbsp;<font color="green">(万元)</font></th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="#page.result">
					<tr>

						<td>${factoryType}<input type="hidden" value="${tourIds }"
							name="tempTourIds">
						</td>
						<td>${totalFactoryCount }</td>
						<Td>${totalPersonCount }</Td>
						<Td>${totalIncome }</Td>
					</tr>
				</s:iterator>
			</tbody>
			
		</table>
</body>
</html>
