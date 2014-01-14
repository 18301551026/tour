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
	 isOperate();
})
function isOperate(){
	var sel=document.getElementById('allFactoryType');   
	var text=sel.options[sel.selectedIndex].text;
	if(text=='民俗旅游'){
		$(".op").removeClass('hidd');
	}else{
		$(".op").addClass('hidd');
	}
}