<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>修改</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<%@ include file="/common/include-jquery-kindeditor.jsp"%>
<script type="text/javascript" src="${ctx }/js/edit2Editor.js"></script>
<script type="text/javascript"
	src="${ctx }/js/My97DatePicker/WdatePicker.js"></script>
<%@ include file="/common/include-bootstap.jsp"%>
<%@ include file="/common/include-jquery-validation.jsp"%>
<%@ include file="/common/include-styles.jsp"%>
<style type="text/css">
.formTable .control-label {
	font-size: 13px;
	height: 34px;
	line-height: 34px;
	padding: 0;
	text-align: right;
	width: 16%;
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
	line-height: 30px;
}
</style>
<script type="text/javascript">
	$(function() {
		$(".updateDetail").click(function() {
			var detailId = $(this).attr("actionId");
			var mon = $(this).prev().prev().val();
			$.ajax({
				type : "POST",
				url : ctx + "/tour/noReported!updateDetail.action",
				data : "detailId=" + detailId + "&money=" + mon,
				success : function(msg) {
					alert("修改成功");
				}
			});
		});
	})
</script>
</head>
<body class="editBody">
	<button class="btn btn-info btn-sm pull-left" id="backButton">
		<span class="glyphicon glyphicon-backward"></span> 返回列表
	</button>
	<div class="btn-group pull-right btn-group-sm">
		<button type="button" class="btn btn-info" id="saveButton">
			<span class="glyphicon glyphicon-ok"></span> 保存
		</button>
		<button type="button" class="btn btn-info" id="resetButton"
			actionUrl="${ctx}/tour/noReported!toUpdate.action?id=${id}">
			<span class="glyphicon glyphicon-repeat"></span> 重置
		</button>
	</div>
	<div class="clearfix" style="margin-bottom: 20px;"></div>
	<form action="${ctx}/tour/noReported!save.action" method="post"
		id="editForm">
		<s:hidden name="id"></s:hidden>
		<table class="formTable table">
			<tr>
				<Td class="control-label"><label for="reprotYearAndMonth">时间：</label>
				<Td class="query_input" colspan="3"><s:textfield
						name="reprotYearAndMonth" readonly="true" cssStyle="width:100%"
						onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy年MM月'})"
						placeholder="请选择时间" cssClass="form-control validate[required]"
						id="reprotYearAndMonth"></s:textfield></Td>
			</tr>
			<tr>
				<Td class="control-label" style="width: 3%;"><label for="totalPersonNum">接待人次：</label>
				<Td class="query_input" colspan="3"><s:textfield name="totalPersonNum"
						placeholder="请输入接待人次" cssStyle="width:94%" cssClass="form-control validate[required]"
						id="totalPersonNum"></s:textfield> <font>(人次)</font></Td>
				<%-- <Td class="control-label"><label for="totalIncome">总收入：</label>
				<Td class="query_input"><s:textfield name="totalIncome"
						placeholder="请输入总收入" cssClass="form-control validate[required]"
						id="totalIncome"></s:textfield> <font>(万元)</font></Td> --%>
			</tr>
			<%-- <c:forEach items="${beans }" var="d" >
				<Tr>
					<Td class="control-label"><label for="voteOptions">${d.name }：</label></Td>
					<Td class="query_input" colspan="3"><input
						placeholder="请输入${d.name }" type="text"
						class="form-control validate[required] pull-left" 
						value="${d.money }" style="margin-bottom: 2px;width: 92%"> <font >(万元)</font>
						<input class="btn btn-info btn-xs pull-right updateDetail" actionId="${d.id }"
						value="修改" style="margin-top: 2px;" type="button" /></Td>
				</Tr>
			</c:forEach> --%>
			<c:forEach begin="0" end="${detailNum-1 }" step="1" var="i">
				<c:if test="${i%2==0 }">
					<tr>
				</c:if>

				<c:if test="${i%2==0 }">
					<Td class="control-label"><label for="voteOptions">${beans[i].name }：</label></Td>
					<Td class="query_input"
						<c:if test="${i==(detailNum-1) }"> colspan="3"</c:if>><input
						type="hidden" name="beans[${i }].id" value="${beans[i].id }">
						<input name="beans[${i }].money"
						placeholder="请输入${beans[i].name }" type="text"
						class="form-control validate[required] pull-left"
						value="${beans[i].money }"
						<c:if test="${i==(detailNum-1) }">
							 style="margin-bottom: 2px;width: 94%"
						</c:if>>
						<font>(万元)</font></Td>
				</c:if>
				<c:if test="${i%2!=0 }">
					<Td class="control-label"><label for="voteOptions">${beans[i].name }：</label></Td>
					<Td class="query_input"><input type="hidden"
						name="beans[${i }].id" value="${beans[i].id }"> <input
						name="beans[${i }].money" placeholder="请输入${beans[i].name }"
						type="text" class="form-control validate[required] pull-left"
						value="${beans[i].money }" style="margin-bottom: 2px;"> <font>(万元)</font></Td>
				</c:if>
				<c:if test="${i%2!=0 }">
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
