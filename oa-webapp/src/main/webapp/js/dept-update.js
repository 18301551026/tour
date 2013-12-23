$(function() {
	$("#updateDeptForm").form({
		url : ctx + '/security/dept!updateDept.action',
		onSubmit : function() {
			if ($("#updateDeptForm").form("validate")) {
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
			parent.$.modalDialog.openner_treeGrid.treegrid('update', {
				id : jsonObj.id,
				row : {
					id : jsonObj.id,
					text : jsonObj.text,
					deptType : jsonObj.deptType,
					deptLevel : jsonObj.deptLevel,
					deptDesc : jsonObj.deptDesc
				}
			})
			$.messager.show({
				msg : '修改成功',
				title : '提示'
			});
			parent.$.modalDialog.handler.dialog('close');
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
})