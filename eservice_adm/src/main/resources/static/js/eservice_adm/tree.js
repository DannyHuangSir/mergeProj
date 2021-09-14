function aceTreeUtil() {
	var obj = this;
	
	obj.fnInitRoot;	   // 根節點初始化的function
	obj.fnInitComplete;   // 載入完成後的function
	obj.fnNodeSelected;   // 節點選擇function
	obj.fnNodeDeselected; // 節點反選function

	obj.sTreeId = '';   // aceTree的ID
	obj.sQueryUrl = ''; // 查詢路徑
	
	obj.show = function() {
		eserviceForm.post(obj.sQueryUrl, {}, function(response) {
			// 一開始只有系統沒有根目錄時須新增根目錄
			if (response.resultData == null || (response.resultData != null && response.resultData.length == 0)) {
				if (obj.fnInitRoot) {
					obj.fnInitRoot(response);
				}
			} else {
				// 顯示功能樹
				obj.showAceTree(response.resultData[0]);
				// 初始功能樹事件
				obj.initTreeEvent();
			}
			
			if (obj.fnInitComplete) {
				obj.fnInitComplete();
			}
		});
	};
	
	// 顯示功能樹
	obj.showAceTree = function(treeData) {
		$(obj.sTreeId).ace_tree({
			dataSource: function(options, callback) {
				if (!('text' in options) && !('type' in options)) {
					callback({
						data: treeData
					});
					return;
				} else if ('type' in options && options.type == 'folder') {
					if ('additionalParameters' in options && "children" in options.additionalParameters) {
						callback({
							data: (options.additionalParameters.children)
						});
					} else {
						callback({
							data: {}
						});
					}
				}
			},
			loadingHTML:'<div class="tree-loading"><i class="my-icon fa fa-refresh fa-spin blue"></i></div>',
			'open-icon' : 'my-icon fa fa-folder-open',
			'close-icon' : 'my-icon fa fa-folder',
			'itemSelect' : true,
			'folderSelect': true,
			'multiSelect': false,
			'selected-icon' : null,
			'unselected-icon' : null,
			'folder-open-icon' : 'my-icon tree-plus',
			'folder-close-icon' : 'my-icon tree-minus'
		});
	}
	
	// 初始事件
	obj.initTreeEvent = function() {
		$('.tree-container-min').ace_scroll({size: 400, mouseWheelLock: true});
		
		$(obj.sTreeId).on('selected.fu.tree', function(e, data) {
			if (obj.fnNodeSelected) {
				obj.fnNodeSelected(data);
			}
		}).on('deselected.fu.tree', function(e) {
			if (obj.fnNodeDeselected) {
				obj.fnNodeDeselected();
			}
		}).on('closed.fu.tree disclosedFolder.fu.tree', function() {
			$('.tree-container-min').ace_scroll('reset').ace_scroll('start');
		});
		
		// 自動產開
		$(obj.sTreeId).find(".tree-branch").each(function(){
			if($(this).find('.tree-label').html() != '') { 
				$(this).find('i').click();
				$(this).removeClass('tree-selected');
				$(this).find('.tree-branch-children').find('.tree-branch').each(function() {
					$(this).find('i').click();
					$(this).removeClass('tree-selected');
				});
			}
		});
	};
}

function jsTreeUtil() {
	var obj = this;
	var dataList;
	
	obj.fnInitRoot;	   // 根節點初始化的function
	obj.fnInitComplete;   // 載入完成後的function
	obj.fnReady;
	obj.fnAfterOpen;

	obj.sTreeId = '';   // jsTree的ID
	obj.sQueryUrl = ''; // 查詢路徑
	
	obj.show = function(formdata) {
		eserviceForm.post(obj.sQueryUrl, formdata, function(response) {
			dataList = response.resultData;
			$(obj.sTreeId).jstree({
				'checkbox': {
					"keep_selected_style": false
				},
				"plugins": ["checkbox"],
				"core" : {
					"data" : dataList
				}
			});
			
			obj.initTreeEvent();
			
			if (obj.fnInitComplete) {
				obj.fnInitComplete();
			}
		});
	};
	
	// 初始事件
	obj.initTreeEvent = function() {
		$(obj.sTreeId).on('ready.jstree', function () {
			if (obj.fnReady) {
				obj.fnReady();
			}
		});
		
		$(obj.sTreeId).bind("after_open.jstree", function () {
			if (obj.fnAfterOpen) {
				obj.fnAfterOpen();
			}
		});
	};
	
	obj.removeDivItemSelect = function() {
		$.each($('li[id^=div]'), function(i, e) {
			$(this).find('.jstree-checkbox').removeClass('jstree-checkbox').addClass('jstree-checkbox2');
			$(this).find('.jstree-anchor').removeClass('jstree-anchor').removeClass('jstree-clicked').addClass('jstree-anchor2').attr("onclick","checkNon('"+$(this).attr("id")+"')"); 
			$(this).find("a").removeAttr("href");
		});
	};
	
	obj.getSelectSysId = function() {
		var allFunIds = obj.getSelectFuncId();
		var sysIds = [];
		
		$.each(allFunIds, function(i, funId) {
			$.each(obj.getDataList(), function(i, obj) {
				if(funId == obj.id) {
					if (!sysIds.includes(obj.sysId)) {
						sysIds.push(obj.sysId);
					}
				}
			 });
		 });
		
		return sysIds;
	};
	
	obj.getSelectFuncId = function() {
		var allFunIds = [];
		$('.jstree-undetermined,.jstree-clicked').each(function(i, obj) {
			var funId = $(obj).closest('li[role="treeitem"]').attr('id');
			if (funId.indexOf('div_') < 0) {
				allFunIds.push(funId);
			}
		});
		return allFunIds;
	};
	
	obj.getSelectDivId = function() {
		var divIds = [];
		$('.jstree-undetermined,.jstree-clicked').each(function(i, obj) {
			var funId = $(obj).closest('li[role="treeitem"]').attr('id');
			if (funId.indexOf('div_') >= 0) {
				divIds.push(funId);
			}
		});
		return divIds;
	};
	
	// 取得資料集合
	obj.getDataList = function() {
		return dataList;
	};
}