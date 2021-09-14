$.fn.extend({
	validation: function(options) {
		var checkGroup = [];
		this.find('[data-validation]').each(function(index, obj) {
			var $element = $(obj);
			var	validation = $element.data('validation');
			
			if ($element.prop('disabled') || $element.prop('readonly')) {
				return;
			}

			$element.on('change blur', function() {
				$div = $element.closest('div');
				if ($div.hasClass("error")) {
					clearTimeout($element.data('validator-timer'));
					$.validator.checkValid(this, validation);
				}
			});

			$element.on('keyup', function() {
				$div = $element.closest('div');
				if ($div.hasClass("error")) {
					var _this = this, timer;
					
					clearTimeout($element.data('validator-timer'));
					timer = setTimeout(function() {
						$.validator.checkValid(_this, validation);
					}, $.validator.delay);

					$element.data('validator-timer', timer);
				}
			});

			checkGroup.push({
				element: $element,
				validation: validation
			});
		});

		this.data('checkGroup', checkGroup);
	},
	valid: function() {
		var checkGroup = this.data('checkGroup');
		var	validateResult;
		var	firstInvalid;
		
		// 若沒加入檢核訊息，就回傳true
		if (!checkGroup && (typeof checkGroup === 'undefined')) {
			return true;
		}

		for (var i = 0; i < checkGroup.length; i++) {
			validateResult = $.validator.checkValid(checkGroup[i].element, checkGroup[i].validation, true);
			
			if (!validateResult && (typeof firstInvalid === 'undefined')) {
				firstInvalid = i;
			}
		}

		if (typeof firstInvalid === 'number') {
			checkGroup[firstInvalid].element.focus();
			return false;
		} else {
			return true;
		}
	}
});

