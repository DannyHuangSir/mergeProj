/*<![CDATA[*/
function getNoticeBoardList() {
	var formData = {
		estatmentType: $('div.popover-content #notificationSearchType').val(),
		status: $('div.popover-content #notificationSearchStatus').val(),
		transMonth: $('div.popover-content #notificationSearchMonth').val()
	};
	$.ajax({
		url : '/eservice/getNoticeBoardList',
		contentType: 'application/json',
		type: "POST",
		data:  JSON.stringify(formData)
	}).done(function(response) {
		if (response.result == 'SUCCESS') {
			$('#notificationNumber').text($(response.resultData).size());
			
			// 顯示用的
			$('div.popover-content #notificationItems').html('');
			
			// template
			$('div[id="notification"][class="popover-template"] #notificationItems').html('');
			
			var notificationCnt = 0;
			$.each(response.resultData, function(i, row) {
				var rowHtml = '';
				rowHtml += '<a href="javascript:showEstatmentModal(\'' + row.id + '\')" class="item" style="cursor: pointer;">';
				rowHtml += '	<div class="header">';
				rowHtml += '		<em>' + row.estatmentTitle + '</em> ' + row.estatmentSubtitle;
				
				if (row.status == 0) {
					rowHtml += '	<span th:id="noticeNotRead_' + row.id + '" style="color: #007AB5">(未讀取)</span>';
					notificationCnt++;
				}
				
				rowHtml += '	</div>';
				rowHtml += '	<div class="content text-right">';
				
				if (row.createDate != null) {
					rowHtml += '		<small>' + $.datepicker.formatDate('yy年/mm月份', new Date(row.createDate)) + '</small>';
				}
				
				rowHtml += '	</div>';
				rowHtml += '</a>';
				
				$('div.popover-content #notificationItems').append(rowHtml);
				$('div[id="notification"][class="popover-template"] #notificationItems').append(rowHtml);
			});
			
			// 更新未讀取數量
			$('div.popover-content #notificationNotRead').html(notificationCnt);
			$('div[id="notification"][class="popover-template"] #notificationNotRead').html(notificationCnt);
			
		} else {
			alertMsg('系統發生錯誤');
		}
	}).fail(function (jqXHR, textStatus) {
		alertMsg('系統發生錯誤');
	});
}

function showEstatmentModal(id) {
	var formData = {estatmentId: id};
	$.ajax({
		url : '/eservice/getEstatmentAttrList',
		contentType: 'application/json',
		type: "POST",
		data:  JSON.stringify(formData)
	}).done(function(response) {
		if (response.result == 'SUCCESS') {
			var title = '';
			var value = '';
			$.each(response.resultData, function(i, row) {
				if (row.estatmentKey == 'TITLE') {
					title = row.estatmentValue;
				}
				if (row.estatmentKey == 'CONTENT') {
					value = row.estatmentValue;
				}
			});
			
			var html = '';
			html += '<div class="modal fade in" id="estatmentModal" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">';
			html += '<div class="modal-dialog">';
			html += '	<div class="modal-content lock-up">';
			html += '		<div class="modal-header">';
			html += '			<button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="$(\'#estatmentModal\').remove();"><span aria-hidden="true">×</span></button>';
			html += '		</div>';
			html += '		<div class="modal-body apply2 center1 grey2">';
			html += '			<h3 id="estatmentModalTitle">' + title + '</h3>';
			html += '			<p id="estatmentModalValue" class="item360 text-center" style="margin:auto">';
			html += '				' + value;
			html += '			</p>';
			html += '			<p class="marginTop20">';
			html += '			</p>';
			html += '		</div>';
			html += '	</div>';
			html += '</div>';
			html += '</div>';

			if ($('#estatmentModal').size() == 0) {
				$('body').append(html);
			}
			
			// 顯示通知內容
			$('#estatmentModal').show();
			
			// 移除該筆未讀取訊息文字
			if ($('div.popover-content #noticeNotRead_' + id).size() != 0) {
				$('div.popover-content #noticeNotRead_' + id).remove();
			}
			if ($('div[id="notification"][class="popover-template"] #noticeNotRead_' + id).size() != 0) {
				$('div[id="notification"][class="popover-template"] #noticeNotRead_' + id).remove();
			}
			
			// 更新未讀取數量
			$('div.popover-content #notificationNotRead').html($('div.popover-content span[id^="noticeNotRead_"]').size());
			$('div[id="notification"][class="popover-template"] #notificationNotRead').html($('div[id="notification"][class="popover-template"] span[id^="noticeNotRead_"]').size());
			
		} else {
			alertMsg('系統發生錯誤');
		}
	}).fail(function (jqXHR, textStatus) {
		alertMsg('系統發生錯誤');
	});
}
/*]]>*/