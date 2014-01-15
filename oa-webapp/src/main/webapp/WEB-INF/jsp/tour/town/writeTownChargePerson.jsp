<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<title>填写责任人</title>
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
		jQuery("#form").validationEngine();
	})
</script>
	<form action=""  id="form" method="post" style="padding: 10px;margin-top: 10px;margin-left: 10px;">
		<s:textfield id="townChargePerson"
			cssStyle="width:230px;" cssClass="form-control validate[required]"
			name="townChargePerson"></s:textfield>
	</form>
</body>
</html>