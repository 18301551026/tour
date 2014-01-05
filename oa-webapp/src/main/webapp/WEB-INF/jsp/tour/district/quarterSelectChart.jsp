<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<title>选择同比时间</title>
<%@ include file="/common/global.jsp"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<%@ include file="/common/include-jquery-easyui.jsp"%>

</head>
<body>
<style type="text/css">
.form-control {
	background-color: #FFFFFF;
	border: 1px solid #CCCCCC;
	border-radius: 4px;
	box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
	color: #555555;
	display: block;
	font-size: 12px;
	height: 25px;
	line-height: 1.42857;
	padding: 2px;
	transition: border-color 0.15s ease-in-out 0s, box-shadow 0.15s
		ease-in-out 0s;
	vertical-align: middle;
	width: 100%;
}
</style>
<script type="text/javascript"
	src="${ctx }/js/My97DatePicker/WdatePicker.js"></script>
<%@ include file="/common/include-jquery-validation.jsp"%>
<script type="text/javascript">
	$(function(){
		jQuery("#chartForm").validationEngine();
	})
</script>
	<form action="" target="_blank" id="chartForm" method="post" style="padding: 10px;margin-top: 10px;margin-left: 10px;">
		年份：<s:textfield name="startYear" 
			cssStyle="width:230px;"
			onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy',maxDate:'%y-%M'})"
			placeholder="请选择时间" cssClass="form-control validate[required]"
			>
		</s:textfield><br/>
		季度：<s:select list="#{1:'一季度',2:'二季度',3:'三季度',4:'四季度' }" name="quarters" cssStyle="width:240px;" 
		placeholder="请选择季度" cssClass="form-control validate[required]"
		>
		</s:select>
	</form>
</body>
</html>