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
}
</style>
<body class="editBody">
	<button class="btn btn-info btn-sm pull-left" id="backButton">
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
						placeholder="请选择时间" cssClass="form-control validate[required]"
						id="reprotYearAndMonth"></s:textfield></Td>
			</tr>
			<tr>
				<Td class="control-label"><label for="totalPersonNum">接待人次：</label>
				<Td class="query_input"><s:textfield name="totalPersonNum"
						placeholder="请输入接待人次" cssClass="form-control validate[required]"
						id="totalPersonNum"></s:textfield> <font>(人次)</font></Td>
				<Td class="control-label"><label for="totalIncome">总收入：</label>
				<Td class="query_input"><s:textfield name="totalIncome"
						placeholder="请输入总收入" cssClass="form-control validate[required]"
						id="totalIncome"></s:textfield> <font>(万元)</font></Td>
			</tr>
			<c:if test="${deptType=='观光园' }">
				<tr>
					<Td class="control-label"><label for="visitTicket">门票收入：</label>
						<input type="hidden" name="labelTexts" value="门票收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入门票收入" cssClass="form-control validate[required]"
							id="visitTicket"></s:textfield> <font>(万元)</font></Td>
					<Td class="control-label"><label for="visitPlant">纯种植、养殖园收入：</label>
						<input type="hidden" name="labelTexts" value="纯种植、养殖园收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入纯种植、养殖园收入"
							cssClass="form-control validate[required]" id="visitPlant"></s:textfield>
						<font>(万元)</font></Td>
				</tr>
				<tr>
					<Td class="control-label"><label for="visitPick">采摘收入：</label>
						<input type="hidden" name="labelTexts" value="采摘收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入采摘收入" cssClass="form-control validate[required]"
							id="visitPick"></s:textfield> <font>(万元)</font></Td>
					<Td class="control-label"><label for="visitPick">设施地采摘收入：</label>
						<input type="hidden" name="labelTexts" value="设施地采摘收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入设施地采摘收入"
							cssClass="form-control validate[required]" id="visitPick"></s:textfield>
						<font>(万元)</font></Td>
				</tr>
				<tr>
					<Td class="control-label"><label for="visitSellProduce">出售农产品收入：</label>
						<input type="hidden" name="labelTexts" value="出售农产品收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入出售农产品收入"
							cssClass="form-control validate[required]" id="visitSellProduce"></s:textfield>
						<font>(万元)</font></Td>
					<Td class="control-label"><label for="visitSellOther">出售其他商品收入：</label>
						<input type="hidden" name="labelTexts" value="出售其他商品收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入出售其他商品收入"
							cssClass="form-control validate[required]" id="visitSellOther"></s:textfield>
						<font>(万元)</font></Td>
				</tr>
				<tr>
					<Td class="control-label"><label for="visitAsusement">健身娱乐收入：</label>
						<input type="hidden" name="labelTexts" value="健身娱乐收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入健身娱乐收入"
							cssClass="form-control validate[required]" id="visitAsusement"></s:textfield>
						<font>(万元)</font></Td>
					<Td class="control-label"><label for="visitFishing">垂钓收入：</label>
						<input type="hidden" name="labelTexts" value="垂钓收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入垂钓收入" cssClass="form-control validate[required]"
							id="visitFishing"></s:textfield> <font>(万元)</font></Td>
				</tr>
				<tr>
					<Td class="control-label"><label for="visitRepast">餐饮收入：</label>
						<input type="hidden" name="labelTexts" value="餐饮收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入餐饮收入" cssClass="form-control validate[required]"
							id="visitRepast"></s:textfield> <font>(万元)</font></Td>
					<Td class="control-label"><label for="visitLive">住宿收入：</label>
						<input type="hidden" name="labelTexts" value="住宿收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入住宿收入" cssClass="form-control validate[required]"
							id="visitLive"></s:textfield> <font>(万元)</font></Td>
				</tr>
				<tr>
					<Td class="control-label" width="3"><label for="visitOther">其它收入：</label>
						<input type="hidden" name="labelTexts" value="其它收入" /></Td>
					<Td class="query_input" colspan="3"><s:textfield
							name="inputMoneys" placeholder="请输入其它收入"
							cssClass="form-control validate[required]" id="visitOther"
							cssStyle="width:94%"></s:textfield> <font>(万元)</font></Td>
				</tr>
			</c:if>
			<!-- 民俗 -->
			<c:if test="${deptType=='民俗旅游' }">
				<tr>
					<Td class="control-label"><label for="customSellAndProcess">出售和加工自农产品收入：</label>
						<input type="hidden" name="labelTexts" value="出售和加工自农产品收入" /></Td>
					<Td class="query_input" colspan="3"><s:textfield
							name="inputMoneys" placeholder="请输入出售和加工自农产品收入"
							cssClass="form-control validate[required]"
							id="customSellAndProcess" cssStyle="width:94%"></s:textfield> <font>(万元)</font></Td>
				</tr>
				<tr>
					<Td class="control-label"><label for="customRepast">餐饮收入：</label>
						<input type="hidden" name="labelTexts" value="餐饮收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入餐饮收入" cssClass="form-control validate[required]"
							id="customRepast"></s:textfield> <font>(万元)</font></Td>
					<Td class="control-label"><label for="customLive">住宿收入：</label>
						<input type="hidden" name="labelTexts" value="住宿收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入住宿收入" cssClass="form-control validate[required]"
							id="customLive" cssStyle="width:96%"></s:textfield> <font>(万元)</font></Td>
				</tr>
			</c:if>
			<!-- 旅游景区 -->
			<c:if test="${deptType=='旅游景区' }">
				<tr>
					</Td>
					<Td class="control-label"><label for="jingQuTicket">门票收入：</label>
						<input type="hidden" name="labelTexts" value="门票收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入门票收入" cssClass="form-control validate[required]"
							id="jingQuTicket"></s:textfield> <font>(万元)</font></Td>
					<Td class="control-label"><label for="jingQuSellOther">出售其他商品收入：</label>
						<input type="hidden" name="labelTexts" value="出售其他商品收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入出售其他商品收入"
							cssClass="form-control validate[required]" id="jingQuSellOther"></s:textfield>
						<font>(万元)</font></Td>
				</tr>
				<tr>
					<Td class="control-label"><label for="jingQuRepast">餐饮收入：</label>
						<input type="hidden" name="labelTexts" value="餐饮收入" /></Td>
					<Td class="query_input" colspan="3"><s:textfield
							name="inputMoneys" placeholder="请输入餐饮收入"
							cssClass="form-control validate[required]" id="jingQuRepast"
							cssStyle="width:94%"></s:textfield> <font>(万元)</font></Td>
				</tr>
			</c:if>
			<!-- 旅游住宿 -->
			<c:if test="${deptType=='旅游住宿' }">
				<tr>
					<Td class="control-label"><label for="liveHouseCost">房费：</label>
						<input type="hidden" name="labelTexts" value="房费" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入房费" cssClass="form-control validate[required]"
							id="liveHouseCost"></s:textfield> <font>(万元)</font></Td>
					<Td class="control-label"><label for="liveSellOther">出售其他商品收入：</label>
						<input type="hidden" name="labelTexts" value="出售其他商品收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入出售商品收入"
							cssClass="form-control validate[required]" id="liveSellOther"></s:textfield>
						<font>(万元)</font></Td>
				</tr>
				<tr>
					<Td class="control-label" width="3%"><label for="liveRepast">餐饮收入：</label>
						<input type="hidden" name="labelTexts" value="餐饮收入" /></Td>
					<Td class="query_input" colspan="3"><s:textfield
							name="inputMoneys" placeholder="请输入餐饮收入"
							cssClass="form-control validate[required]" id="liveRepast"
							cssStyle="width:94%"></s:textfield> <font>(万元)</font></Td>
				</tr>
			</c:if>
			<!-- 工业旅游 -->
			<c:if test="${deptType=='工业旅游' }">
				<tr>
					<Td class="control-label"><label for="factoryTicket">门票收入：</label>
						<input type="hidden" name="labelTexts" value="门票收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入门票收入" cssClass="form-control validate[required]"
							id="factoryTicket"></s:textfield> <font>(万元)</font></Td>
					<Td class="control-label"><label for="factorySellOther">出售其它商品收入：</label>
						<input type="hidden" name="labelTexts" value="出售其它商品收入" /></Td>
					<Td class="query_input"><s:textfield name="inputMoneys"
							placeholder="请输入出售其它商品收入"
							cssClass="form-control validate[required]" id="factorySellOther"></s:textfield>
						<font>(万元)</font></Td>
				</tr>
			</c:if>
			<tr>
				<Td class="control-label"><label for="desc">备注：</label>
				<Td class="query_input" colspan="3"><s:textarea name="desc"
						id="desc"></s:textarea></Td>
			</tr>
		</table>
	</form>
</body>
</html>
