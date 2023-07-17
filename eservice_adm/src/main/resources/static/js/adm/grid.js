function datatableUtil() {
	$.fn.dataTable.ext.errMode = 'none';
	var obj = this;
	var oTable;
	var formData = {};
	
	obj.sGridId = '';   // grid的ID
	obj.sQueryUrl = ''; // 查詢路徑
	obj.aoColumns = []; // 欄位設定
	obj.fnInitComplete; // grid 完成後載入的function
	obj.iDisplayLength = 10;

	obj.create = function() {
		if ($('#showMsg')) {
			$('#showMsg').text('查詢中...');
		}
		
		oTable = $(obj.sGridId).DataTable({
			language: {
				infoFiltered: ''
			},
			filter: false,
			bAutoWidth: false,
			serverSide: true,
			pageSize : 10,
			aaSorting: [],
			aoColumns : obj.aoColumns,
			ajax:{
				type: 'POST',
				url: obj.sQueryUrl,
				contentType: 'application/json',
				dataType: 'json',
				data: function(data) {
					for(key in formData) {
						data[key] = formData[key];
					}
					return JSON.stringify(data);
				},
				dataSrc: function(data) {
					if (data.result == 'SUCCESS') {
						return data.aaData;
					} else {
						$('#showMsg').text('查詢失敗!');
						return '';
					}
				}
			},
			initComplete: function () {
				if ($('#showMsg') && $('#showMsg').html() != '查詢失敗!') {
					$(obj.sGridId + ' thead tr th').removeClass('sorting');
					$('#showMsg').text('查詢成功!');
					$('#showDetail').show();
					
					// 處理查無資料時，[未找到匹配的記錄]顯示在第一欄位
					if ($(obj.sGridId + ' tbody tr td:eq(0)').hasClass('dataTables_empty')) {
						var tdObj = $(obj.sGridId + ' tbody tr td:eq(0)');
						if (tdObj.attr('colspan') == '0') {
							tdObj.attr('colspan', obj.aoColumns.length);
						}
					}
				}
				
				if (obj.fnInitComplete) {
					obj.fnInitComplete();
				}
			}
		});
	};

	// 設定查詢條件
	obj.setPostData = function(name, value) {
		formData[name] = value;
	};

	// 設定查詢條件
	obj.setFormData = function(qryFormData) {
		formData = qryFormData;
	};
	
	// grid reload
	obj.reload = function() {
		oTable.ajax.reload();
	};
}

function jqGridUtil() {
	var obj = this;
	var oJqGrid;
	var formData = {};
	
	// 預設值，可由外面傳入覆寫
	obj.id = '';	  // grid的ID
	obj.url = '';	 // 查詢路徑
	obj.caption = ''; // 表頭顯示文字
	obj.datatype = 'json',
	obj.mtype = 'POST',
	obj.pager = '#pager';
	obj.rowNum = 10;
	obj.height = 250;
	obj.rowList = [10, 20, 30];
	obj.colNames = [];
	obj.colModel = [];
	obj.sortable = false;
	obj.viewrecords = true;
	obj.loadComplete; // grid 完成後載入的function

	obj.create = function() {
		if (oJqGrid) {
			obj.clean();
			var postData = $(obj.id).jqGrid('getGridParam', 'postData');
			for (key in formData) {
				postData[key] = formData[key];
			}
			postData['page'] = 1; // 重新設定第一頁
			$(obj.id).jqGrid('setGridParam', postData).trigger("reloadGrid"); 
		} else {
			oJqGrid = $(obj.id).jqGrid({
				url: obj.url,
				datatype: obj.datatype,
				mtype: obj.mtype,
				ajaxGridOptions: {
					cache: false,
					contentType: 'application/json'
				},
				serializeGridData: function(postData) {
					if (postData.searchField === undefined) postData.searchField = null;
					if (postData.searchString === undefined) postData.searchString = null;
					if (postData.searchOper === undefined) postData.searchOper = null;
					
					// 由查詢物件取出放入postData
					for (key in formData) {
						postData[key] = formData[key];
					}
					return JSON.stringify(postData);
				},
				jsonReader: {
					root: function (obj) { return obj.rows; },
					page: function (obj) { return obj.page; },
					total: function (obj) { return obj.total; },
					records: function (obj) { return obj.records; }
				},
				colNames: obj.colNames,
				colModel: obj.colModel,
				pager: obj.pager,
				sortable: obj.sortable,
				rowNum: obj.rowNum,
				height: obj.height,
				altRows: true,
				rowList: obj.rowList,
				viewrecords: obj.viewrecords,
				caption: obj.caption,
				loadComplete: function(response) {
					if (response.result == 'SUCCESS') {
						$('#showMessage').hide();
					} else {
						$('#errorMsg').text(response.resultMsg);
						$('#showMessage').show();
					}
					
					// 重新設定寬度
					obj.reSizeWidth();
					updatePagerIcons();
					
					// 執行自訂頁面載入function
					if (obj.loadComplete) {
						obj.loadComplete();
					}
				},
			});			
		}
	};

	// 設定查詢條件
	obj.setPostData = function(name, value) {
		formData[name] = value;
	};

	// 設定查詢條件
	obj.setFormData = function(qryFormData) {
		formData = qryFormData;
	};
	
	// 取得查詢條件
	obj.getPostData = function(name) {
		return (formData[name] == null ? '' : formData[name]);
	};
	
	// 總筆數
	obj.rownum = function() {
		return oJqGrid.getGridParam("reccount");
	};
	
	// 設定寬度
	obj.reSizeWidth = function() {
		var newWidth = $(obj.id).closest(".ui-jqgrid").parent().width();
		oJqGrid.jqGrid("setGridWidth", newWidth, true);
	};
	
	// Reload
	obj.reload = function() {
		$(obj.id).trigger("reloadGrid");
	};
	
	// 清空表格
	obj.clean = function() {
		oJqGrid.jqGrid('clearGridData');
	};
}

// replace icons with FontAwesome icons like above
function updatePagerIcons(table) {
	var replacement = {
		'ui-icon-seek-first' : 'my-icon fa fa-angle-double-left bigger-140',
		'ui-icon-seek-prev' : 'my-icon fa fa-angle-left bigger-140',
		'ui-icon-seek-next' : 'my-icon fa fa-angle-right bigger-140',
		'ui-icon-seek-end' : 'my-icon fa fa-angle-double-right bigger-140'
	};
	$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function() {
		var icon = $(this);
		var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
		if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
	})
}