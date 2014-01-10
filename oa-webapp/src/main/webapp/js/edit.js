$(function() {
	$("#saveButton").click(function() {
		$("#editForm").submit();
	});
	$("#resetButton").click(function() {
		var url = $(this).attr("actionUrl");
		if (url) {
			location.href = url;
		} else {

			/* $("#editForm").reset(); */
			$("#editForm :input").val('');
		}

	});
	$("#backButton").click(function() {
		history.go(-1);
	});

	$("#otherButton").click(function() {
		var url = $(this).attr("actionUrl");
		$("#editForm").attr("action", url);
		$("#editForm").submit();
	})
	$("#turnToOther").click(function() {
		var url = $(this).attr("actionUrl");
		location.href = url;
	})
	jQuery("#editForm").validationEngine();
	jQuery('#editForm input').attr('data-prompt-position','topLeft');
	jQuery('#editForm input').data('promptPosition','topLeft');
	jQuery('#editForm textarea').attr('data-prompt-position','topLeft');
	jQuery('#editForm textarea').data('promptPosition','topLeft');
	jQuery('#editForm select').attr('data-prompt-position','topLeft');
	jQuery('#editForm select').data('promptPosition','topLeft');
});
