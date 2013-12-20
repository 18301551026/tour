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
	function switchType(v) {
		if (v == "观光园") {
			$(".visitHidd").removeClass("hidd");
			$(".jingQuHidd").addClass("hidd");
			$(".liveHidd").addClass("hidd");
			$(".factoryHidd").addClass("hidd");
			$(".customHidd").addClass("hidd");
			$(":input[name=inputMoneys]").val('');
		} else if (v == "旅游景区") {
			$(".visitHidd").addClass("hidd");
			$(".jingQuHidd").removeClass("hidd");
			$(".liveHidd").addClass("hidd");
			$(".factoryHidd").addClass("hidd");
			$(".customHidd").addClass("hidd");
			$(":input[name=inputMoneys]").val('');
		} else if (v == "旅游住宿") {
			$(".visitHidd").addClass("hidd");
			$(".jingQuHidd").addClass("hidd");
			$(".liveHidd").removeClass("hidd");
			$(".factoryHidd").addClass("hidd");
			$(".customHidd").addClass("hidd");
			$(":input[id!=factoryType]").val('');
		} else if (v == "工业旅游") {
			$(".visitHidd").addClass("hidd");
			$(".jingQuHidd").addClass("hidd");
			$(".liveHidd").addClass("hidd");
			$(".factoryHidd").removeClass("hidd");
			$(".customHidd").addClass("hidd");
			$(":input[name=inputMoneys]").val('');
		} else if (v == "民俗旅游") {
			$(".customHidd").removeClass("hidd");
			$(".visitHidd").addClass("hidd");
			$(".jingQuHidd").addClass("hidd");
			$(".liveHidd").addClass("hidd");
			$(".factoryHidd").addClass("hidd");
			$(":input[name=inputMoneys]").val('');
		}
	}
	$(function() {
		$("#factoryType").bind("change", function() {
			var v = $(this).find("option:selected").text();
			switchType(v);
		});
		switchType($("#factoryType").find("option:selected").text());
	})
</script>
<style type="text/css">
.hidd {
	display: none;
}