$.extend({
	validator: {
		delay: 500,
		extend: function(options) {
			$.extend(true, this, options);
		},
		validRender: function($element) {
			$div = $element.closest('div');
			
			var errMsg = '';
			$.each($div.find('[data-error]'), function(i, errObj) {
				errMsg += $(errObj).text();
			});
			if (errMsg == '') {
				$div.removeClass('error');
			}
			
			if($element.attr("type") === "radio"){
				var labelId = $element.attr("name") + '_error';;
				var labelName = name + '_error';
				$('#' + labelId).remove();
			}

			var labelId = $element.attr('id') + '_error';
			$('#' + labelId).remove();
		},
		invalidRender: function($element, message) {
			$div = $element.closest('div');
			$div.addClass('error');
			
			var labelId = $element.attr('id') + '_error';
			
			if($element.attr("type") === "radio"){
				var name = $element.attr("name");
				labelId = name + '_error';
			}
			
			if ($('#' + labelId).size() > 0) {
				$('#' + labelId).text(message);
			} else {
				$errorMsg = $('<label data-error id="' + labelId + '" style="display: inline-block; color: red; padding-left: 10px">' + message + '</label>');
				if ($element[0].tagName === "SELECT") {
					$errorMsg.insertAfter($div);
				} else if($element.attr("type") === "radio"){
						$label = $("input[name="+name+"]").last("input").next('label');
						$errorMsg.insertAfter($label);
				} else {
					$errorMsg.insertAfter($element);
				}
			}
		},
		rules: {
			required: function(value, params, $element) {
				// 是否為空值
				if($element.attr("type") == "radio"){
					var name = $element.attr('name');
					value = $("input[name="+name+"]:checked").val();
				}
				
				if (value == $element.attr('placeholder')) {
					value = '';
				}
				
				return !!value.length ? true : $.validator.messages['required'];
			},
			optionalRequired: function(value, idSelectors, $element) {
				// 是否為空值 須完整輸入
				var inputed = true;
				$(idSelectors).each(function(i, obj) {
					if ($(obj).val() != $(obj).attr('placeholder')) {
						if ($(obj).val() == '') {
							inputed = false;
							return;
						}
					}
				});
				return inputed ? true : $.validator.messages['optionalRequired'];
			},
			optionalRequiredOne: function(value, idSelectors, $element) {
				// 有一項輸入即可
				var inputed = false;
				$(idSelectors).each(function(i, obj) {
					if ($(obj).val() != $(obj).attr('placeholder')) {
						if ($(obj).val() != '') {
							inputed = true;
							return;
						}
					}
				});
				return inputed ? true : $.validator.messages['optionalRequiredOne'];
			},
			selectRequired: function(value, $option, $element) {
				var inputed = false;
				var opt = $option.split(",");
				if($(opt[0]).val() != opt[1]){
					inputed = true;
				}else{
					if(value != ''){
						inputed = true;
					}
				}
				return inputed ? true : $.validator.messages['selectRequired'];
			},
			address: function(value) {
				return !!value.length ? true : $.validator.messages['address'];
			},
			integer : function(value) {
				return (eserviceForm.isBlank(value) || /^\d+$/.test(value)) ? true : $.validator.messages['integer'];
			},
			max : function(value, id) {
				//var ids = id.split(',');
				return (eserviceForm.isBlank(value) || value >= id) ? true : eserviceForm.stringformat($.validator.messages['max'], id);
			},
			min : function(value, id) {
				return (eserviceForm.isBlank(value) || value <= id) ? true : eserviceForm.stringformat($.validator.messages['min'], id);
			},
			date : function(value, id) {
				return (eserviceForm.isBlank(value) ||  /^(\d{4})\/(0\d{1}|1[0-2])\/(0\d{1}|[12]\d{1}|3[01])$/.test(value)) ? true : eserviceForm.stringformat($.validator.messages['date'], $(id).val());
			},
			time :function(value, id){
				return (eserviceForm.isBlank(value) || /^([0-1]{1}[0-9]{1}|[2][0-3]):[0-5]{1}[0-9]{1}$/.test(value)) ? true : $.validator.messages['time'];
			},
			checkDateTime : function(value, idList) {
				var date = idList.split(',');
					
				var startDate = new Date($(date[0]).val().replace(/\-/g, "\/")+" "+$(date[1]).val()); 
				var endDate = new Date($(date[2]).val().replace(/\-/g, "\/")+" "+$(date[3]).val()); 
				
				return (eserviceForm.isBlank(value) || (endDate.getTime() > startDate.getTime())) ? true : $.validator.messages['checkDateTime'];
			},
			rangeNum : function(value, ids) {
				var minId = ids.split(',')[0];
				var maxId = ids.split(',')[1];
				return (eserviceForm.isBlank(value) || value <= $(id).val()) ? true : eserviceForm.stringformat($.validator.messages['rangeNum'], minId, maxId);
			},
			mail : function(value) {
				return (eserviceForm.isBlank(value) || /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/.test(value)) ? true : $.validator.messages['mail'];
			},
			alphabet : function(value) {
				return (eserviceForm.isBlank(value) || /^[A-Za-z]+$/.test(value)) ? true : $.validator.messages['alphabet'];
			},
			rocId : function(pid) {
				if (pid.length == 8) {//統編檢查
					return eserviceForm.isValidGUI(pid) ? true : $.validator.messages['taxId'];
				} else {
					return eserviceForm.checkRocId(pid) ? true : $.validator.messages['rocId'];
				}
			},
			telphone : function(value) {
				return (eserviceForm.isBlank(value) || /[0-9]{2}\-[0-9]{7}/.test(value)) ? true : $.validator.messages['telphone'];
			},
			telphone2 : function(value) {
				return (eserviceForm.isBlank(value) || /[0-9]{9}/.test(value)) ? true : $.validator.messages['telphone2'];
			},
			mobile : function(value) {
				return (eserviceForm.isBlank(value) || /^09[0-9]{8}$/.test(value)) ? true : $.validator.messages['mobile'];
			},
			phone : function(value) {
				return (eserviceForm.isBlank(value) || /^09[0-9]{8}$/.test(value) || /[0-9]{2}\-[0-9]{7}/.test(value)) ? true : $.validator.messages['phone'];
			},
			maxlength : function(value, param) {
				return (value.length <= param) ? true : eserviceForm.stringformat($.validator.messages['maxlength'], param);
			},
			minlength : function(value, param) {
				return (value.length >= param) ? true : eserviceForm.stringformat($.validator.messages['minlength'], param);
			},
			inputRange : function(value, param) {
				var start = param.split(',')[0];
				var end = param.split(',')[1];
				return ((value.length >= start) && (value.length <= end)) ? true : eserviceForm.stringformat($.validator.messages['inputRange'], start, end);
			}
		},
		messages: {
			required: '此欄位為必須!',
			optionalRequired: '請輸入完整資訊!',
			optionalRequiredOne: '請擇一輸入!',
			selectRequired: '此欄位為必須!',
			integer: '請輸入數字',
			min: '必須小於{0}',
			max: '必須大於{0}',
			date: '請填入正確日期(格式為yyyy/MM/dd)',
			time:'請填入正確時間(格式為HH:MM)',
			checkDateTime: '開始日期必須小於結束日期',
			rangeNum: '必須介於 {0} 與 {1} 之間',
			mail: '請填寫正確的電子郵件',
			alphabet: '請輸入英文字母',
			rocId: '身分證不正確',
			taxId: '統編不正確',
			mobile: '請輸入09開頭且長度至少為10碼的正確格式',
			telphone: '請輸入正確格式 XX-XXXXXXXX',
			telphone2: '請輸入包含區碼且長度至少為9碼的正確格式',
			phone: '請輸入正確的電話號碼(XX-XXXXXXXX)或手機號碼(XXXXXXXXXX)',
			maxlength: '長度不能大於{0}',
			minlength: '長度不能小於{0}',
			inputRange: '長度需要介於 {0} 與 {1} 之間'
		},
		checkValid: function(element, validation, _check) {
			var $element = $(element),
				value = $.trim($element.val()),
				i, ruleName, params, message,
				rules = validation.split('|');

			for (i = 0; i < rules.length; i++) {
				ruleName = rules[i].split('::')[0];
				params = rules[i].split('::')[1];
				custMessage = rules[i].split('::')[2];
				
				message = $.validator.rules[ruleName](value, params, $element, _check);

				if (typeof message === 'string') {
					if (ruleName == 'optionalRequired' || ruleName == 'optionalRequiredOne') {
						$(params).each(function(i, obj) {
							$(obj).closest('div').addClass('error');
						});
						var optionalLength = params.split(',').length;
						$element = $(params.split(',')[optionalLength - 1]);
					}

					if (typeof custMessage === 'string') {
						message = custMessage;
					}

					$.validator.invalidRender($element, message);
					
					return false;
				}
			}
			
			if (ruleName == 'optionalRequired' || ruleName == 'optionalRequiredOne') {
				$(params).each(function(i, obj) {
					$(obj).closest('div').removeClass('error');
					$('#' + $(obj).attr('id') + '_error').remove();
				});
			} else {
				$.validator.validRender($element);
			}
			return true;
		}
	}
});