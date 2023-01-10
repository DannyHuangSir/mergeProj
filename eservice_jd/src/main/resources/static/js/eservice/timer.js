var $logoutDiv = $('#logoutDiv');
var logutRemainTime = $logoutDiv.data('timeout_seconds');
$(function() {
	if ($logoutDiv.size() > 0) {
		$('#logoutDiv div.rp1').html('<i class="foo-cw"></i>' + logutRemainTime + '秒');
		logoutTimer.show(1); // 1秒
		$('#logoutResetBtn').click(function() {
			logoutTimer.reset();
		});
	}
});

var logoutTimer = function() {
	var timer;
	function showLogutRemainTime(second) {
		timer = setInterval(function() {
			logutRemainTime = logutRemainTime - 1
			
			if (logutRemainTime <= 0) {
				clearInterval(timer);
				$('#logoutDiv div.rp1').html('<i class="foo-cw"></i>0秒');
				$('#logoutForm')[0].submit();
			} else if (logutRemainTime <= 60) {
				$('#logoutDiv div.rp1').html('<i class="foo-cw"></i>' + logutRemainTime  + '秒');
				
				var title = (logutRemainTime + '秒後將自動登出，是否繼續使用？');
				var value = ('為了您的帳戶安全，' + logutRemainTime + '秒後將自動登出!!');
				if ($('#confirmAlertModal').size() == 0) {
					confirmAlert(value,title, function() {
						logoutTimer.reset();
					}, function() {
						clearInterval(timer);
						$('#logoutForm')[0].submit();
					}, '繼續使用', '馬上離開');
				} else {
					$('#confirmAlertModalTitle').html(title);
					$('#confirmAlertModalValue').html(value);
				}
			} else {
				$('#logoutDiv div.rp1').html('<i class="foo-cw"></i>' + logutRemainTime  + '秒');
			}
		} , second * 1000);	
	};
	function reset() {
		clearInterval(timer);
		logutRemainTime = $logoutDiv.data('timeout_seconds');
		showLogutRemainTime(1);
	};
	return {
		show : function(second) {
			showLogutRemainTime(second);
		},
		reset : function() {
			reset();
		}
	};
}();