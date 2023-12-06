// 		<![CDATA[
		var eventArray = [];
		var eventTypes = [];
		var hasSelectData = null;
			jQuery(function($) {
				console.log("initSelect by js");
				//$("#searchError").hide();
				$("#showResult").hide();
				
				if(!my.vars['touch']) {
					$('.chosen-select').chosen({allow_single_deselect:true}); 
					//resize the chosen on window resize
					$(window)
					.off('resize.chosen')
					.on('resize.chosen', function() {
						$('.chosen-select').each(function() {
							 var $this = $(this);
							 $this.next().css({'width': $this.parent().width()});
						})
					}).trigger('resize.chosen');
					//resize chosen on sidebar collapse/expand
					$(document).on('settings.my.chosen', function(e, event_name, event_val) {
						if(event_name != 'sidebar_collapsed') return;
						$('.chosen-select').each(function() {
							 var $this = $(this);
							 $this.next().css({'width': $this.parent().width()});
						})
					});
					
					$('#chosen-multiple-style .btn').on('click', function(e){
						var target = $(this).find('input[type=radio]');
						var which = parseInt(target.val());
						if(which == 2){
							$('#form-field-select-4').addClass('tag-input-style');
						}
						 else $('#form-field-select-4').removeClass('tag-input-style');
					});
				}
				
				postJson("/eservice_adm/eventRecord/initSelect");
				
			});
					
			function postJson(url){
				$.ajax({
					type : 'POST',
					contentType : 'application/json',
					url : url,
					data : null,
					success : function(response) {
						if(response.resultData) {
							if(response.resultData.systems) {
								$("#systems option").remove();
								$("#systems").append($("<option></option>").attr("value", "").text(""));
								$.each(response.resultData.systems, function(inx, obj) {
									$("#systems").append($("<option></option>").attr("value", obj.sysId).text(obj.sysName));
								});
								$("#systems").trigger("chosen:updated");
							} else {
								console.log("resultData.systems is null!");
							}
							if(response.resultData.eventTypes) {
								eventTypes = response.resultData.eventTypes;
								console.log("response.resultData.eventTypes=",eventTypes);
								$("#eventName option").remove();
								$("#eventName").append($("<option></option>").attr("value", "").text(""));
								$.each(response.resultData.eventTypes, function(inx, obj) {
									$("#eventName").append($("<option></option>").attr("value", obj.parameterValue).text(obj.parameterName));
									var event = {eventCode: obj.parameterValue, eventName: obj.parameterName};
									eventArray.push(event);
								});
								$("#eventName").trigger("chosen:updated");
							} else {
								console.log("resultData.eventTypes is null!");
							}
							if(response.resultData.eventStatus) {
								$("#eventStatus option").remove();
								$("#eventStatus").append($("<option></option>").attr("value", "").text(""));
								$.each(response.resultData.eventStatus, function(inx, obj) {
									$("#eventStatus").append($("<option></option>").attr("value", obj.parameterValue).text(obj.parameterName));
								});
								$("#eventStatus").trigger("chosen:updated");
							} else {
								console.log("resultData.eventStatus is null!");
							}
						} else {
							console.log("response.resultData is null!");
						}
						
					},
					error : function() {
						alert('system initial error!')
					}
				});
			}
			//系統別變更時，應重整
