var eserviceForm = {};

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

/**
 * Post policyNo form.
 *
 * @param {String} actionUrl 表單url
 * @param {String} policyNo 保單號碼
 */
function postPolicyNo(actionUrl, policyNo) {
	popupLoading();
	
	var policyForm = $('<form>');
	$(policyForm).attr("action", actionUrl);
	$(policyForm).attr("method", "POST");

	var input = $('<input>').attr('type', 'hidden').attr('name', 'policyNo').val(policyNo);
	$(policyForm).append($(input));

	policyForm.appendTo(document.body)
	$(policyForm).submit();	
}

function postPolicyListType(actionUrl, policyNo, policyListType) {
	popupLoading();
	showLoadingModal();
	console.log("postPolicyListType:"+actionUrl +","+ policyNo+","+policyListType);
	var policyForm = $('<form>');
	$(policyForm).attr("action", actionUrl);
	$(policyForm).attr("method", "POST");

	var inputPolicyNo = $('<input>').attr('type', 'hidden').attr('name', 'policyNo').val(policyNo);
	$(policyForm).append($(inputPolicyNo));

	var inputPolicyListType = $('<input>').attr('type', 'hidden').attr('name', 'policyListType').val(policyListType);
	$(policyForm).append($(inputPolicyListType));

	policyForm.appendTo(document.body)
	$(policyForm).submit();	
}

/**
 * Post policyNo form with others.
 *
 * @param {String} actionUrl 表單url
 * @param {Array} policyNoList 保單清單
 */
function postPolicyNoWithParams(actionUrl, policyNo, params) {
	popupLoading();
	
	var policyForm = $('<form>');
	$(policyForm).attr("action", actionUrl);
	$(policyForm).attr("method", "POST");

	var input = $('<input>').attr('type', 'hidden').attr('name', 'policyNo').val(policyNo);
	$(policyForm).append($(input));
	
	$.each(params, function(i, paramObj) {
		$(policyForm).append($('<input>').attr('type', 'hidden').attr('name', paramObj.name).val(paramObj.value));
	})

	policyForm.appendTo(document.body)
	$(policyForm).submit();	
}


/**
 * Post policyNoList form.
 *
 * @param {String} actionUrl 表單url
 * @param {String[0,1]} policyNoList 保單號碼陣列
 */
function postPolicyNoList(actionUrl, policyNoList) {
	popupLoading();
	
	var policyForm = $('<form>');
	$(policyForm).attr("action", actionUrl);
	$(policyForm).attr("method", "POST");

	$.each(policyNoList, function(inx, obj) {
		var input =  $('<input>').attr('type', 'hidden').attr('name', 'policyNoList').val(obj);
		$(policyForm).append($(input));
	});

	policyForm.appendTo(document.body)
	$(policyForm).submit();	
}

/**
 * Post policyList form with others.
 *
 * @param {String} actionUrl 表單url
 * @param {String[Object]} policyList 保單物件陣列
 * @param {Array} params 其他參數物件
 */
function postPolicyNoListWithParams(actionUrl, policyList, params) {
	popupLoading();
	
	var dataForm = $('<form>');
	$(dataForm).attr("action", actionUrl);
	$(dataForm).attr("method", "POST");

	$.each(policyList, function(i, policyObj) {
		var input = $('<input>').attr('type', 'hidden').attr('name', "policyJson").val(JSON.stringify(policyObj));
		$(dataForm).append($(input));
	})

	$.each(params, function(i, paramObj) {
		$(dataForm).append($('<input>').attr('type', 'hidden').attr('name', paramObj.name).val(paramObj.value));
	})
	
	dataForm.appendTo(document.body)
	$(dataForm).submit();	
}

/**
 * 檢查欄位字串, 如果為空白或空字串, 則傳回 true
 * @param {String} value
 * @return true: 為空白或空字串
 */
eserviceForm.isBlank = function(value) {
	return (value == null) || value.replace( /^\s+|\s+$/g, '' ).length == 0 || /^\s+$/.test(value);
};

eserviceForm.stringformat = function () {
	if (arguments.length == 0) {
		return null;
	}
	var str = arguments[0];
	for ( var i = 1; i < arguments.length; i++) {
		var re = new RegExp('\\{' + (i - 1) + '\\}', 'gm');
		str = str.replace(re, arguments[i]);
	}
	return str;
};

/**
 * 轉換全形數字.
 * 
 * @param {String} value
 * @return 地址帶全形數字
 */
eserviceForm.convertAddressNumber = function(value) {
	return value.replace( /1/g, '１')
		.replace( /2/g, '２')
		.replace( /3/g, '３')
		.replace( /4/g, '４')
		.replace( /5/g, '５')
		.replace( /6/g, '６')
		.replace( /7/g, '７')
		.replace( /8/g, '８')
		.replace( /9/g, '９')
		.replace( /0/g, '０');
};

/**
 * 統編檢查
 */
eserviceForm.isValidGUI = function(taxId) {
	var invalidList = "00000000,11111111";
	if (/^\d{8}$/.test(taxId) == false || invalidList.indexOf(taxId) != -1) {
		return false;
	}

	var validateOperator = [1, 2, 1, 2, 1, 2, 4, 1],
		sum = 0,
		calculate = function(product) { // 個位數 + 十位數
			var ones = product % 10,
				tens = (product - ones) / 10;
			return ones + tens;
		};
	for (var i = 0; i < validateOperator.length; i++) {
		sum += calculate(taxId[i] * validateOperator[i]);
	}

	return sum % 10 == 0 || (taxId[6] == "7" && (sum + 1) % 10 == 0);
};

