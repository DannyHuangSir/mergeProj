function alertMsg(msg) {
	var modalHtml = '';
	modalHtml += '<div id="alertModal" class="bootbox modal fade bootbox-confirm in" tabindex="-1" role="dialog">';
	modalHtml += '	<div class="modal-dialog" style="z-index: 1100;">';
	modalHtml += '		<div class="modal-content">';
	modalHtml += '			<div class="modal-body">';
	modalHtml += '				<button type="button" class="bootbox-close-button close"';
	modalHtml += '					data-dismiss="modal" aria-hidden="true" style="margin-top: -10px;">×</button>';
	modalHtml += '				<div class="bootbox-body">' + msg + '</div>';
	modalHtml += '			</div>';
	modalHtml += '			<div class="modal-footer">';
	modalHtml += '				<button data-dismiss="modal" type="button"';
	modalHtml += '					class="btn btn-primary">確認</button>';
	modalHtml += '			</div>';
	modalHtml += '		</div>';
	modalHtml += '	</div>';
	modalHtml += '</div>';
	
	$('body').append(modalHtml);
	if ($('.ui-dialog').size() >= 1) {
		$.each($('.ui-dialog'), function(i, obj) {
			if ($(obj).css('display') == 'block') {
				$("#alertModal").appendTo($(obj));
				return;
			}
		});
	}
	
	$("#alertModal").modal();
	$('#alertModal').on('hidden.bs.modal', function(e) {
		$(this).remove();
	});
}

function confirmMsg(msg, callback) {
	var modalHtml = '';
	modalHtml += '<div id="confirmModal" class="bootbox modal fade bootbox-confirm in" tabindex="-1" role="dialog">';
	modalHtml += '	<div class="modal-dialog" style="z-index: 1100;">';
	modalHtml += '		<div class="modal-content">';
	modalHtml += '			<div class="modal-body">';
	modalHtml += '				<button type="button" class="bootbox-close-button close"';
	modalHtml += '					data-dismiss="modal" aria-hidden="true" style="margin-top: -10px;">×</button>';
	modalHtml += '				<div class="bootbox-body">' + msg + '</div>';
	modalHtml += '			</div>';
	modalHtml += '			<div class="modal-footer">';
	modalHtml += '				<button data-bb-handler="cancel" type="button"';
	modalHtml += '					class="btn btn-default" id="confirmFalse">取消</button>';
	modalHtml += '				<button data-bb-handler="confirm" type="button"';
	modalHtml += '					class="btn btn-primary" id="confirmTrue">確認</button>';
	modalHtml += '			</div>';
	modalHtml += '		</div>';
	modalHtml += '	</div>';
	modalHtml += '</div>';
	
	$('body').append(modalHtml);
	if ($('.ui-dialog').size() >= 1) {
		$.each($('.ui-dialog'), function(i, obj) {
			if ($(obj).css('display') == 'block') {
				$("#confirmModal").appendTo($(obj));
				return;
			}
		});
	}
	
	$("#confirmModal").modal();
	$('#confirmModal').on('hidden.bs.modal', function(e) {
		$(this).remove();
	});

	$('#confirmFalse').click(function(){
		$('#confirmModal').modal('hide');
		if (callback) callback(false);
	});

	$('#confirmTrue').click(function(){
		$('#confirmModal').modal('hide');
		if (callback) callback(true);
	});
}