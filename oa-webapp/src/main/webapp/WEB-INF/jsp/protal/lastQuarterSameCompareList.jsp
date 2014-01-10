<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/meta.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上个季度同比情况首页展示</title>
<%@include file="/common/global.jsp"%>
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
					<th>年份</th>
					<th>季度</th>
					<th>类型</th>
					<th colspan="3">接待人次</th>
					<th colspan="3">总收入&nbsp;<font color="green;">(万元)</font>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr align="center">
					<Td></Td>
					<td></td>
					<Td></Td>
					<td>本年</td>
					<td>上一年</td>
					<td>百分比</td>
					<td>本年</td>
					<td>上一年</td>
					<td>百分比</td>
				</tr>
				<s:iterator value="#page.result">
					<tr>
						<td>${year }</td>
						<td>${quarter }</td>
						<Td>${type }</Td>
						<Td>${nowTotalPersonNum }</Td>
						<Td>${lastTotalPersonNum }</Td>
						<td>
							
							<c:if test="${fn:startsWith(personNumPercent, '-') }">
								<font color="red">${personNumPercent }%</font>
							</c:if>
							<c:if test="${!fn:startsWith(personNumPercent, '-') }">
								<font color="green">${personNumPercent }%</font>
							</c:if>
							
						</td>
						<Td>${nowTotalIncome }</Td>
						<Td>${lastTotalIncome }</Td>
						<td>
							<c:if test="${fn:startsWith(incomePercent, '-') }">
								<font color="red">${incomePercent }%</font>
							</c:if>
							<c:if test="${!fn:startsWith(incomePercent, '-') }">
								<font color="green">${incomePercent }%</font>
							</c:if>
						</td>
					</tr>
				</s:iterator>
				</tr>
			</tbody>
		</table>
</body>
</html>