/**
 * 身分證號驗證
 */
eserviceForm.checkRocId = function(value) {
	var a = new Array('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', 'W', 'Z', 'I', 'O');
	var b = new Array(1, 9, 8, 7, 6, 5, 4, 3, 2, 1);
	var c = new Array(2);
	var d;
	var e;
	var f;
	var g = 0;
	var h = /^[a-z](1|2)\d{8}$/i;
	if (value.search(h) == -1) {
		return false;
	} else {
		d = value.charAt(0).toUpperCase();
		f = value.charAt(9);
	}
	for (var i = 0; i < 26; i++) {
		if (d == a[i]) {
			e = i + 10; //10
			c[0] = Math.floor(e / 10); //1
			c[1] = e - (c[0] * 10); //10-(1*10)
			break;
		}
	}
	for (var i = 0; i < b.length; i++) {
		if (i < 2) {
			g += c[i] * b[i];
		} else {
			g += parseInt(value.charAt(i - 1)) * b[i];
		}
	}
	if ((g % 10) == f) {
		return true;
	}
	if ((10 - (g % 10)) != f) {
		return false;
	}
	return true;
};

/**
 * 手機號碼驗證
 */
eserviceForm.checkMobile = function(mobile) {
	mobile = mobile.replace(/\s+/g, "");
	return /^09[0-9]{8}$/.test(mobile);
};

/**
 * 密碼驗證
 * 
 * 1.最低要求8字元
 * 2.最少符合下列四項中三項規則: 
 *   - 大寫英文字元
 *   - 小寫英文字元
 *   - 數字字元
 *   - 符號字元
 */
eserviceForm.checkPassword = function(value) {
	if (value == null) {
		return false;
	}
	
	if (value.length < 8) {
		return false;
	}
	
	// 滿足次數
	var matchCnt = 0;
	
	// 是否有符號
	if (/[\@\#\$\%\^\&\*  \_\+\!]/.test(value)) {
		matchCnt++;
	}
	// 是否有大寫
	if (/[A-Z]/.test(value)) {
		matchCnt++;
	}
	// 是否有小寫
	if (/[a-z]/.test(value)) {
		matchCnt++;
	}
	// 是否有數字
	if (/[0-9]/.test(value)) {
		matchCnt++;
	}
	
	return (matchCnt >= 3);
}

eserviceForm.isNumber = function(value) {
	if (/^\d+$/.test(value)) {
		return true;
	}
	return false;
};

eserviceForm.isEmail = function(value) {
	if (/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/.test(value)) {
		return true;
	}
	return false;
};

/**
 * 通用型POST.
 * 
 * @param {String} url
 * @param {Function} beforeFn 檢核
 * @param {Function} afterFn 資料回來後的處理
 * @return true: 為空白或空字串
 */
eserviceForm.post = function (url, beforeFn, afterFn) {
	if (beforeFn) {
		if (!beforeFn()) {
			return false;
		}
	}
	$.ajax({
		url : url,
		type : "POST"
	}).done(function(data) {
		if (data.result == 'SUCCESS') {
			if (afterFn) {
				afterFn(data.resultData);
			}
		} else {
			// error handle
		}
	}).fail(function (jqXHR, textStatus) {
		// alert('error!');
	});
};

eserviceForm.pwdCheck = function(pwdCheckSelector) {
	$(pwdCheckSelector).keyup(function () { 
		var strongRegex = new RegExp("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g"); 
		var mediumRegex = new RegExp("^(?=.{7,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g"); 
		var enoughRegex = new RegExp("(?=.{6,}).*", "g"); 
	
		if (false == enoughRegex.test($(this).val())) { 
			$('#pwdCheckLevelDiv').removeClass('pw-weak'); 
			$('#pwdCheckLevelDiv').removeClass('pw-medium'); 
			$('#pwdCheckLevelDiv').removeClass('pw-strong'); 
			$('#pwdCheckLevelDiv').addClass(' pw-defule'); 
			//密碼小於六位的時候，密碼強度圖片都為灰色 
		} 
		else if (strongRegex.test($(this).val())) { 
			$('#pwdCheckLevelDiv').removeClass('pw-weak'); 
			$('#pwdCheckLevelDiv').removeClass('pw-medium'); 
			$('#pwdCheckLevelDiv').removeClass('pw-strong'); 
			$('#pwdCheckLevelDiv').addClass(' pw-strong'); 
			//密碼為八位及以上並且字母數字特殊字符三項都包括,強度最強 
		} 
		else if (mediumRegex.test($(this).val())) { 
			$('#pwdCheckLevelDiv').removeClass('pw-weak'); 
			$('#pwdCheckLevelDiv').removeClass('pw-medium'); 
			$('#pwdCheckLevelDiv').removeClass('pw-strong'); 
			$('#pwdCheckLevelDiv').addClass(' pw-medium'); 
			//密碼為七位及以上並且字母、數字、特殊字符三項中有兩項，強度是中等 
		} 
		else { 
			$('#pwdCheckLevelDiv').removeClass('pw-weak'); 
			$('#pwdCheckLevelDiv').removeClass('pw-medium'); 
			$('#pwdCheckLevelDiv').removeClass('pw-strong'); 
			$('#pwdCheckLevelDiv').addClass('pw-weak'); 
			//如果密碼為6為及以下，就算字母、數字、特殊字符三項都包括，強度也是弱的 
		} 
		return true; 
	}); 
};
