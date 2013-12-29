<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>区同比</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<script type="text/javascript"
	src="${ctx }/js/My97DatePicker/WdatePicker.js"></script>
<%@ include file="/common/include-bootstap.jsp"%>
<script src="${ctx }/js/grid.js"></script>
<%@ include file="/common/include-styles.jsp"%>
</head>
<script type="text/javascript">
	$(
			function() {
				$("#pillarButton").click(
						function() {
							$("#queryForm").attr("action",
									ctx + "/tour/chart!districtChart.action")
							$("#queryForm").submit();
						})
			})
</script>
<body>
	<div class="panel panel-info">
		<div class="panel-heading">
			<div class="btn-group btn-group-sm">
				<button id="pillarButton" class="btn btn-info">
					<span class="glyphicon glyphicon-print"></span> 柱形图展示
				</button>
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
				action="${ctx}/tour/districtSameCompare!districtSameCompare.action"
				method="post">
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
					<th>时间</th>
					<th>类型</th>
					<th colspan="2">接待人次</th>
					<th colspan="2">总收入&nbsp;<font color="green;">(万元)</font>
					</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<tr align="center">
					<Td></Td>
					<Td></Td>
					<td>本年</td>
					<td>上一年</td>
					<td>本年</td>
					<td>上一年</td>
					<Td></Td>
				</tr>
				<s:iterator value="#page.result">
					<tr>
						<td>${year }年${month}月</td>
						<Td>${type }</Td>
						<Td>${nowTotalPersonNum }</Td>
						<Td>${lastTotalPersonNum }</Td>
						<Td>${nowTotalIncome }</Td>
						<Td>${lastTotalIncome }</Td>
						<td><a
							href="${ctx }/tour/districtSameCompare!sameCompareToDetail.action?nowIds=${nowIds}&lastIds=${lastIds}">详情</a></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</form>
	<script type="text/javascript">
		var queryForm;
		$(function() {
			var url = window.location.href;
			var path = url.substr(url.indexOf(ctx));

			//判断查询form是否存在  		
			$('form').each(function() {
				var r = new RegExp($(this).attr('action'));
				if (r.test(path)) {
					queryForm = $(this);
				}
			});

			//如果不存在queryForm
			if (!queryForm) {
				$(document.body).append(
						'<form id = "queryForm" method="post"></form>');
				queryForm = $('#queryForm');
				queryForm.attr('action', window.location.href);
			}

			//在form中添加currentMonth,pageMonthNum对象
			queryForm
					.append('<s:hidden name="currentMonth" id="start"></s:hidden>');
			queryForm
					.append('<s:hidden name="pageMonthNum" id="pageSize"></s:hidden>');

			//初始化分页变量
			var start = parseInt($('#start').val());
			var pageSize = parseInt($('#pageSize').val());
			//总页数
			var pageCount = parseInt($('#pageCount').html());

			//判断边界
			if (start == 0) {
				$('#prev').toggleClass("disabled");
				$('#first').toggleClass("disabled");
			}
			if (start == (pageCount - 1) * pageSize || pageCount == 0) {
				$('#next').toggleClass("disabled");
				$('#last').toggleClass("disabled");
			}

			//分页按钮事件
			$('#first').bind('click', function() {
				if ($(this).attr("class") !== "disabled") {
					$("#start").val(0);
					queryForm.submit();
				}
			});
			$('#next').bind('click', function() {
				if ($(this).attr("class") !== "disabled") {
					$('#start').val(start + pageSize);
					queryForm.submit();
				}
			});
			$('#prev').bind('click', function() {
				if ($(this).attr("class") !== "disabled") {
					$('#start').val(start - pageSize);
					queryForm.submit();
				}

			});
			$('#last').bind('click', function() {
				if ($(this).attr("class") !== "disabled") {
					$('#start').val((pageCount - 1) * pageSize);
					queryForm.submit();
				}
			});
			//设置每页行数下拉列表框的onchange事件
			$('#setPageSize').bind('change', function() {
				$('#pageSize').val($('#setPageSize').val());
				$('#start').val(0);
				queryForm.submit();
			});
			$(".gotoPageNo").click(function() {
				var p = parseInt($(this).html());
				$('#start').val((p - 1) * pageSize);
				queryForm.submit();
			});

		});
	</script>

	<c:set var="m" value="${page.rowCount % pageMonthNum}"></c:set>
	<c:set var="pageCountDouble"
		value="${m == 0 ? page.rowCount/pageMonthNum : (page.rowCount - m)/pageMonthNum + 1}"></c:set>
	<fmt:formatNumber pattern="#" value="${pageCountDouble }"
		var="pageCount"></fmt:formatNumber>
	<c:set var="pageNo" value="${currentMonth / pageMonthNum + 1}"></c:set>
	<c:set var="begin" value="${1 > pageNo-5 ? 1 : pageNo-5 }"></c:set>
	<c:set var="end"
		value="${pageNo + 5 < pageCount ? pageNo + 5 : pageCount}"></c:set>

	<div class="pagination pull-right">
		<ul class="pagination-sm">
			<li class="disabled"><a>共<span id="rowCount">${page.rowCount}</span>个月<span
					id="pageCount">${pageCount }</span>页
			</a></li>
			<li id="first"><a href="javascript:void(0)">首页</a></li>
			<li id="prev"><a href="javascript:void(0)">上一页</a></li>

			<c:forEach var="i" begin="${begin }" end="${end}">
				<c:choose>
					<c:when test="${i == pageNo}">
						<li class="active"><a href="javascript:void(0)">${i}</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="javascript:void(0)" class="gotoPageNo">${i}</a></li>
					</c:otherwise>
				</c:choose>
			</c:forEach>

			<li id="next"><a href="javascript:void(0)">下一页</a></li>
			<li id="last"><a href="javascript:void(0)">末页</a></li>
			<li><a> <s:select
						list="#{1:'1个月', 2:'2个月', 3:'3个月', 4: '4个月', 5: '5个月' }"
						id="setPageSize" name="pageMonthNum"
						cssStyle="width:80px; margin:0px;">
					</s:select>
			</a></li>
		</ul>

	</div>
</body>
</html>
