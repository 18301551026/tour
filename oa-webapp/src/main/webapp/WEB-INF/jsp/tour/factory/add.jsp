<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>添加</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<%@ include file="/common/include-jquery-kindeditor.jsp"%>
<script type="text/javascript" src="${ctx }/js/edit2Editor.js"></script>
<script type="text/javascript"
	src="${ctx }/js/My97DatePicker/WdatePicker.js"></script>
<%@ include file="/common/include-bootstap.jsp"%>
<%@ include file="/common/include-jquery-validation.jsp"%>
<%@ include file="/common/include-styles.jsp"%>

</head>
<script type="text/javascript">
	function checkDate(field, rules, i, options){
		var reprotYearAndMonth = field.val();
		var result;
		if (reprotYearAndMonth) {
			$.ajax({
					type : "POST",
					async: false,
					url : ctx
							+ "/tour/noReported!checkUserIsReportThisMonth.action",
					data : "reprotYearAndMonth="
							+ reprotYearAndMonth,
					success : function(msg) {
						if (msg == '已经申报') {
							options.allrules.validate2fields.alertText="您"+ reprotYearAndMonth+ "已经申报了，不能再申报了"
							$("#reprotYearAndMonth").val('');
							result= options.allrules.validate2fields.alertText;
						}
				}
					});
		}
		return result;
	}
</script>
<style type="text/css">
.formTable .control-label {
	font-size: 13px;
	height: 34px;
	line-height: 34px;
	padding: 0;
	text-align: right;
	width: 18%;
}

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
	width: 85%;
}

font {
	color: green;
	width: 20px;
	display: inline;
	line-height: 27px;
}
</style>
<body class="editBody">
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
	<form action="${ctx}/tour/noReported!save.action" method="post"
		id="editForm">
		<input type="hidden" name="type" value="${deptType }">
		<table class="formTable table">
			<tr>
				<Td class="control-label" style="width: 3%"><label
					for="reprotYearAndMonth">时间：</label>
				<Td class="query_input" colspan="3"><s:textfield
						name="reprotYearAndMonth" readonly="true" cssStyle="width:100%"
						onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy年MM月',maxDate:'%y-%M'})"
						placeholder="请选择时间" cssClass="form-control validate[required,funcCall[checkDate]]"
						id="reprotYearAndMonth"></s:textfield></Td>
			</tr>
			<tr>
				<Td class="control-label" style="width: 3%"><label
					for="totalPersonNum">接待人次：</label>
				<Td class="query_input" colspan="3"><s:textfield
						name="totalPersonNum" placeholder="请输入接待人次"
						cssClass="form-control validate[required,custom[integer],min[0]]" id="totalPersonNum"
						cssStyle="width:94%"></s:textfield> <font>(人次)</font></Td>
			</tr>
			<c:set value="${fn:length(factoryOptions)}" var="detailNum"></c:set>
			<c:forEach items="${factoryOptions}" var="o" varStatus="sta">
				<input type="hidden" name="labelTexts" value="${o.name }" />
				</Td>
				<c:if test="${sta.index%2==0 }">
					<tr>
				</c:if>
				<c:if test="${sta.index%2==0 }">
					<Td class="control-label"><label for="voteOptions">${o.name}：</label></Td>
					<Td class="query_input"
						<c:if test="${sta.index==(detailNum-1) }"> colspan="3"</c:if>>
						<input name="inputMoneys" placeholder="请输入${o.name }" type="text"
						class="form-control validate[required,custom[number],min[0]]  pull-left"
						<c:if test="${sta.index==(detailNum-1) }">
							 style="margin-bottom: 2px;width: 94%"
						</c:if>>
						<font>(万元)</font>
					</Td>
				</c:if>
				<c:if test="${sta.index%2!=0 }">
					<Td class="control-label"><label for="voteOptions">${o.name }：</label></Td>
					<Td class="query_input"><input name="inputMoneys"
						placeholder="请输入${o.name }" type="text"
						class="form-control validate[required,custom[number],min[0]] pull-left"
						style="margin-bottom: 2px;"> <font>(万元)</font></Td>
				</c:if>
				<c:if test="${sta.index%2!=0 }">
					</tr>
				</c:if>
			</c:forEach>
			<tr>
				<Td class="control-label"><label for="desc">备注：</label>
				<Td class="query_input" colspan="3"><s:textarea name="desc"
						id="desc"></s:textarea></Td>
			</tr>
		</table>
	</form>
</body>
</html>
