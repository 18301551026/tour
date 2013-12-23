$(function() {
	$("#addDeptForm").form({
		url : ctx + '/security/dept!addDept.action',
		onSubmit : function() {
			if ($("#addDeptForm").form("validate")) {
				return true;
			} else {
				return false;
			}
		},
		success : function(r) {
			if (!r) {
				return;
			}
			var jsonObj = jQuery.parseJSON(r);

			parent.$.modalDialog.openner_treeGrid.treegrid('append', {
				parent : jsonObj.pid,
				data : [ {
					id : jsonObj.id,
					text : jsonObj.text,
					deptType:jsonObj.deptType,
					deptLevel:jsonObj.deptLevel,
					deptDesc : jsonObj.deptDesc
				} ]

			});
			$.messager.show({
				msg : '添加成功',
				title : '提示'
			});
			parent.$.modalDialog.handler.dialog('close');
			// parent.$.modalDialog.openner_treeGrid.treegrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_treeGrid这个对象，是因为role.jsp页面预定义好了

		}
	});

	$("#deptLevel")
			.bind(
					"change",
					function() {
						var v = $(this).val();
						var html = '<br  class="t" /> <label  class="t" >类型：</label> <select name="deptType"'
								+ 'class="form-control t">'
								+ '<option value="观光园">观光园</option>'
								+ '<option value="民俗旅游">民俗旅游</option>'
								+ '<option value="旅游住宿">旅游住宿</option>'
								+ '<option value="旅游风景">旅游风景</option>'
								+ '<option value="工业旅游">工业旅游</option>'
								+ '</select>';
						if (v == "企业") {
							$("#deptLevel").after(html);
						} else {
							$(this).next(".t").remove();
							$(this).next(".t").remove();
							$(this).next(".t").remove();
						}

					});
});
