<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>类型添加</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<%@ include file="/common/include-jquery-easyui.jsp"%>
<%@ include file="/common/include-bootstap.jsp"%>
<%@ include file="/common/include-jquery-validation.jsp"%>
<%@ include file="/common/include-jquery-kindeditor.jsp"%>
<script type="text/javascript" src="${ctx }/js/edit2Editor.js"></script>
<%@ include file="/common/include-styles.jsp"%>
</head>
<script type="text/javascript">
	$(function(){
		$("#addOption")
		.click(
				function() {
					var temp = $(this)
							.after(
									'<input name="optionNames" class="pull-left form-control validate[required]"  style="width: 94%;" placeholder="请输入明细"><input class="btn btn-info btn-xs pull-right deleteTypeOption" style="margin-top:4px;" value="删除" type="button" />');
				});
		$(".deleteTypeOption").live("click",function(){
			var pre = $(this).prev();
			pre.remove();
			$(this).remove();
		});
	})
</script>
<body class="editBody">
	<button class="btn btn-info btn-sm pull-left"   onclick="javascript:location.href='${ctx}/tour/factoryType!findPage.action'">
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
	<form action="${ctx}/tour/factoryType!save.action" method="post"
		id="editForm">
		<table class="formTable table">
			<tr>
				<Td class="control-label" style="width: 3%;"><label for="name">类型名称：</label></Td>
				<Td class="query_input" colspan="3"><s:textfield name="name"
						placeholder="请输入类型名称" cssClass="form-control validate[required]"
						id="name"></s:textfield></Td>
			</tr>
			<tr>
				<Td class="control-label" style="width: 3%"><label>明细：</label></Td>
				<Td class="query_input">
				<input class="pull-left form-control validate[required]" name="optionNames" style="width: 94%;" placeholder="请输入明细">
				<input class="btn btn-info btn-xs pull-right"
					id="addOption" value="添加" type="button" style="margin-top: 3px;" /></Td>
			</tr>
			<tr>
				<Td class="control-label" style="width: 3%;"><label for="desc">描述：</label></Td>
				<Td class="query_input" colspan="3"><s:textarea name="desc"></s:textarea>
				</Td>
			</tr>
		</table>
	</form>
</body>

</html>
