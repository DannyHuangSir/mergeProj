/**
 * 顯示西元年(yyyy/mm/dd)
 *
 * @param {Object} obj 需要判斷的屬性物件
 */
function westDate(obj) {
	return (obj == null ? '' : $.datepicker.formatDate('yy/mm/dd', new Date(obj)))
}

/**
 * 顯示民國年(yyy/mm/dd)
 *
 * @param {Object} obj 需要判斷的屬性物件
 */
function twDate(obj) {
	if (obj == null) {
		return '';
	}
	
	var yyymmdd = '';
	try {
		var mmdd = $.datepicker.formatDate('mm/dd', new Date(obj));
		var yyyy = $.datepicker.formatDate('yy', new Date(obj));
		var yyy = (parseInt(yyyy, 10) - 1911);
		if (yyy < 100) {
			yyy = '0' + yyy;
		}
		yyymmdd = (parseInt(yyyy, 10) - 1911) + '/' + mmdd;
	} catch (e) {
		yyymmdd = '';
	}
	
	return yyymmdd;
}

/**
 * 顯示西元年月份(yyyy/mm)
 *
 * @param {Object} obj 需要判斷的屬性物件
 */
function westMonth(obj) {
	return (obj == null ? '' : $.datepicker.formatDate('yy/mm', new Date(obj)))
}

/**
 * 顯示西元年(yyyy)
 *
 * @param {Object} obj 需要判斷的屬性物件
 */
function westYear(obj) {
	return (obj == null ? '' : $.datepicker.formatDate('yy', new Date(obj)))
}

/**
 * 如果物件為null，顯示空字串
 *
 * @param {Object} obj 需要判斷的屬性物件
 */
function emptyIfNull(obj) {
	return (obj == null ? '' : obj)
}

/**
 * 如果物件為null，顯示自訂的預設值
 *
 * @param {Object} obj 需要判斷的屬性物件
 * @param {String} defaultValue 預設值
 */
function emptyDefaultValue(obj, defaultValue) {
	return (obj == null ? defaultValue : obj)
}

/**
 * 顯示數字(小數點字型會稍微小)
 *
 * @param {Object} obj 需要判斷的屬性物件
 * @param {Integer} fractionDigits 小數點後最小的位數
 */
function showNumberPointValue(obj, fractionDigits) {
	var resultVal = '<span class="tablesaw-cell-content"></span>';
	if (obj == null) {
		return resultVal;
	}
	
	if (!(typeof obj === 'string')) {
		if (obj.toString().indexOf(".") != -1) {
			resultVal = '<span class="tablesaw-cell-content">' + formatNumber(obj.toString().split('.')[0], 0) + '.<span class="point">' + rpad(fractionDigits, obj.toString().split('.')[1], '0') + '</span></span>';
		} else {
			resultVal = '<span class="tablesaw-cell-content">' + obj + '</span>';
		}
	}
	return resultVal;
}

/**
 * 數字格式化
 *
 * @param {Object} obj 需要判斷的屬性物件
 * @param {Integer} fractionDigits 小數點後最小的位數
 */
function formatNumber(obj, fractionDigits) {
	if (obj == null) {
		return '';
	}
	
	return accounting.formatNumber(obj, fractionDigits);
}

/**
 * 向右補滿
 *
 * @param {Integer} num 最大右補位數
 * @param {String} string 需要補位的字串
 * @param {String} char 填充字串
 */
function rpad(num, string, char) {
	if (!string || !char || string.length >= num) {
		return string;
	}
	var max = (num - string.length) / char.length;
	for (var i = 0; i < max; i++) {
		string += char;
	}
	return string;
}
