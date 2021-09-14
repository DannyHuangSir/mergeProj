//listing panel expand 
$(function() {
	$(".cancalApplication").click(function(event) {
		event.stopPropagation();
		openAlert();
	});

	$("button[id^=cancalApplication_]").click(function(event) {
		console.log($(this).attr("id"));
		event.stopPropagation();
		openAlert();
	});
	
	$("button[id^=upload_]").click(function(event) {
		console.log($(this).attr("id"));
		event.stopPropagation();
	});
	
	$("#deny, .close").click(function(event) {
		hideAlert();
	});

	function openAlert() {
		$('#modal-container-deleteConfirm').show();
		$('#fadeInBg').show();
	}

	function hideAlert() {
		$('#modal-container-deleteConfirm').hide();
		$('#fadeInBg').hide();
	}

});

function openAlert(value, url) {
	var html = "";
	html = '<div class="modal fade in" id="modal-container-showErrorMessage" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">';
	html = html + '<div class="modal-dialog"><div class="modal-content lock-up">';
	console.log(url);
	html = html + '<div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="hideAlert(\''+url+'\')">';
	html = html + '<span aria-hidden="true">×</span></button></div>';
	html = html + '<div class="modal-body apply2 center1 grey2">';
	html = html + '<p class="item360 text-center" style="margin:auto">'+ value;
	html = html + '</p><p class="marginTop20"><a href="#">';
	html = html + '<button class="btn-login lock2" onclick="hideAlert(\''+url+'\')">確定</button></a></p>';
	html = html + '</div></div></div></div>';		

	if($("#modal-container-showErrorMessage").length == 0){
		$("body").append(html);
	}else{
		$("#modal-container-showErrorMessage").remove();
		$("body").append(html);
	}
	
	$('#modal-container-showErrorMessage').show();
	$('#fadeInBg').show();
}

function showMsg(obt){
	var policyNo = $(obt).attr("id");
	var msg = $("#msg_"+policyNo).val();
	var str = "";
	var strList = [];
	if(msg == null || msg == ""){
		str = "此張保單已過投保終期，故無法使用。";
	}else{
		strList = msg.split('|');
		strList = $.unique(strList);
		if (strList.length <= 1){
		str = msg;
	}
	}
	
	var html = "";
	html = '<div class="modal fade in" id="modal-container-locked" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">';
	html = html + '<div class="modal-dialog"><div class="modal-content lock-up">';
	html = html + '<div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-label="Close">';
	html = html + '<span aria-hidden="true">×</span></button></div>';
	html = html + '<div class="modal-body apply2 center1 grey2" style = "padding-bottom: 30px;">';
	html = html + '<h3><i class="foo-unlock foo-4x orange"></i></h3> 您目前無法選取該保單';
	if (strList.length <= 1){
		html = html + '<p>'+str+'</p>';
	}else{
		strList.forEach(function(str){
			html = html + '<p>'+str+'</p>';
		});
		
	}
	html = html + '</div></div></div></div>';		

	if($("#modal-container-locked").length == 0){
		$("body").append(html);
	}else{
		$("#modal-container-locked").remove();
		$("body").append(html);
	}
	
	$('#modal-container-locked').show();
}

function confirmAlert(value, title, fnOk, fnCancel, custOkBtnName, custDenyBtnName) {
	var denyBtnName = '否';
	var okBtnName = '是';
	
	if (custOkBtnName) {
		okBtnName = custOkBtnName;
	}
	
	if (custDenyBtnName) {
		denyBtnName = custDenyBtnName;
	}
	
	var html = '';
	html += '<div class="modal fade in" data-backdrop="static" id="confirmAlertModal" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">';
	html += '<div class="modal-dialog">';
	html += '	<div class="modal-content lock-up">';
	html += '		<div class="modal-header">';
	html += '			<button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="$(\'#confirmAlertModal\').hide();"><span aria-hidden="true">×</span></button>';
	html += '		</div>';
	html += '		<div class="modal-body apply2 center1 grey2">';
	html += '			<h3 id="confirmAlertModalTitle">' + title + '</h3>';
	html += '			<p id="confirmAlertModalValue" class="item360 text-center" style="margin:auto">';
	html += '				' + value;
	html += '			</p>';
	html += '			<p class="marginTop20">';
	html += '				<a href="#">';
	html += '					<button class="btn-login lock2" id="deny">' + denyBtnName + '</button>';
	html += '				</a>';
	html += '				<a href="#">';
	html += '					<button class="btn-login lock2" id="okBtn">' + okBtnName + '</button>';
	html += '				</a>';
	html += '			</p>';
	html += '		</div>';
	html += '	</div>';
	html += '</div>';
	html += '</div>';

	if ($('#confirmAlertModal').size() == 0) {
		$('body').append(html);
	}
	
	$('#confirmAlertModal #okBtn').click(function() {
		if (fnOk) {
			fnOk();
		}
		$('#confirmAlertModal').remove();
	});
	
	$('#confirmAlertModal #deny').click(function() {
		if (fnCancel) {
			fnCancel();
		}
		$('#confirmAlertModal').remove();
	});
	
	$('#confirmAlertModal').show();
}

function hideAlert(url) {
	$('#modal-container-showErrorMessage').hide();
	$('#fadeInBg').hide();
	if(url != "undefined" && url != null){
		window.location.href = url;
	}
}