//			$("#systems").chosen().change(function(){
//				if($(this).val() != ""){
//					$("#eventCode").val($(this).val());
//				}else{
//					$("#eventCode").val("");
//				}
//			});
			
			$("#eventName").chosen().change(function(){
				if($(this).val() != ""){
					$("#eventCode").val($(this).val());
				}else{
					$("#eventCode").val("");
				}
			});
			
			function changEventCode(){
				var code = $("#eventCode").val();
				if(code.trim() != ""){
					$("#eventName").val(code);
					$("#eventName").trigger("chosen:updated");
				}else{
					$("#eventName").val("");
					$("#eventName").trigger("chosen:updated");
				}
			}
			
			function selectForm(){
				console.log("selectForm by js");
				var code = $("#eventName").val();
				var eventName = "";
				$.each(eventArray, function(ind, obj) {
					if(obj['eventCode'] == code){
						eventName = obj['eventName'];
					}
				});
				
				//檢查欄位
				if($("#systems").val()==""){
					$("#searchError").show();
					$("#searchErrorCont").text("查詢條件「系統別」 欄位不允許為空，請重新確認!");
					return;
				}else{
					$("#searchError").hide();
				}
				if($("#userId").val() == "" && $("#eventCode").val() == "" && $("#eventName").val() == ""){
					$("#searchError").show();
					$("#searchErrorCont").text("查詢條件「帳號、事件代碼、事件名稱」 欄位至少輸入一項，請重新確認!");
					return;
				}
				
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
			//var d3=(new Date(endDate)-new Date(startDate))/86400000;
			// if(d3 > 7){
			// 	$("#searchError").show();
			// 	$("#searchErrorCont").text("查詢條件「記錄時間起迄」 區間必須小於 3 天，請重新確認!");
			// 	return;
			// }
					
				var data = {
						userId : $("#userId").val(),
						targetSystemId : $("#systems").val(),
						eventName : eventName,
						eventCode : $("#eventCode").val(),
						eventStatus : $("#eventStatus").val(),
						startDate : $("#startDate").val(),
						endDate : $("#endDate").val()
				}
				hasSelectData = data;
				$("#showResult").hide();
				postDetailJson("/eservice_adm/eventRecord/getEventRecordTable", data);
				$('#show-detail-results').show();
			}
			
			function postDetailJson(url, data){
				$.ajax({
					type : 'POST',
					contentType : 'application/json',
					url : url,
					data : JSON.stringify(data),
					success : function(response) {
						var grid_data = [];
						if(response.resultData) {
							if(response.resultData.length == 0){
								jQuery("#grid-table").jqGrid('clearGridData');  //清空表格
								$("#showResult").show();
								$("#showResultError").text("查無資料!");
								return;
							}
							$.each(response.resultData, function(idx, obj) {
								var data = {
										systemId : obj.targetSystemId,
										recordTime : obj.eventDate,
										eventCode: obj.eventCode,
										eventName: obj.eventName,
										userId : obj.userId,
										busineRecord:"<a href='/eservice_adm/businessEventDetail?businessEventId="+obj.businessEventId+"' target='showDetailFrame' onclick='openRecordDetailModal()' class='green bigger-140 show-details-btn' title='Show Details'> <i class='my-icon fa fa-angle-double-down'></i> <span class='sr-only'>Details</span> </a>"}
								grid_data.push(data);
							});
						}
						
						
						if(response.resultMsg != ""){
							$("#errorMsg").text(response.resultMsg);
							$("#showMessage").show();
						}else{
							$("#showMessage").hide();
							//儲存查詢條件
							hasSelect = true;
							sysId = $("#systemsSelect").val();
							roleName = $("#roleName").val();
						}
						showJqGrid(grid_data);
						var grid_selector = "#grid-table";
						jQuery(grid_selector).jqGrid().trigger("reloadGrid");
					},
					error : function() {
						alert('error!')
					}
				});
			}
			function showJqGrid(grid_data){
				var grid_selector = "#grid-table";
				var pager_selector = "#grid-pager";
				
				var pa = $(grid_selector).closest('[class*="col-"]');
				$(grid_selector).jqGrid( 'setGridWidth', '1920');
				//resize to fit page size
				$(window).on('resize.jqGrid', function () {
					$(grid_selector).jqGrid( 'setGridWidth', pa.width() );
			    })
				
				//resize on sidebar collapse/expand
				$(document).on('settings.my.jqGrid' , function(ev, event_name, collapsed) {
					if( event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed' ) {
						//setTimeout is for webkit only to give time for DOM changes and then redraw!!!
						setTimeout(function() {
							alert(parent_column.width());
							$(grid_selector).jqGrid( 'setGridWidth', parent_column.width() );
						}, 20);
					}
			    })

			    $(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
				
				jQuery(grid_selector).jqGrid('clearGridData');  //清空表格
			    
			    jQuery(grid_selector).jqGrid({
					subGridOptions : {
						plusicon : "my-icon fa fa-plus center bigger-110 blue",
						minusicon  : "my-icon fa fa-minus center bigger-110 blue",
						openicon : "my-icon fa fa-chevron-right center orange"
					},
					//for this example we are using local data
					data: grid_data,
					datatype: "local",
					height: 250,
					colNames:['系統別', '記錄時間','事件代碼','事件名稱', '帳號', '業務事件記錄'],
					colModel:[
						{name:'systemId',index:'systemId', editable:true},
						{name:'recordTime',index:'recordTime',editable:true},
						{name:'eventCode',index:'eventCode',editable: true},
						{name:'eventName',index:'eventName', editable: true},
						{name:'userId',index:'userId', editable: true},
						{name:'busineRecord',index:'busineRecord', sortable:false, align:'left'}
					], 
			
					viewrecords : true,
					rowNum:10,
					rowList:[10,20,30],
					pager : pager_selector,
			        multiboxonly: true,
					loadComplete : function() {
						var table = this;
						setTimeout(function(){
							updatePagerIcons(table);
							
						}, 0);
					},
					editurl: "./dummy.php",//nothing is saved
					caption: "事件記錄查詢"
				});
			    
				jQuery(grid_selector).jqGrid('setGridParam',{  // 重新加载数据
			        datatype:'local',
			        data : grid_data,   //  newdata 是符合格式要求的需要重新加载的数据 
					page : 1
			  	}).trigger("reloadGrid");

				$('#show-detail-results').show();
			}
			
			//replace icons with FontAwesome icons like above
			function updatePagerIcons(table) {
				var replacement = 
				{
					'ui-icon-seek-first' : 'my-icon fa fa-angle-double-left bigger-140',
					'ui-icon-seek-prev' : 'my-icon fa fa-angle-left bigger-140',
					'ui-icon-seek-next' : 'my-icon fa fa-angle-right bigger-140',
					'ui-icon-seek-end' : 'my-icon fa fa-angle-double-right bigger-140'
				};
				$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
					var icon = $(this);
					var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
					
					if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
				})
			}

			function getFormatDate(d){
				return d.getMonth()+1 + '/' + d.getDate() + '/' + d.getFullYear();
			}
			
			function downloadEventRecord(){
				if(hasSelectData != null){
					window.location.href = "/eservice_adm/eventRecord/downloadEventRecord?userId="+
					hasSelectData.userId+
					"&targetSystemId="+hasSelectData.targetSystemId+
					"&eventName="+hasSelectData.eventName+
					"&eventCode="+hasSelectData.eventCode+
					"&eventStatus="+hasSelectData.eventStatus+
					"&startDate="+hasSelectData.startDate+
					"&endDate="+hasSelectData.endDate;
				}else{
					alert("查詢結果清單無任何資料筆數無法執行匯出，請重新查詢!");
				}
			}

			$('input[id=eventDate]').daterangepicker({
				'autoUpdateInput' : false,
				'applyClass' : 'btn-sm btn-success',
				'cancelClass' : 'btn-sm btn-default',
			maxDate : new Date(),		
				locale: {
					format:'YYYY-MM-DD',
					applyLabel: 'Apply',
					cancelLabel: 'Cancel',
				},
				'dateLimit': {
			        "days": 7
			    },
			});

			function changeEventDate(){
				var eventDate = $("#eventDate").val();
				$("#startDate").val(eventDate.substring(0,eventDate.indexOf("~")).trim());
				$("#endDate").val(eventDate.substring(eventDate.indexOf("~")+1).trim());
			}
			
			$('input[id=eventDate]').on('apply.daterangepicker', function(ev, picker) {
				$("#startDate").val(picker.startDate.format('YYYY-MM-DD'));
				$("#endDate").val(picker.endDate.format('YYYY-MM-DD'));
				$(this).val(picker.startDate.format('YYYY-MM-DD') + ' ~ ' + picker.endDate.format('YYYY-MM-DD'));
			});

			$('input[id=eventDate]').on('cancel.daterangepicker', function(ev, picker) {
				$("#startDate").val("");
				$("#endDate").val("");
				$(this).val("");
		});			
		
			
			function resetForm(){
				document.getElementById('eventRecord').reset();
				$("#systems").trigger("chosen:updated");
				$("#eventName").trigger("chosen:updated");
				$("#eventStatus").trigger("chosen:updated");
				document.getElementById('eventRecord').reset();
			}
			
			function openRecordDetailModal() {
				$('#showDetailModal').modal('show');
				
			}
			

//		 	]]>
