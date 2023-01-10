//var sheet = window.document.styleSheets[0];
//sheet.insertRule(".loading-modal {display: none;position: fixed;z-index: 1000;top: 0;left: 0;height: 100%;width: 100%;background: rgba(255, 255, 255, .3) url('../img/ajax-loader.gif') 50% 50% no-repeat;}", sheet.cssRules.length);
//sheet.insertRule("body.loading .loading-modal {	overflow: hidden;}", sheet.cssRules.length);
//sheet.insertRule("body.loading .loading-modal {	display: block;}", sheet.cssRules.length);

function popupLoading() {
	$("body").addClass("loading");
}

function removeLoading() {
	$("body").removeClass("loading");
}