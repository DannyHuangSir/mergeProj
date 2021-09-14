function getApplyGrid1Columns() {
	return [
		{
			// radio
			'fnRender': function(oRow) {
				var radioHtml = '';
				radioHtml += '<div class="radio">';
				radioHtml += '    <label>';
				radioHtml += '        <div class="radio-btn" onclick="checkApplyGridRadio(this)"><i><input type="radio" name="chosePolicy" value="' + emptyIfNull(oRow.policyNo) + '"/></i></div>';
				radioHtml += '    </label>';
				radioHtml += '</div>';				
				return radioHtml;
			},
			'notCellContent': true
		},
		{
			// 保單
			'fnRender': function(oRow) {
				return emptyIfNull(oRow.productName) + '<p>(' + emptyIfNull(oRow.policyNo) + ')</p>';
			},
			'tdClass': 'table3'
		},
		{
			// 主被保險人
			'mDataProp': 'mainInsuredName'
		},
		{
			// 保單生效日
			'fnRender': function(oRow) {
				return westDate(oRow.policyStartDate);
			},
			'tdClass': 'enThin'
		},
		{
			// 約定扣款日
			'mDataProp': 'drawDay'
		},
		{
			// 每期保費
			'fnRender': function(oRow) {
				var paidAmountHtml = '';
				paidAmountHtml += formatNumber(oRow.paidAmount);
				paidAmountHtml += '<i class="my5">' + (emptyIfNull(oRow.currency)) + '</i>';
				return paidAmountHtml;
			},
		},
		{
			// 繳別
			'mDataProp': 'paymentMode'
		}
	];
};

function getApplyGrid2Columns() {
	return [
		{
			// radio
			'fnRender': function(oRow) {
				var radioHtml = '';
				radioHtml += '<div class="radio">';
				radioHtml += '    <label>';
				radioHtml += '        <div class="radio-btn" onclick="checkApplyGridRadio(this)"><i><input type="radio" name="chosePolicy" value="' + emptyIfNull(oRow.policyNo) + '"/></i></div>';
				radioHtml += '    </label>';
				radioHtml += '</div>';				
				return radioHtml;
			},
			'notCellContent': true
		},
		{
			// 保單
			'fnRender': function(oRow) {
				return emptyIfNull(oRow.productName) + '<p>(' + emptyIfNull(oRow.policyNo) + ')</p>';
			},
			'tdClass': 'table3'
		},
		{
			// 保單狀態
			'mDataProp': 'policyStatus',
			'tdClass': 'hid1 hid2'
		},
		{
			// 保單生效日
			'fnRender': function(oRow) {
				return westDate(oRow.policyStartDate);
			},
			'tdClass': 'enThin'
		},
		{
			// 保額
			'fnRender': function(oRow) {
				return formatNumber(oRow.insuredValue, 0);
			},
			'tdClass': 'hid1 hid2'
		},
		{
			// 每期保費
			'fnRender': function(oRow) {
				var paidAmountHtml = '';
				paidAmountHtml += formatNumber(oRow.paidAmount);
				paidAmountHtml += '<i class="my5">' + (emptyIfNull(oRow.currency)) + '</i>';
				return paidAmountHtml;
			},
		},
		{
			// 繳別
			'mDataProp': 'paymentMode'
		}
	];
};

function checkApplyGridRadio(radioDiv) {
	var table = $(radioDiv).closest("table");
	table.find('.radio-btn').removeClass('checkedRadio');
	table.find('.radio-btn').find(':radio').prop("checked", false);
	
	$(radioDiv).addClass('checkedRadio');
	$(radioDiv).find(':radio').prop("checked", true);
}