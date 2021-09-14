/*<![CDATA[*/
/**FB登入功能**/
window.fbAsyncInit = function() {
	FB.init({
		appId :   '1068717626639004',//臺銀正式
		cookie : true,
		xfbml : true,
		version : 'v3.2'
	});

	FB.AppEvents.logPageView();

};

(function(d, s, id) {
	var js, fjs = d.getElementsByTagName(s)[0];
	if (d.getElementById(id)) {
		return;
	}
	js = d.createElement(s);
	js.id = id;
	js.src = "https://connect.facebook.net/en_US/sdk.js";
	fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

function checkGDPR(){
	var rtn = false;
	var euNationality = $('input[name="euNationality"]:checked').val();
	if (euNationality == '' || euNationality == null) {
		//openAlert('請點選本人是否具有歐盟國會員國之國籍或是否居住於歐盟地區');
		openAlert($('#E0134').val());
		return false;
	}else{
		$('#loginForm #euNationality').val(euNationality);
		rtn =  true;
	}
	return rtn;
}

function checkLoginState() {
	//非使用帳號密碼登入，清空用戶名稱及密碼欄
	$('#userId').val("");
	$('#password').val("");
	
	if(!checkGDPR()){
		return;
	}
	
	if (location.protocol != 'https:') {
		//openAlert("非安全性網站無法使用facebook登入功能!");
		openAlert($('#E0004').val());
		return;
	}
	
	FB.getLoginStatus(function(response) {
		popupLoading();
		console.log("getLoginStatus response", response);
		statusChangeCallback(response);
	});
}

function statusChangeCallback(response) {
	if(console)
		console.log('statusChangeCallback');
	// The response object is returned with a status field that lets the
	// app know the current login status of the person.
	// Full docs on the response object can be found in the documentation
	// for FB.getLoginStatus().
	if (response.status === 'connected') {
		// Logged into your app and Facebook.
		if(console)
			console.log('Welcome!  Fetching your information.... ', response);
		FB.api('/me', {
			fields : 'id, name, email'
		}, function(response) {
			//console.log('Successful login for: ' + response.name, response);
			console.log('id=' + response.id + ",name=" + response.name	+ ",email=" + response.email);
			// 取得id, email
			$('#login-status').html('正透過Facebook登入，請稍候...');
			$("#fbId").val(response.id);
			$("#email").val(response.email);
			var auth = FB.getAuthResponse();
			$('#fbAccessToken').val(auth.accessToken);
			console.log("fb id="+response.id +'fbAccessToken=' + auth.accessToken);
			//alert("fb id="+response.id + ", fbAccessToken=" + auth.accessToken);
			//removeLoading();
			$('#loginForm').submit();
		});
	} else {
		// The person is not logged into your app or we are unable to tell.
		// document.getElementById('status').innerHTML = 'Please log into this app.';
		//console.log('User not logged in.');
		removeLoading();
		alert("Facebook未成功登入，請重新登入!");
	}
}

// custom fb login button
function fb_login() {
	//非使用帳號密碼登入，清空用戶名稱及密碼欄
	$('#userID').val("");
	$('#password').val("");
	
	if(!checkGDPR()){
		return;
	}
	
	// FB 第三方登入，要求公開資料與email
	FB.login(function(response) {
		// statusChangeCallback(response);
		//console.log(response);
		removeLoading();
	}, {
		scope : 'public_profile,email'
	});
}

function fb_logout() {
	FB.logout(function(response) {
		// Person is now logged out

	});
}


/**憑證登入功能**/
var postTarget;
var timeoutId;
var action;
var rtndata;

function postData(target, data) {
	http.url = target;
	http.actionMethod = "POST";
	var code = http.sendRequest(data);
	if (code != 0)
		return null;
	
	return http.responseText;
}

function getData(target, data) {
	http.url = target;
	http.actionMethod = "GET";
	var code = http.send();
	
	if(xmlreq.readyState == 4 && xmlreq.status == 200) {
		return http.responseText;
	}else{
		return null;
	}
}

function checkFinish() {
	if (postTarget) {
		postTarget.close();
		removeLoading();
		//alert("尚未安裝元件");
		// 提示請安裝HiCOS Client卡片管理工具
		$("#icCardNoticeModal").modal("show");
	}
}

function makeSignature()
{
    var ua = window.navigator.userAgent;
	if(ua.indexOf("MSIE")!=-1 || ua.indexOf("Trident")!=-1) //is IE, use ActiveX
	{
		// is IE, use ActiveX
		console.log("#is ie #");
		var tbsPackage=getTbsPackage();
		document.getElementById("httpObject").innerHTML='<OBJECT id="http" width=1 height=1 style="LEFT: 1px; TOP: 1px" type="application/x-httpcomponent" VIEWASTEXT></OBJECT>';

		rtndata=postData("http://localhost:61161/sign",tbsPackage);
		if(!rtndata) {
			removeLoading();
			//alert("尚未安裝元件");
			$("#icCardNoticeModal").modal("show");
		} else {
			setSignature(rtndata);
		}

	}
	else{
		postTarget=window.open("http://localhost:61161/popupForm", "Signing","height=200, width=200, left=100, top=20");
		timeoutId=setTimeout(checkFinish,3500);
	}
}

//function makeSignature() {
//	var ua = window.navigator.userAgent;
//	console.log("userAgent=" + ua);
//	if (ua.indexOf("MSIE") != -1 || ua.indexOf("Trident") != -1) {
//		// is IE, use ActiveX
//		console.log("#is ie #");
//		var tbsPackage = getTbsPackage();
//		var data = postData("http://localhost:61161/sign", "tbsPackage="
//				+ tbsPackage);
//		if (!data) {
//			removeLoading();
//			//alert("尚未安裝元件");
//			$("#icCardNoticeModal").modal("show");
//		} else {
//			setSignature(data);
//		}
//	} else {
//		console.log("#not ie #");
//		postTarget = window.open("http://localhost:61161/popupForm", "Signing",
//				"height=200, width=200, left=100, top=20");
//		timeoutId = setTimeout(checkFinish, 3500);
//	}
//}


function getTbsPackage() {
	var tbsData = {};
	var nonce = "C37D8787474AE135B5549141FFEB38F6";
	tbsData["tbs"] = "<TBS><AAData></AAData></TBS>";
	tbsData["hashAlgorithm"] = "SHA1";
	tbsData["withCardSN"] = "true";
	tbsData["pin"] = $("#icpwd").val();
	tbsData["nonce"] = nonce;
	tbsData["func"] = "MakeSignature";
	tbsData["signatureType"] = "PKCS7";	
	var json = JSON.stringify(tbsData);

	return json;
}

function setSignature(signature) {
	var ret = JSON.parse(signature);
	//alert("ret="+JSON.stringify(ret));
	
	if (console)
		//console.log("ret=", ret);
	// $("#base64Data").val(ret.signature);
	// document.getElementById("base64Cred").value = ret.signature;
	// document.getElementById("base64Data").value = ret.signature;
	if (ret.ret_code != 0) {
		// 驗證有誤
		removeLoading();
		alert(MajorErrorReason(ret.ret_code));
	} else {
		$('#login-status').html('登入中，請稍候...');
		// 驗證成功
		$('#icCardModal').modal('hide');
		
		//getCertInfo();
		
		 $.ajax({
			type : 'POST',
			contentType : 'application/json',
			url : '/eservice/setSessioinCardSN',
			data : ret.cardSN,
			async : false,
			success : function(response) {
				console.log(response);
				console.log("success");
				//alert("success.");
				//if(response.resultMsg != ""){
				//	openAlert(response.resultMsg);
					return;
				//} else {
				//	openAlert("response.resultMsg is empty.");
				//}
			}
		});
		
		
		$("#cardSN").val(ret.cardSN);
		$("#certb64").val(ret.certb64);
		
		//removeLoading();

		//startTest();
		
		//alert("fake submit");
		//prevention F12 change from data
		if(checkMoicaForm(signature)){
			$('#loginForm').submit();
		}else{
            openAlert($('#E0018').val());//系統發生錯誤!
		}
		
	}
}


function encryptCardSN(cardSN){
	var rtnCardSN = "";
	var sJson = JSON.stringify
	({ 
		sCardSN: cardSN
	});
	$.ajax({
		type     : 'POST',
		contentType : 'application/json; charset=UTF-8',
		url      : '/eservice/encryptCardSN',
		async    : false,
		dataType : "json",
		data     : sJson,
		
		success : function(response) {
			//console.log(response);
			if(response.resultMsg != ""){
				openAlert(response.resultMsg);
				return;
			} else {
				var rtnCardSn = response.resultData[0].cardSN
				//alert("rtnCardSn="+rtnCardSn);
			}
		},
		error : function() {
			console.log("error");
			openAlert("error");
			/* openAlert('系統發生錯誤!'); */
            openAlert($('#E0018').val());
		},
        complete: function(data) {
        	console.log("complete");
        	openAlert("complete");
            removeLoading();
        }
	});
	
	return rtnCardSN;
}

function checkLoginForm(){
	var rtn = false;
	if( !isEmpty($('#loginForm #cardSN').val())){//check moica data
		if (typeof rtndata == "null" || rtndata == null) {
			return false;
		}else{
			var ret = JSON.parse(rtndata);
			if(ret.cardSN == $('#loginForm #cardSN').val() && ret.certb64 == $('#loginForm #certb64').val()){
				rtn = true;
			}
		}
	}else{
		rtn = true;
	}

	return rtn;
}

function isEmpty(value) {
	return typeof value == 'string' && !value.trim() || typeof value == 'undefined' || value === null;
}

function checkMoicaForm(signature){
	var rtn = false;
	var ret = JSON.parse(signature);
	//check #loginForm
	if (typeof ret == "null" || ret == null) {
		alert('MICA check error.');
	}else{
		//非使用帳號密碼登入，清空用戶名稱及密碼欄
		$('#loginForm #userId').val();
		$('#loginForm #password').val();
		
		if(ret.cardSN == $("#cardSN").val() && ret.certb64 == $("#certb64").val()){
			rtn = true;
		}else{
			alert('MICA check error.');
		}
		
	}
	
	if(!rtn){
		window.location.href = "/eservice/login";
	}
	return rtn;
}

function startTest(){
	//alert("startTest");
	var xmlreq = new XMLHttpRequest();
	var tbsData = {};
	tbsData["tbs"]="TBS";
	tbsData["hashAlgorithm"]="SHA256";
	tbsData["pin"] = encodeURIComponent($("#icpwd").val());
	if(tbsData["pin"].length<6){
		alert("PIN碼至少需6碼");
		return;
	}
	tbsData["func"]="MakeSignature";
	tbsData["signatureType"]="PKCS7";
	tbsData["slotDescription"] = 0;
	var json = JSON.stringify(tbsData );

	xmlreq.open("POST", "/sign", false);
	xmlreq.send("tbsPackage="+json);
	if(xmlreq.readyState == 4 && xmlreq.status == 200) {
		var ret=JSON.parse(xmlreq.responseText);
		if(ret.ret_code!=0){
			alert("sign error.");
			return;
		}

	}else{
		//alert("xmlreq.state="+xmlreq.readyState);
		return;
	}
	getCertInfo();
}

function getCertInfo(){
	
	//var rtndata = getData("http://localhost:61161/pkcs11info?withcert=true",null);
	
	var xmlreq = new XMLHttpRequest();
	xmlreq.onreadystatechange = function() 
	{
		if(xmlreq.readyState == 4 && xmlreq.status == 200) {
			var ret = JSON.parse(xmlreq.responseText);
			var slots = ret.slots;
			//var selectSlot = document.getElementById('slotDescription');
			for(var index in slots){ 
				//if(slots[index].slotDescription!=selectSlot.value) continue;
				var certs = slots[index].token.certs;
				for(var indexCert in certs){
					var notAfter=new Date(certs[indexCert].notAfterT*1000);
					var notBefore=new Date(certs[indexCert].notBeforeT*1000);
					var validity="自"+notBefore.getFullYear()+"年"+(notBefore.getMonth()+1)+"月"+notBefore.getDate()+"日 至 "+notAfter.getFullYear()+"年"+(notAfter.getMonth()+1)+"月"+notAfter.getDate()+"日";
					var certInfo="憑證主體:"+certs[indexCert].subjectDN+"<br/>憑證序號:"+certs[indexCert].sn+"<br/>憑證效期:"+validity+"<br/>金鑰用途:"+certs[indexCert].usage;
					
					//alert("validity="+validity);
					//alert("certInfo="+certInfo);
					
					if(certs[indexCert].label==="cert1"){
						//document.getElementById("signCertPass").innerHTML=passString;
						//document.getElementById("signCert").innerHTML=certInfo;
					}else if(certs[indexCert].label==="cert2"){
						//document.getElementById("encryptCertPass").innerHTML=passString;
						//document.getElementById("encryptCert").innerHTML=certInfo;
					}
				}
			}
		}else{
		}
	};
	
	xmlreq.open("GET", "http://localhost:61161/pkcs11info?withcert=true", true);
	xmlreq.setRequestHeader("Access-Control-Allow-Origin", "http://localhost:61161");
	xmlreq.setRequestHeader("Access-Control-Allow-Headers", "X-Requested-With");
	xmlreq.setRequestHeader("Access-Control-Allow-Methods", "GET");
	xmlreq.setRequestHeader("Access-Control-Allow-Credentials", true);
	
	xmlreq.send();
}

function receiveMessage(event) {
	if (console)
		console.debug(event);

	if (event.origin != "http://localhost:61161")
		return;
	try {
		var ret = JSON.parse(event.data);
		if (ret.func) {
			if (ret.func == "getTbs") {
				clearTimeout(timeoutId);
				var json = getTbsPackage();
				//alert("json getTbsPackage="+json);
				postTarget.postMessage(json, "*");
			} else if (ret.func == "sign") {
				rtndata = event.data;
				setSignature(event.data);
			}
		} else {
			if (console)
				console.error("no func");
			removeLoading();
		}
	} catch (e) {
		removeLoading();
		// errorhandle
		if (console)
			console.error(e);
	}
}
if (window.addEventListener) {
	window.addEventListener("message", receiveMessage, false);
} else {
	// for IE8
	window.attachEvent("onmessage", receiveMessage);
}
// for IE8
var console = console || {
	"log" : function() {
	},
	"debug" : function() {
	},
	"error" : function() {
	}
};

var check = false;

function onInited(code) {
	if (code != 0) {
		alert(getErrorMessage(code));
		return;
	}
}

function submitCert() {
	popupLoading();
	makeSignature();
}

function openIcModal() {
	return false;//disable MOICA login
	//非使用帳號密碼登入，清空用戶名稱及密碼欄
	$('#userId').val("");
	$('#password').val("");
	
	if ($('#validateCode').val() == '') {
		//openAlert('請輸入圖形驗證碼');
		openAlert($('#E0008').val());
		return false;
	}
	
	if(!checkGDPR()){
		return;
	}
	
	$.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : '/eservice/preCheckkValidateCode',
		data : $('#validateCode').val(),
		success : function(response) {
			//console.log(response);
			if(response.resultMsg != ""){
				openAlert(response.resultMsg);
				$('#validateCode').val("");
				return;
			} else {
				$('#icCardModal').modal('show');
				setTimeout(function(){
				    $('input[name=icpwd]').focus();
				}, 0);
			}
		},
		error : function() {
			/* openAlert('系統發生錯誤!'); */
            openAlert($('#E0018').val());
		},
        complete: function(data) {
            removeLoading();
        }
	});
}
/* ]]> */