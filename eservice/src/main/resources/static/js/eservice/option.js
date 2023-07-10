function getParameterList(categoryCode, desabledCode) {
	$.ajax({
		type : 'POST',
		url: '/eservice/getParameterList?categoryCode=' + categoryCode
	}).done(function (data) {
		if (desabledCode == null) {
			desabledCode = '';
		}
		if (data.result == 'SUCCESS') {
			$('div.check-list div.mode2select').html('');
			
			var optionHtml = '';
			if (categoryCode == 'RENEW_REDUCE_TYPE') {
				optionHtml += '<label for="" class="col-sm-5 col-sm-offset-1 text-right">' + data.resultData[0].CATEGORY_NAME + '：</label>';
			} else {
				optionHtml += '<label for="" class="col-sm-4 col-sm-offset-1 text-right">' + data.resultData[0].CATEGORY_NAME + '：</label>';
			}
			
			optionHtml += '<label for="" class="col-sm-5 pll pmd">';
			$.each(data.resultData, function(i, row) {
				if(desabledCode.indexOf(row.PARAMETER_CODE) != -1 || desabledCode == row.PARAMETER_CODE){
					optionHtml += '<input type="radio" name="radio-btn" value="' + row.PARAMETER_CODE + '" id="radio1" disabled="disabled"/> '+ row.PARAMETER_NAME;
				}else{
					optionHtml += '<input type="radio" name="radio-btn" value="' + row.PARAMETER_CODE + '" id="radio1"/> '+ row.PARAMETER_NAME;
				}
				optionHtml += '<br/>';
			});
			optionHtml += '</label>';
			
			$('.check-list .mode2select').html(optionHtml);
		} else {
			alert(data.resultMsg);
		}
	}).fail(function (jqXHR, textStatus) {
		alert('系統發生錯誤!');
	});	
}

