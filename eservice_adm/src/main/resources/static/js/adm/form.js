var eserviceForm = {};

/**
 * 通用型POST.
 * 
 * @param {String} url
 * @param {Object} formData 傳送參數
 * @param {Function} afterFn 資料回來後的處理
 * @param {Function} beforeFn 檢核
 * @return true: 為空白或空字串
 */
eserviceForm.post = function (url, formData, afterFn, beforeFn) {
	if (beforeFn) {
		if (!beforeFn()) {
			return false;
		}
	}
	$.ajax({
		url : url,
		contentType: 'application/json',
		type: "POST",
		data:  JSON.stringify(formData),
		async: false
	}).done(function(response) {
		if (response.result == 'SUCCESS') {
			if (afterFn) {
				afterFn(response);
			}
		} else {
			if (response.resultMsg) {
				alertMsg(response.resultMsg);
			}
		}
	}).fail(function (jqXHR, textStatus) {
		alertMsg('系統發生錯誤');
	});
};


/**
 * Post form.
 *
 * @param {String} actionUrl 表單url
 * @param {Array} params 參數物件
 */
 function postWithParams(actionUrl, params) {
	popupLoading();
	
	var policyForm = $('<form>');
	$(policyForm).attr("action", actionUrl);
	$(policyForm).attr("method", "POST");
	
	$.each(params, function(i, paramObj) {
		$(policyForm).append($('<input>').attr('type', 'hidden').attr('name', paramObj.name).val(paramObj.value));
	})

	policyForm.appendTo(document.body)
	$(policyForm).submit();	
}

function postWithFormData(actionUrl, formData) {
	var params = [];
	for(var key in formData) {
		params.push({
			name: key,
			value: formData[key]
		});
	}
	postWithParams(actionUrl, params);
}