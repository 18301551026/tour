$(function() {
	treeGrid = $('#treeGrid').treegrid({
		url : ctx + '/security/dept!getAllDept.action',
		idField : 'id',
		treeField : 'text',
		/* parentField : 'pid', */
		rownumbers : true,
		fit : true,
		animate : true,
		fitColumns : true,
		border : false,
		nowrap : true,
		columns : [ [ {
			field : 'text',
			title : '部门名称',
			width : 280
		}, {
			field : 'deptType',
			title : '部门类型',
			width : 280
		}, {
			field : 'deptLevel',
			title : '部门级别',
			width : 280
		}, {
			field : 'deptDesc',
			title : '描述',
			width : 350
		} ] ],
		toolbar : "#treeGridToolBar",
		onContextMenu : onContextMenu
	});
});
function deleteFun(id) {
	if (id != undefined) {
		treeGrid.treegrid('select', id);
	}
	var node = treeGrid.treegrid('getSelected');
	if (node) {
		parent.$.messager.confirm('询问', '您是否要删除当前节点？', function(b) {
			if (b) {
				$.ajax({
					type : "POST",
					url : ctx + '/security/dept!deleteDept.action',
					data : "id=" + node.id,
					success : function(msg) {
						if (msg) {
							$.messager.show({
								msg : '删除成功',
								title : '提示'
							});
							treeGrid.treegrid('remove', node.id);
						}
					}
				});
			}
		});
	}
}

function editFun(id) {
	if (id != undefined) {
		treeGrid.treegrid('select', id);
	}
	var node = treeGrid.treegrid('getSelected');
	if (node) {
		parent.$.modalDialog({
			title : '编辑节点',
			width : 400,
			height : 330,
			href : ctx + '/security/dept!toUpdate.action?id=' + node.id,
			buttons : [
					{
						text : '编辑',
						iconCls : "icon-edit",
						handler : function() {
							parent.$.modalDialog.openner_treeGrid = treeGrid;// 因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
							var f = parent.$.modalDialog.handler
									.find('#updateDeptForm');
							f.submit();
						}
					}, {
						text : '取消',
						iconCls : "icon-cancel",
						handler : function() {
							parent.$.modalDialog.handler.dialog('close');
						}
					} ]
		});
	}
}

function addFun(flag) {
	var url
	if (flag) {
		url = ctx + '/security/dept!toAdd.action';
	} else {
		var node = treeGrid.treegrid('getSelected');
		url = ctx + '/security/dept!toAdd.action?id=' + node.id
	}
	parent.$.modalDialog({
		title : '添加节点',
		width : 400,
		height : 330,
		href : url,
		buttons : [ {
			text : '添加',
			iconCls : "icon-add",
			handler : function() {
				parent.$.modalDialog.openner_treeGrid = treeGrid;// 因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
				var f = parent.$.modalDialog.handler.find('#addDeptForm');
				f.submit();
			}
		}, {
			text : '取消',
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}

function redo() {
	var node = treeGrid.treegrid('getSelected');
	if (node) {
		treeGrid.treegrid('expandAll', node.id);
	} else {
		treeGrid.treegrid('expandAll');
	}
}
function undo() {
	var node = treeGrid.treegrid('getSelected');
	if (node) {
		treeGrid.treegrid('collapseAll', node.id);
	} else {
		treeGrid.treegrid('collapseAll');
	}
}
function onContextMenu(e, row) {
	e.preventDefault();
	$(this).treegrid('select', row.id);
	$('#mm').menu('show', {
		left : e.pageX,
		top : e.pageY
	});
}