.formTable .control-label {
	font-size: 13px;
	height: 34px;
	line-height: 34px;
	padding: 0;
	text-align: right;
	width: 10%;
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
	width: 90%;
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
		<table class="formTable table">
			<tr>
				<Td class="control-label"><label for="factoryType">类型：</label></Td>
				<Td class="query_input">
					<!-- <select name="job.id"
					id="factoryType" class="form-control validate[required]"
					placeholder="请选择类型">
						<option value="1" selected="selected">观光园</option>
						<option value="5">民俗旅游</option>
						<option value="2">旅游景区</option>
						<option value="3">旅游住宿</option>
						<option value="4">工业旅游</option>
				</select> --> <s:select list="userJobs" id="factoryType"
						cssClass="form-control validate[required]" listKey="id"
						listValue="jobName" name="job.id"></s:select>
				</Td>
				<Td class="control-label"><label for="reprotYearAndMonth">时间：</label>
				<Td class="query_input"><s:textfield name="reprotYearAndMonth"
						readonly="true"
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
			<tr class="visitHidd hidd">
				<Td class="control-label"><label for="visitTicket">门票收入：</label>
					<input type="hidden" name="labelTexts" value="门票收入" /></Td>
				<Td class="query_input"><s:textfield name="inputMoneys"
						placeholder="请输入门票收入" cssClass="form-control validate[required]"
						id="visitTicket"></s:textfield> <font>(万元)</font></Td>
				<Td class="control-label"><label for="visitPlant">纯种植收入：</label>
					<input type="hidden" name="labelTexts" value="纯种植收入" /></Td>
				<Td class="query_input"><s:textfield name="inputMoneys"
						placeholder="请输入纯种植收入" cssClass="form-control validate[required]"
						id="visitPlant"></s:textfield> <font>(万元)</font></Td>
			</tr>
			<tr class="visitHidd hidd">
				<Td class="control-label"><label for="visitCultivation">养殖园收入：</label>
					<input type="hidden" name="labelTexts" value="养殖收入" /></Td>
				<Td class="query_input"><s:textfield name="inputMoneys"
						placeholder="请输入养殖园收入" cssClass="form-control validate[required]"
						id="visitCultivation"></s:textfield> <font>(万元)</font></Td>
				<Td class="control-label"><label for="visitPick">采摘收入：</label>
					<input type="hidden" name="labelTexts" value="采摘收入" /></Td>
				<Td class="query_input"><s:textfield name="inputMoneys"
						placeholder="请输入采摘收入" cssClass="form-control validate[required]"
						id="visitPick"></s:textfield> <font>(万元)</font></Td>
			</tr>
			<tr class="visitHidd hidd">
				<Td class="control-label"><label for="visitSellProduce">农产品收入：</label>
					<input type="hidden" name="labelTexts" value="农产品收入" /></Td>
				<Td class="query_input"><s:textfield name="inputMoneys"
						placeholder="请输入农产品收入" cssClass="form-control validate[required]"
						id="visitSellProduce"></s:textfield> <font>(万元)</font></Td>
				<Td class="control-label"><label for="visitSellOther">其他商品收入：</label>
					<input type="hidden" name="labelTexts" value="其他商品收入" /></Td>
				<Td class="query_input"><s:textfield name="inputMoneys"
						placeholder="请输入其他商品收入" cssClass="form-control validate[required]"
						id="visitSellOther"></s:textfield> <font>(万元)</font></Td>
			</tr>
			<tr class="visitHidd hidd">
				<Td class="control-label"><label for="visitAsusement">健身娱乐收入：</label>
					<input type="hidden" name="labelTexts" value="健身娱乐收入" /></Td>
				<Td class="query_input"><s:textfield name="inputMoneys"
						placeholder="请输入健身娱乐收入" cssClass="form-control validate[required]"
						id="visitAsusement"></s:textfield> <font>(万元)</font></Td>
				<Td class="control-label"><label for="visitFishing">垂钓收入：</label>
					<input type="hidden" name="labelTexts" value="垂钓收入" /></Td>
				<Td class="query_input"><s:textfield name="inputMoneys"
						placeholder="请输入垂钓收入" cssClass="form-control validate[required]"
						id="visitFishing"></s:textfield> <font>(万元)</font></Td>
			</tr>
			<tr class="visitHidd hidd">
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
			<tr class="visitHidd hidd">
				<Td class="control-label" width="3"><label for="visitOther">其它收入：</label>
					<input type="hidden" name="labelTexts" value="其它收入" /></Td>
				<Td class="query_input" colspan="3"><s:textfield
						name="inputMoneys" placeholder="请输入其它收入"
						cssClass="form-control validate[required]" id="visitOther"
						cssStyle="width:96%"></s:textfield> <font>(万元)</font></Td>
			</tr>

			<!-- 民俗 -->
			<tr class="customHidd hidd">
				<Td class="control-label"><label for="customSellAndProcess">出售和加工收入：</label>
					<input type="hidden" name="labelTexts" value="出售和加工收入" /></Td>
				<Td class="query_input"><s:textfield name="inputMoneys"
						placeholder="请输入出售和加工收入"
						cssClass="form-control validate[required]"
						id="customSellAndProcess"></s:textfield> <font>(万元)</font></Td>
				<Td class="control-label"><label for="customRepast">餐饮收入：</label>
					<input type="hidden" name="labelTexts" value="餐饮收入" /></Td>
				<Td class="query_input"><s:textfield name="inputMoneys"
						placeholder="请输入餐饮收入" cssClass="form-control validate[required]"
						id="customRepast"></s:textfield> <font>(万元)</font></Td>
			</tr>
			<tr class="customHidd hidd">
				<Td class="control-label" width="3%"><label for="customLive">住宿收入：</label>
					<input type="hidden" name="labelTexts" value="住宿收入" /></Td>
				<Td class="query_input" colspan="3"><s:textfield
						name="inputMoneys" placeholder="请输入住宿收入"
						cssClass="form-control validate[required]" id="customLive"
						cssStyle="width:96%"></s:textfield> <font>(万元)</font></Td>
			</tr>
			<!-- 旅游景区 -->
			<tr class="jingQuHidd hidd">
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
			<tr class="jingQuHidd hidd">
				<Td class="control-label"><label for="jingQuRepast">餐饮收入：</label>
					<input type="hidden" name="labelTexts" value="餐饮收入" /></Td>
				<Td class="query_input" colspan="3"><s:textfield
						name="inputMoneys" placeholder="请输入餐饮收入"
						cssClass="form-control validate[required]" id="jingQuRepast"
						cssStyle="width:96%"></s:textfield> <font>(万元)</font></Td>
			</tr>
			<!-- 旅游住宿 -->
			<tr class="liveHidd hidd">
				<Td class="control-label"><label for="liveHouseCost">房费：</label>
					<input type="hidden" name="labelTexts" value="房费" /></Td>
				<Td class="query_input"><s:textfield name="inputMoneys"
						placeholder="请输入房费" cssClass="form-control validate[required]"
						id="liveHouseCost"></s:textfield> <font>(万元)</font></Td>
				<Td class="control-label"><label for="liveSellOther">出售其他商品收入：</label>
					<input type="hidden" name="labelTexts" value="出售其他商品收入" /></Td>
				<Td class="query_input"><s:textfield name="inputMoneys"
						placeholder="请输入出售商品收入" cssClass="form-control validate[required]"
						id="liveSellOther"></s:textfield> <font>(万元)</font></Td>
			</tr>
			<tr class="liveHidd hidd">
				<Td class="control-label" width="3%"><label for="liveRepast">餐饮收入：</label>
					<input type="hidden" name="labelTexts" value="餐饮收入" /></Td>
				<Td class="query_input" colspan="3"><s:textfield
						name="inputMoneys" placeholder="请输入餐饮收入"
						cssClass="form-control validate[required]" id="liveRepast"
						cssStyle="width:96%"></s:textfield> <font>(万元)</font></Td>
			</tr>
			<!-- 工业旅游 -->
			<tr class="factoryHidd hidd">
				<Td class="control-label"><label for="factoryTicket">门票收入：</label>
					<input type="hidden" name="labelTexts" value="门票收入" /></Td>
				<Td class="query_input"><s:textfield name="inputMoneys"
						placeholder="请输入门票收入" cssClass="form-control validate[required]"
						id="factoryTicket"></s:textfield> <font>(万元)</font></Td>
				<Td class="control-label"><label for="factorySellOther">出售商品收入：</label>
					<input type="hidden" name="labelTexts" value="出售商品收入" /></Td>
				<Td class="query_input"><s:textfield name="inputMoneys"
						placeholder="请输入出售商品收入" cssClass="form-control validate[required]"
						id="factorySellOther"></s:textfield> <font>(万元)</font></Td>
			</tr>
			<tr>
				<Td class="control-label"><label for="desc">备注：</label>
				<Td class="query_input" colspan="3"><s:textarea name="desc"
						id="desc"></s:textarea></Td>
			</tr>
		</table>
	</form>
</body>
</html>
