<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>添加</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<script type="text/javascript" src="${ctx }/js/edit.js"></script>
<%@ include file="/common/include-bootstap.jsp"%>
<%@ include file="/common/include-jquery-validation.jsp"%>
<%@ include file="/common/include-styles.jsp"%>

</head>
<script type="text/javascript">
	$(function() {
		$("#factoryType").bind("change", function() {
			var v = $(this).val();
			if (v == 1) {
				$(".visitHidd").removeClass("hidd");
				$(".jingQuHidd").addClass("hidd");
				$(".liveHidd").addClass("hidd");
				$(".factoryHidd").addClass("hidd");
				$(".customHidd").addClass("hidd");
				$(":input[id!=factoryType]").val('');
			} else if (v == 2) {
				$(".visitHidd").addClass("hidd");
				$(".jingQuHidd").removeClass("hidd");
				$(".liveHidd").addClass("hidd");
				$(".factoryHidd").addClass("hidd");
				$(".customHidd").addClass("hidd");
				$(":input[id!=factoryType]").val('');
			} else if (v == 3) {
				$(".visitHidd").addClass("hidd");
				$(".jingQuHidd").addClass("hidd");
				$(".liveHidd").removeClass("hidd");
				$(".factoryHidd").addClass("hidd");
				$(".customHidd").addClass("hidd");
				$(":input[id!=factoryType]").val('');
			} else if (v == 4) {
				$(".visitHidd").addClass("hidd");
				$(".jingQuHidd").addClass("hidd");
				$(".liveHidd").addClass("hidd");
				$(".factoryHidd").removeClass("hidd");
				$(".customHidd").addClass("hidd");
				$(":input[id!=factoryType]").val('');
			} else if (v == 5) {
				$(".customHidd").removeClass("hidd");
				$(".visitHidd").addClass("hidd");
				$(".jingQuHidd").addClass("hidd");
				$(".liveHidd").addClass("hidd");
				$(".factoryHidd").addClass("hidd");
				$(":input[id!=factoryType]").val('');
			}
		});
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
	<form action="${ctx}/tour/factory!save.action" method="post" id="editForm">
		<table class="formTable table">
			<tr>
				<Td class="control-label" style="width: 3%"><label
					for="factoryType">类型：</label></Td>
				<Td class="query_input" colspan="3"><select
					name="tourBase.type" id="factoryType"
					class="form-control validate[required]" placeholder="请选择类型">
						<option value="1" selected="selected">观光园</option>
						<option value="5">民俗旅游</option>
						<option value="2">旅游景区</option>
						<option value="3">旅游住宿</option>
						<option value="4">工业旅游</option>
				</select></Td>
			</tr>
			<tr class="visitHidd">
				<Td class="control-label"><label for="totalPersonNum">接待人次：</label>
					<input type="hidden" name="labelTexts" value="接待人次" /></Td>
				<Td class="query_input"><s:textfield
						name="d" placeholder="请输入接待人次"
						cssClass="form-control validate[required]" id="totalPersonNum"></s:textfield></Td>
				<Td class="control-label"><label for="totalIncome">总收入：</label>
					<input type="hidden" name="labelTexts" value="总收入" /></Td>
				<Td class="query_input"><s:textfield
						name="" placeholder="请输入总收入"
						cssClass="form-control validate[required]" id="totalIncome"></s:textfield></Td>
			</tr>
			<tr class="visitHidd">
				<Td class="control-label"><label for="visitTicket">门票收入：</label>
					<input type="hidden" name="labelTexts" value="门票收入" /></Td>
				<Td class="query_input"><s:textfield name="visitTicket"
						placeholder="请输入门票收入" cssClass="form-control validate[required]"
						id="visitTicket"></s:textfield></Td>
				<Td class="control-label"><label for="visitPlant">纯种植收入：</label>
					<input type="hidden" name="labelTexts" value="纯种植收入" /></Td>
				<Td class="query_input"><s:textfield name="visitPlant"
						placeholder="请输入纯种植收入" cssClass="form-control validate[required]"
						id="visitPlant"></s:textfield></Td>
			</tr>
			<tr class="visitHidd">
				<Td class="control-label"><label for="visitCultivation">养殖园收入：</label>
					<input type="hidden" name="labelTexts" value="养殖收入" /></Td>
				<Td class="query_input"><s:textfield name="visitCultivation"
						placeholder="请输入养殖园收入" cssClass="form-control validate[required]"
						id="visitCultivation"></s:textfield></Td>
				<Td class="control-label"><label for="visitPick">采摘收入：</label>
					<input type="hidden" name="labelTexts" value="采摘收入" /></Td>
				<Td class="query_input"><s:textfield name="visitPick"
						placeholder="请输入采摘收入" cssClass="form-control validate[required]"
						id="visitPick"></s:textfield></Td>
			</tr>
			<tr class="visitHidd">
				<Td class="control-label"><label for="visitSellProduce">农产品收入：</label>
					<input type="hidden" name="labelTexts" value="农产品收入" /></Td>
				<Td class="query_input"><s:textfield name="visitSellProduce"
						placeholder="请输入农产品收入" cssClass="form-control validate[required]"
						id="visitSellProduce"></s:textfield></Td>
				<Td class="control-label"><label for="visitSellOther">其他商品收入：</label>
					<input type="hidden" name="labelTexts" value="其他商品收入" /></Td>
				<Td class="query_input"><s:textfield name="visitSellOther"
						placeholder="请输入其他商品收入" cssClass="form-control validate[required]"
						id="visitSellOther"></s:textfield></Td>
			</tr>
			<tr class="visitHidd">
				<Td class="control-label"><label for="visitAsusement">健身娱乐收入：</label>
					<input type="hidden" name="labelTexts" value="健身娱乐收入" /></Td>
				<Td class="query_input"><s:textfield name="visitAsusement"
						placeholder="请输入健身娱乐收入" cssClass="form-control validate[required]"
						id="visitAsusement"></s:textfield></Td>
				<Td class="control-label"><label for="visitFishing">垂钓收入：</label>
					<input type="hidden" name="labelTexts" value="垂钓收入" /></Td>
				<Td class="query_input"><s:textfield name="visitFishing"
						placeholder="请输入垂钓收入" cssClass="form-control validate[required]"
						id="visitFishing"></s:textfield></Td>
			</tr>
			<tr class="visitHidd">
				<Td class="control-label"><label for="visitRepast">餐饮收入：</label>
					<input type="hidden" name="labelTexts" value="餐饮收入" /></Td>
				<Td class="query_input"><s:textfield name="visitRepast"
						placeholder="请输入餐饮收入" cssClass="form-control validate[required]"
						id="visitRepast"></s:textfield></Td>
				<Td class="control-label"><label for="visitLive">住宿收入：</label>
					<input type="hidden" name="labelTexts" value="住宿收入" /></Td>
				<Td class="query_input"><s:textfield name="visitLive"
						placeholder="请输入住宿收入" cssClass="form-control validate[required]"
						id="visitLive"></s:textfield></Td>
			</tr>
			<tr class="visitHidd">
				<Td class="control-label" width="3"><label for="visitOther">其他：</label>
					<input type="hidden" name="labelTexts" value="其他" /></Td>
				<Td class="query_input" colspan="3"><s:textfield
						name="visitOther" placeholder="请输入其他"
						cssClass="form-control validate[required]" id="visitOther"></s:textfield></Td>
			</tr>

			<!-- 民俗 -->
			<%-- <tr class="customHidd hidd">
				<Td class="control-label"><label for="customOperateNum">经营户：</label>
				<input type="hidden" name="labelTexts" value="经营户" />
				</Td>
				<Td class="query_input"><s:textfield name="customOperateNum"
						placeholder="请输入经营户" cssClass="form-control validate[required]"
						id="customOperateNum"></s:textfield></Td>
			</tr> --%>
			<tr class="customHidd hidd">
				<Td class="control-label"><label for="customOperateNum">接待人次：</label>
					<input type="hidden" name="labelTexts" value="接待人次" /></Td>
				<Td class="query_input"><s:textfield name="customOperateNum"
						placeholder="请输入接待人次" cssClass="form-control validate[required]"
						id="pwd"></s:textfield></Td>
				<Td class="control-label"><label for="customSellAndProcess">总收入：</label>
					<input type="hidden" name="labelTexts" value="总收入" /></Td>
				<Td class="query_input"><s:textfield
						name="customSellAndProcess" placeholder="请输入总收入"
						cssClass="form-control validate[required]"
						id="customSellAndProcess"></s:textfield></Td>
			</tr>
			<tr class="customHidd hidd">
				<Td class="control-label"><label for="customSellAndProcess">出售和加工收入：</label>
					<input type="hidden" name="labelTexts" value="出售和加工收入" /></Td>
				<Td class="query_input"><s:textfield
						name="customSellAndProcess" placeholder="请输入出售和加工收入"
						cssClass="form-control validate[required]"
						id="customSellAndProcess"></s:textfield></Td>
				<Td class="control-label"><label for="customRepast">餐饮收入：</label>
					<input type="hidden" name="labelTexts" value="餐饮收入" /></Td>
				<Td class="query_input"><s:textfield name="customRepast"
						placeholder="请输入餐饮收入" cssClass="form-control validate[required]"
						id="customRepast"></s:textfield></Td>
			</tr>
			<tr class="customHidd hidd">
				<Td class="control-label" width="3%"><label for="customLive">住宿收入：</label>
					<input type="hidden" name="labelTexts" value="住宿收入" /></Td>
				<Td class="query_input" colspan="3"><s:textfield
						name="customLive" placeholder="请输入住宿收入"
						cssClass="form-control validate[required]" id="customLive"></s:textfield></Td>
			</tr>
			<!-- 旅游景区 -->
			<tr class="jingQuHidd hidd">
				<Td class="control-label"><label for="jingQuSellOther">接待人次：</label>
					<input type="hidden" name="labelTexts" value="接待人次" /></Td>
				<Td class="query_input"><s:textfield name="jingQuSellOther"
						placeholder="请输入接待人次" cssClass="form-control validate[required]"
						id="jingQuSellOther"></s:textfield></Td>
				<Td class="control-label"><label for="jingQuTicket">总收入：</label>
					<input type="hidden" name="labelTexts" value="总收入" /></Td>
				<Td class="query_input"><s:textfield name="jingQuTicket"
						placeholder="请输入总收入" cssClass="form-control validate[required]"
						id="jingQuTicket">
					</s:textfield>
			</tr>
			<tr class="jingQuHidd hidd">

				</Td>
				<Td class="control-label"><label for="jingQuTicket">门票收入：</label>
					<input type="hidden" name="labelTexts" value="门票收入" /></Td>
				<Td class="query_input"><s:textfield name="jingQuTicket"
						placeholder="请输入门票收入" cssClass="form-control validate[required]"
						id="jingQuTicket"></s:textfield></Td>
				<Td class="control-label"><label for="jingQuSellOther">出售其他商品收入：</label>
					<input type="hidden" name="labelTexts" value="出售其他商品收入" /></Td>
				<Td class="query_input"><s:textfield name="jingQuSellOther"
						placeholder="请输入出售其他商品收入"
						cssClass="form-control validate[required]" id="jingQuSellOther"></s:textfield></Td>
			</tr>
			<tr class="jingQuHidd hidd">
				<Td class="control-label"><label for="jingQuRepast">餐饮收入：</label>
					<input type="hidden" name="labelTexts" value="餐饮收入" /></Td>
				<Td class="query_input" colspan="3"><s:textfield
						name="jingQuRepast" placeholder="请输入餐饮收入"
						cssClass="form-control validate[required]" id="jingQuRepast"></s:textfield></Td>
			</tr>
			<!-- 旅游住宿 -->
			<tr class="liveHidd hidd">
				<Td class="control-label"><label for="liveSellOther">接待人次：</label>
					<input type="hidden" name="labelTexts" value="接待人次" /></Td>
				<Td class="query_input"><s:textfield name="liveSellOther"
						placeholder="请输入接待人次" cssClass="form-control validate[required]"
						id="liveSellOther"></s:textfield></Td>
				<Td class="control-label"><label for="liveHouseCost">总收入：</label>
					<input type="hidden" name="labelTexts" value="总收入" /></Td>
				<Td class="query_input"><s:textfield name="liveHouseCost"
						placeholder="请输入总收入" cssClass="form-control validate[required]"
						id="liveHouseCost"></s:textfield></Td>
			</tr>
			<tr class="liveHidd hidd">
				<Td class="control-label"><label for="liveHouseCost">房费：</label>
					<input type="hidden" name="labelTexts" value="房费" /></Td>
				<Td class="query_input"><s:textfield name="liveHouseCost"
						placeholder="请输入房费" cssClass="form-control validate[required]"
						id="liveHouseCost"></s:textfield></Td>
				<Td class="control-label"><label for="liveSellOther">出售其他商品收入：</label>
					<input type="hidden" name="labelTexts" value="出售其他商品收入" /></Td>
				<Td class="query_input"><s:textfield name="liveSellOther"
						placeholder="请输入出售商品收入" cssClass="form-control validate[required]"
						id="liveSellOther"></s:textfield></Td>
			</tr>
			<tr class="liveHidd hidd">
				<Td class="control-label" width="3%"><label for="liveRepast">餐饮收入：</label>
					<input type="hidden" name="labelTexts" value="餐饮收入" /></Td>
				<Td class="query_input" colspan="3"><s:textfield
						name="liveRepast" placeholder="请输入餐饮收入"
						cssClass="form-control validate[required]" id="liveRepast"></s:textfield></Td>
			</tr>
			<!-- 工业旅游 -->
			<tr class="factoryHidd hidd">
				<Td class="control-label"><label for="factoryTicket">接待人次：</label>
					<input type="hidden" name="labelTexts" value="接待人次" /></Td>
				<Td class="query_input"><s:textfield name="factoryTicket"
						placeholder="请输入接待人次" cssClass="form-control validate[required]"
						id="factoryTicket"></s:textfield></Td>
				<Td class="control-label"><label for="factorySellOther">总收入：</label>
					<input type="hidden" name="labelTexts" value="总收入" /></Td>
				<Td class="query_input"><s:textfield name="factorySellOther"
						placeholder="请输入总收入" cssClass="form-control validate[required]"
						id="factorySellOther"></s:textfield></Td>
			</tr>
			<tr class="factoryHidd hidd">
				<Td class="control-label"><label for="factoryTicket">门票收入：</label>
					<input type="hidden" name="labelTexts" value="门票收入" /></Td>
				<Td class="query_input"><s:textfield name="factoryTicket"
						placeholder="请输入门票收入" cssClass="form-control validate[required]"
						id="factoryTicket"></s:textfield></Td>
				<Td class="control-label"><label for="factorySellOther">出售商品收入：</label>
					<input type="hidden" name="labelTexts" value="出售商品收入" /></Td>
				<Td class="query_input"><s:textfield name="factorySellOther"
						placeholder="请输入出售商品收入" cssClass="form-control validate[required]"
						id="factorySellOther"></s:textfield></Td>
			</tr>
		</table>
	</form>
</body>
</html>