var eserviceOption = function() {
	function commonOptions($target, url, callback) {
		$target.html('');
		$.ajax({
			url : url,
			type : "POST"
		}).done(function(data) {
		    console.log("execute commonOptions ajax done : "  + $target.attr("id") + " url: " + url)
			if (data.result == 'SUCCESS') {
				var optHtml = '<option value="">請選擇</option>';
				$.each(data.resultData, function(i, obj) {
					//console.log(obj.VALUE );
					optHtml += ('<option value="' + obj.KEY + '">' + obj.VALUE + '</option>');
				});
				$target.html(optHtml);
				$("#bankName").trigger("chosen:updated");
			}
			if (callback) {
				callback();
			}
		});
	};
	function commonOptionsWithDefault(defaultStr, $target, url, callback) {
		$target.html('');
		$.ajax({
			url : url,
			type : "POST"
		}).done(function(data) {
			if (data.result == 'SUCCESS') {
				var optHtml = '<option value="">請選擇' + defaultStr + '</option>';
				$.each(data.resultData, function(i, obj) {
					//console.log(obj.VALUE );
					optHtml += ('<option value="' + obj.KEY + '">' + obj.VALUE + '</option>');
				});
				$target.html(optHtml);
				$("#bankName").trigger("chosen:updated");
			}
			if (callback) {
				callback();
			}
		});
	};
	function commonOptionschosen($target, url, callback) {
		$target.html('');
		$.ajax({
			url : url,
			type : "POST",
			async: false,
		}).done(function(data) {
			//console.log(data);
			if (data.result == 'SUCCESS') {
				var optHtml = '<option value=""></option>';
				$.each(data.resultData, function(i, obj) {
					optHtml += ('<option value="' + obj.KEY + '">' + obj.VALUE + '</option>');
				});
				$target.html(optHtml);
				$target.trigger("chosen:updated");
			}
			if (callback) {
				callback();
			}
		});
	};
	function commonOptionsPolicy($target, url, callback) {
		$target.html('');
		$.ajax({
			url : url,
			type : "POST"
		}).done(function(data) {
			if (data.result == 'SUCCESS') {
				var optHtml = '<option value="">請選擇</option>';
				$.each(data.resultData, function(i, obj) {
					optHtml += ('<option value="' + obj.POLICY_NO + '">' + obj.PRODUCT_NAME + '</option>');
				});
				$target.html(optHtml);
			}
			if (callback) {
				callback();
			}
		});
	};
	function cityRegionOptions($cityTarget, cityUrl, $regionTarget, regionUrl) {
		emptyOptions($regionTarget, '請選擇');
		commonOptions($cityTarget, cityUrl, function() {
			$cityTarget.change(function() {
				commonOptions($regionTarget, (regionUrl + '?cityId=' + $(this).val()));
			});
		});
	};
	function cityRegionSelectedOptions($cityTarget, cityUrl, selectedCity, $regionTarget, regionUrl, selectedRegion) {
		emptyOptions($regionTarget, '請選擇');
		commonOptions($cityTarget, cityUrl, function() {
			$cityTarget.change(function() {
				commonOptions($regionTarget, (regionUrl + '?cityId=' + $cityTarget.val()));
			});
			
			if (selectedCity) {
				$cityTarget.val(selectedCity);
				commonOptions($regionTarget, (regionUrl + '?cityId=' + selectedCity), function() {
					$regionTarget.val(selectedRegion);
				});
			}
		});
	};
	function bankBranchesOptions($bank, bankUrl, $branches, branchesUrl) {
		emptyOptions($branches, '請選擇');
		commonOptionschosen($bank, bankUrl, function() {
			$bank.change(function() {
				commonOptionschosen($branches, (branchesUrl + '?bankId=' + $(this).val()));
			});
		});
	};
	function chosenPartnerOptions($target, url, callback) {
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
	function emptyOptions($target, defaultValue) {
		$target.html('');
		$target.html('<option value="">' + defaultValue + '</option>');
	};
	function areaCountryOptions($areaTarget, areaUrl, $countryTarget, countryUrl, defaultOption) {
		emptyOptions($countryTarget, '請選擇' + defaultOption);
		commonOptionsWithDefault('區域', $areaTarget, areaUrl, function() {
			$areaTarget.change(function() {
				commonOptionsWithDefault('國家', $countryTarget, (countryUrl + '?areaId=' + $(this).val()));
			});
		});
	};
	function jobOptions($jobBTarget, jobBUrl, $jobMTarget, jobMUrl) {
		emptyOptions($jobMTarget, '請選擇中分類');
		commonOptionsWithDefault('大分類', $jobBTarget, jobBUrl, function() {
			$jobBTarget.change(function() {
				commonOptionsWithDefault('中分類', $jobMTarget, (jobMUrl + '?jobB=' + $(this).val()));
			});
		});
	};
	function jobSelectedOptions($jobBTarget, jobBUrl, selectedCity, $regionTarget, JobMUrl, selectedJobM) {
		emptyOptions($jobMTarget, '請選擇小分類');
		commonOptions($jobBTarget, jobBUrl, function() {
			$jobBTarget.change(function() {
				commonOptions($jobMTarget, (JobMUrl + '?jobM=' + $jobBTarget.val()));
			});
			
			if (selectedJobM) {
				$jobBTarget.val(selectedJobM);
				commonOptions($jobMTarget, (JobMUrl + '?jobM=' + selectedJobM), function() {
					$jobMTarget.val(selectedJobM);
				});
			}
		});
	};
	return {
		common : function(target, url, callback) {
			if ((typeof url === 'undefined')) {
				emptyOptions($(target), '請選擇');
			} else {
				commonOptions($(target), url, callback);
			}
		},
		commonPolicy : function(target, url, callback) {
			if ((typeof url === 'undefined')) {
				emptyOptions($(target), '請選擇');
			} else {
				commonOptionsPolicy($(target), url, callback);
			}
		},
		empty : function(target, defaultValue) {
			emptyOptions($(target), defaultValue);
		},
		cityRegion : function(cityTargetId, cityUrl, regionTargetId, regionUrl) {
			cityRegionOptions($(cityTargetId), cityUrl, $(regionTargetId), regionUrl);
		},
		cityRegionSelected : function(cityTargetId, cityUrl, selectedCity, regionTargetId, regionUrl, selectedRegion) {
			cityRegionSelectedOptions($(cityTargetId), cityUrl, selectedCity, $(regionTargetId), regionUrl, selectedRegion);
		},
		bankBranches : function(bankId, bankUrl, branchesId, branchesUrl) {
			bankBranchesOptions($(bankId), bankUrl, $(branchesId), branchesUrl);
		},
		partner : function(target, url, callback) {
			if ((typeof url === 'undefined')) {
				emptyOptions($(target), msg);
			} else {
				chosenPartnerOptions($(target), url, callback);
			}
		},
		areaCountry : function(areaTargetId, areaUrl, countryTargetId, countryUrl, defaultOption) {
			areaCountryOptions($(areaTargetId), areaUrl, $(countryTargetId), countryUrl, defaultOption);
		},
		jobPicker : function(jobBTargetId, jobBUrl, jobMTargetId, jobMUrl) {
			jobOptions($(jobBTargetId), jobBUrl, $(jobMTargetId), jobMUrl);
		},
		jobSelected : function(cityTargetId, cityUrl, selectedCity, regionTargetId, regionUrl, selectedRegion) {
			jobSelectedOptions($(cityTargetId), cityUrl, selectedCity, $(regionTargetId), regionUrl, selectedRegion);
		},
		updateBranch : function(branches, branchesUrl, bankVal) {
            emptyOptions($(branches), '請選擇');
            commonOptionschosen($(branches), (branchesUrl + '?bankId=' + bankVal));
        }
	};
}();
