<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>选择接收人</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<%@ include file="/common/include-jquery-easyui.jsp"%>
</head>
<body>
	<script type="text/javascript" src="${ctx }/js/phoneNotify-selectReceiveUsers.js"></script>
		
	<div class="easyui-layout" data-options="fit:true,border:false">
		<s:hidden id="ids" name="receiveUserIds"></s:hidden>
		<s:hidden name="receiveUsersName" id="receiveUsersName"></s:hidden>
		<div data-options="region:'west'" class="easyui-accordion"
			style="width: 150px;" data-options="border:false">
			<div title="企业类型">
				<select style="width: 144px;" onchange="selectDeptToShowUserfn()"
					id="selectDeptToShowUser">
					<c:forEach items="${typeList }" var="t">
						<option value="${t.id }">${t.name }</option>
					</c:forEach>
				</select> <select multiple="multiple" id="seleceUserByDept"
					style="width: 144px; height: 91%; border: none;">
				</select>
			</div>
			<div title="镇列表" data-options="selected:true,border:false">
				<select multiple="multiple" id="allDept"
					style="width: 144px; height: 96%; border: none;">
					<c:forEach items="${townList }" var="t">
						<option value="t${t.id }">${t.text }</option>
					</c:forEach>
				</select>
			</div>
			<div title="企业" style="border: none;">
				<input id="searchuserbox" class="easyui-searchbox"
					style="width: 144px;"></input> <select multiple="multiple"
					class="selectUser" id="allUsers"
					style="width: 144px; height: 91%; border: none;">
				</select>
			</div>
		</div>
		<div data-options="region:'center'">
			<select multiple="multiple" id="selectedUsers"
				style="width: 132px; height: 97%; border: none;">
			</select>
		</div>
	</div>

</body>
</html>