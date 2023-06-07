function showSystemMessage(code, array) {
	var msg = '';
	$.ajax({
		contentType : 'application/json; charset=iso-8859-1',
		type : 'POST',
		url : requestContextPath + '/unsecure/getSystemMessage.do',
		data : "{ 'code': '" + code + "'}",
		success : function(dataMap, textStatus, jqXHR) {
			if (dataMap != null) {
				if (dataMap.result == 'success') {
					msg = dataMap.resultMsg;
					if (array != null && array.length > 0) {
						for (var i=0; i<array.length; i++) {
							msg = msg.replace('{' + (i + 1) + '}', array[i]);
						}
					}
					showMessageDialog(msg);
				}
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			//alert('系統發生錯誤，請稍候再試！');
			return false;
		}
	});
	return false;
}
function showMessageInTag(code, array, element) {
	var msg = '';
	$.ajax({
		contentType : 'application/json; charset=iso-8859-1',
		type : 'POST',
		url : requestContextPath + '/unsecure/getSystemMessage.do',
		data : "{ 'code': '" + code + "'}",
		success : function(dataMap, textStatus, jqXHR) {
			if (dataMap != null) {
				if (dataMap.result == 'success') {
					msg = dataMap.resultMsg;
					if (array != null && array.length > 0) {
						for (var i=0; i<array.length; i++) {
							msg = msg.replace('{' + (i + 1) + '}', array[i]);
						}
					}
				}
				element.html(msg);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			//alert('系統發生錯誤，請稍候再試！');
			return false;
		}
	});
	return false;
}


//alert(navigator.userAgent);
//檢測平台 
var p = navigator.platform;  
var win = p.indexOf("Win") == 0;  
var mac = p.indexOf("Mac") == 0;  
var x11 = (p == "X11") || (p.indexOf("Linux") == 0);  

function clickPop(tagId) {
	$(tagId).prop("disabled", false);
	$(tagId).click();
	$(tagId).prop("disabled", true);
}


function showMessageDialog(msg) {
	$("#messagePopContent").html(msg);
	$("#dialog-message").modal({
		show: true,
		backdrop: "static",
		keyboard: false
	});
	/*$("#button-message").prop("disabled", false);
	$("#button-message").click();
	$("#button-message").prop("disabled", true);*/
	/*if(win || mac || x11){ //PC端
		$("#dialog-message-content").html(msg);
		$("#dialog-message").dialog({
			resizable : false,
			height : 200,
			width : 600,
			modal : true,
			buttons : {
				"確定" : function() {
					$(this).dialog("close");
				}
			}
		});
	}else{ //移動端
		alert(msg);
	} 	*/
}
function showConfirmDialog(msg, func) {
	$("#confirmPopContent").html(msg);
	$("#dialog-confirm").find("button[id='cancelThisApply']").attr("onclick", func);
	$("#dialog-confirm").find("button[id='cancelThisApply']").attr("data-dismiss", "modal");
	$("#dialog-confirm").modal({
		show: true,
		backdrop: "static",
		keyboard: false
	});
	/*$("#button-confirm").prop("disabled", false);
	$("#button-confirm").click();
	$("#button-confirm").prop("disabled", true);*/
	/*if(win || mac || x11){ //PC端
		$("#dialog-confirm-content").html(msg);
	    $("#dialog-confirm").dialog({
			resizable : false,
			height : 200,
			width : 600,
			modal : true,
			buttons : {
				"確定" : function() {
					setTimeout(func, 5);
					$(this).dialog("close");
				},
				"取消" : function() {
					$(this).dialog("close");
				}
			}
		});
	}else{ //移動端
		if(confirm(msg)) {
			setTimeout(func, 5);
		}		
	} 	*/
		
}
function showConfirmDialog60(msg, func) {
	$("#confirmPopContent60").html(msg);
	$("#dialog-confirm60").find("button[id='cancelThisApply60']").attr("onclick", func);
	$("#dialog-confirm60").find("button[id='cancelThisApply60']").attr("data-dismiss", "modal");
	$("#dialog-confirm60").modal({
		show: true,
		backdrop: "static",
		keyboard: false
	});
	/*$("#button-confirm60").prop("disabled", false);
	$("#button-confirm60").click();
	$("#button-confirm60").prop("disabled", true);*/
}
function showSurveyDialog() {
	$("#dialog-survey").modal({
		show: true,
		backdrop: "static",
		keyboard: false
	});
	/*$("#button-survey").prop("disabled", false);
	$("#button-survey").click();
	$("#button-survey").prop("disabled", true);*/		
}

function showLoadingModal() {
	$('#loadingModal').modal('show')
}

function hideLoadingModal() {
	$('#loadingModal').modal('hide')
}

function getPolicyInfo(actionUrl, policyNo, policyListType) {
	showLoadingModal()
	postPolicyListType(actionUrl,policyNo,policyListType)
}
