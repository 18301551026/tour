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
<script type="text/javascript">
	$(function() {
		$("#reportHtmlButton").click(function() {
			$("#reprotType").val('html');
			$("#queryForm").attr("action",ctx+"/tour/export!townExportXlsOaHtml.action")
			$("#queryForm").attr("target","_blank");
			$("#queryForm").submit();
		});
		$("#exportExcelButton").click(function() {
			$("#reprotType").val('xls');
			$("#queryForm").attr("action",ctx+"/tour/export!townExportXlsOaHtml.action");
			$("#queryForm").attr("target","_blank");
			$("#queryForm").submit();
		});
		$("#exportWordButton").click(function() {
			$("#queryForm").attr("target","_blank");
			$("#queryForm").attr("action",ctx+"/tour/export!townExportWord.action")
			$("#queryForm").submit();
		});
	});
</script>
<body>
	<div class="panel panel-info">
		<div class="panel-heading">
			<div class="btn-group btn-group-sm">
				<button id="reportHtmlButton" class="btn btn-info">
					<span class="glyphicon glyphicon-print"></span> 报表html
				</button>
				<button id="exportExcelButton" class="btn btn-info">
					<span class="glyphicon glyphicon-print"></span> 导出excel
				</button>
				<button id="exportWordButton" class="btn btn-info">
					<span class="glyphicon glyphicon-print"></span> 导出word
				</button>
				<button id="queryButton" class="btn btn-info"  actionUrl="${ctx}/tour/townStatistic!townStatisticList.action">
					<span class="glyphicon glyphicon-search"></span> 查询
				</button>
			</div>
			<div class="pull-right" style="margin-top: 6px;">
				<a href="javascript:void(0)" title="查询表单"
					id="showOrHideQueryPanelBtn"><span
					class="glyphicon glyphicon-chevron-down pull-right"></span> 查询条件</a>
			</div>
		</div>
		<div class="panel-body hide" id="queryPanel">
			<form role="form" id="queryForm" class="form-horizontal" target="_self"
				action="${ctx}/tour/townStatistic!townStatisticList.action"
				method="post">
				<s:hidden name="reprotType" id="reprotType"></s:hidden>
				<table class="formTable">
					<Tr>
						<Td class="control-label" style="width: 3%"><label>选择日期：</label></Td>
						<Td class="query_input" colspan="3"><input id="d4311"
							class="form-control" style="width: 45%; display: inline;"
							type="text" name="startDate" value="${startDate }"
							onFocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy年MM月',maxDate:'#F{$dp.$D(\'d4312\')||\'%y-%M\'}'})" />&nbsp;至&nbsp;
							<input id="d4312" type="text" class="form-control"
							style="width: 45%; display: inline;" name="endDate"
							value="${endDate }"
							onFocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy年MM月',minDate:'#F{$dp.$D(\'d4311\')}',maxDate:'%y-%M'})" />
						</Td>
					</Tr>
				</table>
			</form>
		</div>
	</div>
	<form method="post" action="${ctx }/tour/reported!delete.action"
		id="deleteForm">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>类型</th>
					<th>个数</th>
					<th>接待人次&nbsp;<font color="green">(人次)</font></th>
					<th>总收入&nbsp;<font color="green">(万元)</font></th>
					<th>操作</th>
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
						<td><a
							href="${ctx }/tour/townStatistic!townStatisticListToDetail.action?tourIds=${tourIds}">详情</a></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</form>

</body>
</html>
