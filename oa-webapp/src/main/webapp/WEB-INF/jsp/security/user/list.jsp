<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>用户</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<%@ include file="/common/include-bootstap.jsp"%>
<script src="${ctx }/js/grid.js"></script>
<%@ include file="/common/include-styles.jsp"%>
</head>
<body>
	<div class="panel panel-info">
		<div class="panel-heading">
			<div class="btn-group btn-group-sm">
				<button id="addButton"
					actionUrl="${ctx }/security/user!toAdd.action" class="btn btn-info">
					<span class="glyphicon glyphicon-plus"></span> 新建
				</button>
				<button id="deleteButton" class="btn btn-info">
					<span class="glyphicon glyphicon-minus"></span> 删除
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
		<div class="panel-body" id="queryPanel">
			<form role="form" id="queryForm" class="form-horizontal"
				action="${ctx}/security/user!findPage.action" method="post">
				<table class="formTable">
					<Tr>
						<Td class="control-label"><label for="userName">用户名称：</label></Td>
						<Td class="query_input"><s:textfield name="userName"
								cssClass="form-control" id="userName"></s:textfield></Td>
						<Td class="control-label" style="width: 5%"><label
							for="address">企业名称：</label></Td>
						<Td class="query_input"
						
						>
						<s:textfield name="factoryName" cssClass="form-control" ></s:textfield>
						</Td>
					</Tr>
					
					<tr>
						<c:if test="${empty deptLevel||deptLevel!='镇级'}">
					<Td class="control-label" style="width: 5%"><label
							for="address">镇：</label></Td>
						<Td class="query_input" colspan="3">
						<s:select list="districtTown"  cssClass="form-control"
						listKey="id" listValue="text"
						name="townId" headerKey="" headerValue="全部"
						></s:select>
						</Td>
					</c:if>	
					</tr>
				</table>
			</form>
		</div>
	</div>
	<form method="post" action="${ctx }/security/user!delete.action"
		id="deleteForm">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th class="table_checkbox"><input type="checkbox"
						id="checkAllCheckBox"></th>
					<th>登陆名</th>
					<th>真实姓名</th>
					<th>所属镇</th>
					<th>部门</th>
					<td>部门级别</td>
					<th>角色</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="#page.result">
					<tr>
						<td class="table_checkbox"><input type="checkbox" name="ids"
							value="${id }" /></td>
						<td>${userName }</td>
						<td>${realName }</td>
						<Td>
						<c:if test="${dept.deptLevel=='企业' }">
							${dept.parent.text }
						</c:if>
						<c:if test="${dept.deptLevel!='企业' }">
							----
						</c:if>
						</Td>
						<td>${dept.text }</td>
						<Td>${dept.deptLevel }</Td>
						<td><c:forEach items="${roles }" var="j">
				          	${j.roleName}&nbsp;
				          </c:forEach></td>
						<td><a href="${ctx}/security/user!toUpdate.action?id=${id}">修改</a>
						</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</form>
	<tags:pagination page="${page }"></tags:pagination>

</body>
</html>
