
function initDivAuth(funcUrl) {
	var divAuthMap = ${sessionScope['DIV_AUTH_MAP']};
	var divAuth = divAuthMap[funcUrl];
	console.log("divAuth of "+funcUrl+":", divAuth);
	if(divAuth) {
		for(var i=0; i<divAuth.length;i++) {
			console.log(divAuth[i].divId +":"+divAuth[i].divName);
			$("#"+divAuth[i].divName).hide();
		}
	}
	return divAuth;
}
