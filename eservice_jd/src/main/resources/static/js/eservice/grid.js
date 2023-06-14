function eserviceGrid() {
	var obj = this;
	var formData = {};
	var $formObj;
	var rownum;
	var dataList;
	
	obj.sGridId = '';           // grid的ID
	obj.sQueryUrl = '';         // 查詢路徑
	obj.sSummayHtml = '';       // 合計顯示html
	obj.sRowClass = '';         // 自訂row 的CSS
	obj.sRowClickFun = '';      // 自訂row 的 onclick事件
	obj.sRowClickFunValue = ''; // 自訂row 的 onclick事件 傳入值
	obj.aoColumns = [];         // 欄位設定
	obj.fnInitComplete;         // grid 完成後載入的function
	obj.fnRowClick;             // row點擊事件
	obj.bNoEventStyle = true;   // 預設沒有小手點擊圖示，若要小手點擊圖示設定為false
	obj.iDisplayLength = 4;     // 分頁預設顯示筆數
	obj.resp = null

	// 無分頁grid
	obj.createSimple = function(pageMode) {
		$.ajax({
			type : 'POST',
			url: obj.sQueryUrl,
			data : formData
		}).done(function (response) {
			hideLoadingModal();
		    obj.resp = response
			if (response.result == 'SUCCESS') {
				$(obj.sGridId).find('tbody').eq(0).html('');
				rownum = $(response.resultData).size();
				dataList = response.resultData;
				$.each(response.resultData, function(i, oRow) {
					// Note: noEvent(沒有點擊小手圖示)
					var trStyle = '';
					if (obj.bNoEventStyle) {
						trStyle = 'noEvent';
						if ((i + 1) % 2 == 0) {
							trStyle = 'tr-odd-noEvent';
						}
					} else {
						trStyle = '';
						if ((i + 1) % 2 == 0) {
							trStyle = 'tr-odd';
						}
					}
					
					// 若有 gradient2 style，不需要偶數row變色
					if ($(obj.sGridId).hasClass('gradient2')) {
						trStyle = '';
					}
					var trObj = $('<tr class="' + obj.sRowClass + ' ' + trStyle + '"></tr>');
					
					// 直接把自訂row click 加入
					if (obj.sRowClickFun != '') {
						var funValue = "";
						var funList = obj.sRowClickFunValue.split(",");
						$.each(funList, function(t, fun) {
							if (funValue == "") {
								funValue = "'" + oRow[fun] + "'";
							} else {
								funValue = funValue + ",'" + "" + oRow[fun] + "'";
							}
						});
						trObj = $('<tr onclick="' + obj.sRowClickFun + '(' + funValue + ')"></tr>');
					}
					
					$.each(obj.aoColumns, function(i, oColumn) {
						var tdClass = (oColumn.tdClass == null ? '' : oColumn.tdClass);
						var rwdName = (oColumn.rwdName == null ? '' : oColumn.rwdName);
						var tdHtml = '<td class="' + tdClass + '">';
						if (rwdName != '') {
							tdHtml += '<b class="tablesaw-cell-label">' + rwdName + '<br></b>';
						}
						if (!oColumn.notCellContent) {
							tdHtml += '<span class="tablesaw-cell-content">';
						}
						
						// 從設定的屬性取得資料
						if (oColumn.mDataProp) {
							tdHtml +=  emptyIfNull(oRow[oColumn.mDataProp]);
						} else {
							// 若無設定屬性，有fnRender去處理該欄位的特殊顯示
							if (oColumn.fnRender) {
								tdHtml +=  oColumn.fnRender(oRow);
							}
						}
						if (!oColumn.notCellContent) {
							tdHtml += '</span>';
						}
						tdHtml += '</td>';
						trObj.append(tdHtml);
					});
					
					// 表格row 點擊事件
					if (obj.fnRowClick) {
						$(trObj).click(function() {
							obj.fnRowClick(oRow);
						});
					}
					$(obj.sGridId).find('tbody').eq(0).append(trObj);
				});
				
				// 若表格有自訂的備註html，則附加在最下方
				if (obj.sSummayHtml != '') {
					$(obj.sGridId).find('tbody').eq(0).append(obj.sSummayHtml);
				}
				
				// 若分頁模式，顯示pagebar
				if (pageMode) {
					if (response.resultData.length > 0) {
						rownum = response.resultData[0].totalRow;
					}
					
					$('.pagination').html(getPageBarHtml(response));
				}
				
				// grid載入完成後，執行自訂的function
				if (obj.fnInitComplete) {
					obj.fnInitComplete();
				}
			} else {
				openAlert(response.resultMsg);
			}
		}).fail(function (jqXHR, textStatus) {
			hideLoadingModal();
			// alert('error!');
		});
	};
	
	// 查詢(呼叫時，需先手動setPostData)
	obj.queryByPage = function(pageNum) {
		obj.setPostData('pageNum', pageNum);
		obj.createSimple(true);
	};

	// 設定查詢條件
	obj.setPostData = function(name, value) {
		formData[name] = value;
	};
	
	// 取得資料筆數
	obj.rownum = function() {
		return rownum;
	};
	
	// 取得資料集合
	obj.getDataList = function() {
		return dataList;
	};
	
	// 根據欄位取得合計
	obj.sumByField = function(fieldName) {
		var sum = 0;
		$.each(dataList, function(i, oRow) {
			if (oRow[fieldName]) {
				sum += oRow[fieldName];
			}
		});
		return sum;
	};
};
