var eserviceAdmOption = function() {
	function commonOptions($target, url, msg, parameterType, callback) {
		$target.html('');
		$.ajax({
			url : url,
			type : "POST",
			async: false,
		}).done(function(data) {
		    console.log("execute commonOptions ajax done : "  + $target.attr("id") + " url: " + url)
			if (data.result == 'SUCCESS') {
				var optHtml = '';
				if (msg) {
					optHtml = '<option value="">' + msg + '</option>';
				} else {
					optHtml = '<option value="">請選擇</option>';
				}
				$.each(data.resultData, function(i, obj) {
					// 預設用parameterCode
					var optionValue = obj.parameterCode;
					if (parameterType == 'VALUE') {
						optionValue = obj.parameterValue;
					}
					optHtml += ('<option value="' + optionValue + '">' + obj.parameterName + '</option>');
				});
				$target.html(optHtml);
			}
			if (callback) {
				callback();
			}
		});
	};
	function chosenCommonOptions($target, url, parameterType, callback) {
		$.ajax({
			url : url,
			type : "POST"
		}).done(function(data) {
			if (data.result == 'SUCCESS') {
				$target.find('option').remove();
				$target.append($('<option></option>').attr('value', ''));
				$.each(data.resultData, function(i, obj) {
					// 預設用parameterCode
					var optionValue = obj.parameterCode;
					if (parameterType == 'VALUE') {
						optionValue = obj.parameterValue;
					}
					$target.append($('<option></option>').attr('value', optionValue).text(obj.parameterName));
				});
				$target.trigger('chosen:updated');
			}
			if (callback) {
				callback();
			}
		});
	};
	function systemOptions($target, url, msg, callback) {
		$target.html('');
		$.ajax({
			url : url,
			type : "POST"
		}).done(function(data) {
			if (data.result == 'SUCCESS') {
				var optHtml = '';
				if (msg) {
					optHtml = '<option value="">' + msg + '</option>';
				} else {
					optHtml = '<option value="">請選擇系統別...</option>';
				}
				$.each(data.resultData, function(i, obj) {
					optHtml += ('<option value="' + obj.sysId + '">' + obj.sysName + '</option>');
				});
				$target.html(optHtml);
			}
			if (callback) {
				callback();
			}
		});
	};
	function deptOptions($target, url, callback) {
		$target.html('');
		$.ajax({
			url : url,
			type : "POST"
		}).done(function(data) {
			if (data.result == 'SUCCESS') {
				var optHtml = '<option value="">請選擇部門...</option>';
				$.each(data.resultData, function(i, obj) {
					optHtml += ('<option value="' + obj.depId + '">' + obj.depName + '</option>');
				});
				$target.html(optHtml);
			}
			if (callback) {
				callback();
			}
		});
	};
	function jobTitleOptions($target, url, callback) {
		$target.html('');
		$.ajax({
			url : url,
			type : "POST"
		}).done(function(data) {
			if (data.result == 'SUCCESS') {
				var optHtml = '<option value="">請選擇職位...</option>';
				$.each(data.resultData, function(i, obj) {
					optHtml += ('<option value="' + obj.titleId + '">' + obj.titleName + '</option>');
				});
				$target.html(optHtml);
			}
			if (callback) {
				callback();
			}
		});
	};
	function roleOptions($target, url, callback) {
		$target.html('');
		$.ajax({
			url : url,
			type : "POST"
		}).done(function(data) {
			if (data.result == 'SUCCESS') {
				var optHtml = '<option value="">請選擇角色...</option>';
				$.each(data.resultData, function(i, obj) {
					optHtml += ('<option value="' + obj.roleId + '">' + obj.roleName + '</option>');
				});
				$target.html(optHtml);
			}
			if (callback) {
				callback();
			}
		});
	};
	function chosenSystemOptions($target, url, callback) {
		$.ajax({
			url : url,
			type : "POST"
		}).done(function(data) {
			if (data.result == 'SUCCESS') {
				$target.find('option').remove();
				$target.append($('<option></option>').attr('value', ''));
				$.each(data.resultData, function(i, obj) {
					$target.append($('<option></option>').attr('value', obj.sysId).text(obj.sysName));
				});
				$target.trigger('chosen:updated');
			}
			if (callback) {
				callback();
			}
		});
	};
	function parameterCategoryOptions($target, url, callback) {
		console.log(url);
		$.ajax({
			url : url,
			type : "POST"
		}).done(function(data) {
			if (data.result == 'SUCCESS') {
				$target.find('option').remove();
				$target.append($('<option></option>').attr('value', ''));
				$.each(data.resultData, function(i, obj) {
					$target.append($('<option></option>').attr('value', obj.parameterCategoryId).text(obj.categoryName));
				});
				$target.trigger('chosen:updated');
			}
			if (callback) {
				callback();
			}
		});
	};
	function parameterOptions($target, url, callback) {
		$.ajax({
			url : url,
			type : "POST"
		}).done(function(data) {
			if (data.result == 'SUCCESS') {
				//console.log("parameterOptions=", data);
				$target.find('option').remove();
				$target.append($('<option></option>').attr('value', ''));
				$.each(data.resultData, function(i, obj) {
					$target.append($('<option></option>').attr('value', obj.parameterId).text(obj.parameterName));
				});
				setInterval(function(){ $target.trigger('chosen:updated'); }, 1000);
				
			}
			if (callback) {
				callback();
			}
		});
	};
	function msgTempParameterOptions($target, url, callback) {
		$.ajax({
			url : url,
			type : "POST"
		}).done(function(data) {
			if (data.result == 'SUCCESS') {
				//console.log("msgTempParameterOptions=", data.resultData);
				$target.find('option').remove();
				$target.append($('<option></option>').attr('value', ''));
				$.each(data.resultData, function(i, obj) {
					$target.append($('<option></option>').attr('value', obj.parameterId+"^"+obj.parameterValue).text(obj.parameterName));
				});
				$target.trigger('chosen:updated');
			}
			if (callback) {
				callback();
			}
		});
	};
	function chosenCommTempCodeOptions($target, url, callback) {
		$.ajax({
			url : url,
			type : "POST"
		}).done(function(data) {
			if (data.result == 'SUCCESS') {
				$target.find('option').remove();
				$target.append($('<option></option>').attr('value', ''));
				$.each(data.resultData, function(i, obj) {
					$target.append($('<option></option>').attr('value', obj.messagingTemplateCode).text(obj.messagingTemplateCode));
				});
				$target.trigger('chosen:updated');
			}
			if (callback) {
				callback();
			}
		});
	};
	function chosenRoleOptions($target, url, callback) {
		$.ajax({
			url : url,
			type : "POST"
		}).done(function(data) {
			if (data.result == 'SUCCESS') {
				$target.find('option').remove();
				$target.append($('<option></option>').attr('value', ''));
				$.each(data.resultData, function(i, obj) {
					$target.append($('<option></option>').attr('value', obj.roleId).text(obj.roleName));
				});
				$target.trigger('chosen:updated');
			}
			if (callback) {
				callback();
			}
		});
	};
	function chosenCommTempNameOptions($target, url, callback) {
		$.ajax({
			url : url,
			type : "POST"
		}).done(function(data) {
			if (data.result == 'SUCCESS') {
				$target.find('option').remove();
				$target.append($('<option></option>').attr('value', ''));
				$.each(data.resultData, function(i, obj) {
					$target.append($('<option></option>').attr('value', obj.messagingTemplateName).text(obj.messagingTemplateName));
				});
				$target.trigger('chosen:updated');
			}
			if (callback) {
				callback();
			}
		});
	};
	function emptyOptions($target, defaultValue) {
		$target.html('');
		$target.html('<option value="">' + defaultValue + '</option>');
	};
	function relationOption($target, url, selectVal) {
    		$target.html('');
    		$.ajax({
    			url : url,
    			type : "POST"
    		}).done(function(data) {
    			if (data.result == 'SUCCESS') {
    				var optHtml = '';
    				$.each(data.resultData, function(i, obj) {
    				    if (obj.parameterValue == selectVal) {
    				    optHtml += ('<option selected value="' + obj.parameterValue + '">' + obj.parameterName + '</option>');
    				    } else {
        					optHtml += ('<option value="' + obj.parameterValue + '">' + obj.parameterName + '</option>');
    				    }
    				});
    				$target.html(optHtml);
    			}
    		});
    	};
	return {
		common : function(target, url, msg, parameterType, callback) {
		    console.log("execute common: "  + target + " url: " + url)
			if ((typeof url === 'undefined')) {
				emptyOptions($(target), msg);
			} else {
				commonOptions($(target), url, msg, parameterType, callback);
			}
		},
		empty : function(target, defaultValue) {
			emptyOptions($(target), defaultValue);
		},
		system : function(target, url, msg, callback) {
			if ((typeof url === 'undefined')) {
				emptyOptions($(target), msg);
			} else {
				systemOptions($(target), url, msg, callback);
			}
		},
		chosenCommon : function(target, url, parameterType, callback) {
			if ((typeof url === 'undefined')) {
				emptyOptions($(target), msg);
			} else {
				chosenCommonOptions($(target), url, parameterType, callback);
			}
		},
		chosenSystem : function(target, url, callback) {
			if ((typeof url === 'undefined')) {
				emptyOptions($(target), msg);
			} else {
				chosenSystemOptions($(target), url, callback);
			}
		},
		chosenParameterCategory : function(target, url, callback) {
			if ((typeof url === 'undefined')) {
				emptyOptions($(target), msg);
			} else {
				parameterCategoryOptions($(target), url, callback);
			}
		},
		chosenParameter : function(target, url, callback) {
			if ((typeof url === 'undefined')) {
				emptyOptions($(target), msg);
			} else {
				parameterOptions($(target), url, callback);
			}
		},
		chosenMsgTempParameter : function(target, url, callback) {
			if ((typeof url === 'undefined')) {
				emptyOptions($(target), msg);
			} else {
				msgTempParameterOptions($(target), url, callback);
			}
		},
		chosenCommTempCode : function(target, url, callback) {
			if ((typeof url === 'undefined')) {
				emptyOptions($(target), msg);
			} else {
				chosenCommTempCodeOptions($(target), url, callback);
			}
		},
		chosenCommTempName : function(target, url, callback) {
			if ((typeof url === 'undefined')) {
				emptyOptions($(target), msg);
			} else {
				chosenCommTempNameOptions($(target), url, callback);
			}
		},
		chosenRole : function(target, url, callback) {
			if ((typeof url === 'undefined')) {
				emptyOptions($(target), msg);
			} else {
				chosenRoleOptions($(target), url, callback);
			}
		},
		dept : function(target, url, callback) {
			if ((typeof url === 'undefined')) {
				emptyOptions($(target), msg);
			} else {
				deptOptions($(target), url, callback);
			}
		},
		jobTitle : function(target, url, callback) {
			if ((typeof url === 'undefined')) {
				emptyOptions($(target), msg);
			} else {
				jobTitleOptions($(target), url, callback);
			}
		},
		role : function(target, url, callback) {
			if ((typeof url === 'undefined')) {
				emptyOptions($(target), msg);
			} else {
				roleOptions($(target), url, callback);
			}
		},
		relation: function(target, url, select) {
		    if ((typeof url === 'undefined')) {
                emptyOptions($(target), msg);
            } else {
                relationOption($(target), url, select);
            }
		}
	};
}();