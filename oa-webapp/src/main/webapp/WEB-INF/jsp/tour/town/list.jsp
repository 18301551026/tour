<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>镇政府</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<script type="text/javascript"
	src="${ctx }/js/My97DatePicker/WdatePicker.js"></script>
<%@ include file="/common/include-bootstap.jsp"%>
<script src="${ctx }/js/grid.js"></script>
<%@ include file="/common/include-styles.jsp"%>
</head>
<body>
	<div class="panel panel-info">
		<div class="panel-heading">
			<div class="btn-group btn-group-sm">
				<button id="queryButton" class="btn btn-info">
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
			<form role="form" id="queryForm" class="form-horizontal"
				action="${ctx}/tour/townList!findPage.action" method="post">
				<s:hidden name="status"></s:hidden>
				<s:hidden name="statisticType"></s:hidden>
				<table class="formTable">
					<Tr>
						<Td class="control-label" style="width: 3%"><label
							for="address">类型：</label></Td>
						<Td class="query_input"><s:select cssClass="form-control"
								list="#{'观光园':'观光园','民俗旅游':'民俗旅游','工业旅游':'工业旅游','旅游住宿':'旅游住宿','风景旅游':'风景旅游' }"
								name="deptType" headerKey="" headerValue="全部"></s:select></Td>
						<Td class="control-label"><label>选择日期：</label></Td>
						<Td class="query_input"><input id="d4311"
							class="form-control" style="width: 45%; display: inline;"
							type="text" name="startDate" value="${startDate }"
							onFocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy年MM月',maxDate:'#F{$dp.$D(\'d4312\')||\'%y-%M\'}'})" />&nbsp;至&nbsp;
							<input id="d4312" type="text" class="form-control"
							style="width: 45%; display: inline;" name="endDate"
							value="${endDate }"
							onFocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy年MM月',minDate:'#F{$dp.$D(\'d4311\')}',maxDate:'%y-%M'})" />
						</Td>
					</Tr>
					<%-- <tr>
						<Td class="control-label" style="width: 3%"><label
							for="totalPersonNum">企业：</label>
						<Td class="query_input" colspan="3"><s:select
								list="townFactorys" id="factoryType"
								cssClass="form-control validate[required]" headerKey=""
								headerValue="全部" listKey="id" listValue="text" name="factoryId"></s:select></Td>
					</tr> --%>
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
					<th>部门</th>
					<th>接待人次&nbsp;<font color="green">(人次)</font></th>
					<th>总收入&nbsp;<font color="green">(万元)</font></th>
					<th>时间</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="#page.result">
					<tr>
						<td>${type}</td>
						<td>${user.dept.text }</td>
						<Td>${totalPersonNum }</Td>
						<Td>${totalIncome }</Td>
						<td>${reportYear }年${reportMonth }月</td>
						<td><a href="${ctx }/tour/reported!toDetail.action?id=${id}">详情</a></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</form>
	<tags:pagination page="${page }"></tags:pagination>

</body>
</html>
