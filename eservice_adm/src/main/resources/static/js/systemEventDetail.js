// 		<![CDATA[
   		    var businessEventId = ""; 
			jQuery(function($) {
				//initiate dataTables plugin
				console.log("getSystemEventDetail by js");
				
				businessEventId = request("businessEventId");
				
				postJson("/eservice_adm/eventRecord/getSystemEventDetail", businessEventId);
				
			});
			
			function postJson(url, data) {
				$.ajax({
					type : 'POST',
					contentType : 'application/json',
					url : url,
					data : data,
					success : function(response) {
						var showTables = "";
						var showDialog = "";
						
						$.each(response.resultData, function(idx, obj) {
							
							var idxs = idx;
							idxs = idxs + 1;
							
							var tableS = '<table id="table'+idxs+'" class="table table-bordered table-hover">';
							var thead = '<thead><tr><th align="left">資料存取-'+idxs+'</th></tr></thead>';
							var tbodyS = '<tbody><tr><td><div class="table-detail"><div class="row"><div class="col-xs-12 col-sm-12">'+
											'<div class="space visible-xs"></div><div class="profile-user-info profile-user-info-striped">';				
							var tbodyE = '</div></div></div></div></td></tr></tbody>';
							var tableE = '</table>';
							
							var execDate = '<div class="profile-info-row"><div class="profile-info-name" style="width:200px;">'+
								' 存取物件-日期時間 </div><div class="profile-info-value"><span>'+
								obj.execDate+'</span></div></div>';
							var execMethod = '<div class="profile-info-row"><div class="profile-info-name">'+
								' 存取物件-方法類型 </div><div class="profile-info-value"><span>'+
								obj.execMethod+'</span></div></div>';
							var execUser = '<div class="profile-info-row"><div class="profile-info-name">'+
								' 存取物件-帳號名稱 </div><div class="profile-info-value"><span>'+
								obj.execUser+'</span></div></div>';
							var execFile = '<div class="profile-info-row"><div class="profile-info-name">'+
								' 存取物件-資料名稱 </div><div class="profile-info-value"><span>'+
								obj.execFile+'</span></div></div>';
							var execSql = '<div class="profile-info-row"><div class="profile-info-name">'+
								' 存取物件-SQL </div><div class="profile-info-value">'+
								'<button class="pull-left btn btn-yellow" type="button" id="showSql_'+idxs+'">'+
								'...</button></div></div>';
							var execCount = '<div class="profile-info-row"><div class="profile-info-name">'+
								' 存取物件-筆數 </div><div class="profile-info-value"><span>'+
								obj.execCount+'</span></div></div>';
								
								var state = "";
								if(obj.execStatus == 1){
									state = "成功";
								}else{
									state = "失敗";
								}
							var execStatus = '<div class="profile-info-row"><div class="profile-info-name">'+
								' 存取物件-狀態 </div><div class="profile-info-value"><span>'+
								state+'</span></div></div>';
							var execMsg = '<div class="profile-info-row"><div class="profile-info-name">'+
								' 存取物件-偵錯訊息 </div><div class="profile-info-value">'+
								'<button class="pull-left btn btn-yellow" '+
								'type="button" id="showMsg_'+idxs+'">...</button></div></div>';
								
								
							var tbodyRow = execDate + execMethod + execUser + execFile + execSql + execCount + execStatus + execMsg;
							
							showTables = showTables + tableS + thead + tbodyS + tbodyRow + tbodyE + tableE;
							
							var showSqlDialog = '<div id="showSqlDialog_'+idxs+'" class="hide"><div class="widget-main no-padding">'+
								'<span>'+ obj.execSql +'</span></div></div>';
							var showMsgDialog = '<div id="showMsgDialog_'+idxs+'" class="hide"><div class="widget-main no-padding">'+
								'<span>'+ obj.execMsg +'</span></div></div>';
							showDialog = showDialog + showSqlDialog + showMsgDialog;
							
						});
						
						if(response.resultData.length == 0){
							var tableS = '<table id="table0" class="table table-bordered table-hover">';
							var thead = '<thead><tr><th align="left">資料存取-0</th></tr></thead>';
							var tbody = '<tbody><tr><td>查無資料...</td></tr></tbody>';
							var tableE = '</table>';
							
							showTables = tableS + thead + tbody + tableE
						}
						
//						showTables = showTables +'<div class="col-xs-12 col-sm-12">'+
//							'<button class="pull-right btn btn-info" type="button" onclick="retunrSearch()">'+
//							'<i class="my-icon fa fa-angle-double-left"></i>'+
//							'返回查詢頁面</button></div>';
						$("#createTable").append(showTables);
						$("#showDialog").append(showDialog);
						
						//顯示彈出視窗
						showDialogF();
					},
					error : function() {
						alert('error!')
					}
				});
			}
			
			function showDialogF(){
				//彈出視窗上方
				$.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
					_title: function(title) {
						var $title = this.options.title || '&nbsp;'
						if( ("title_html" in this.options) && this.options.title_html == true )
							title.html($title);
						else title.text($title);
					}
				}));
				
				//Sql
				$("button[id^=showSql_]").on('click', function(e) {
					e.preventDefault();
					
					var idx = $(this).attr("id").substring($(this).attr("id").indexOf("_")+1);
					
					var dialog = $("#showSqlDialog_"+idx).removeClass('hide').dialog({
						modal: true,
						title: "<div class='widget-header widget-header-small center'><h4 class='smaller'>SQL語法</h4></div>",
						title_html: true,
						buttons: [ 
							{
								text: "確認",
								"class" : "btn btn-primary btn-minier",
								click: function() {
									$( this ).dialog( "close" ); 
								} 
							}
						],
						width: 600
					});
				});
				
				//msg
				$("button[id^=showMsg_]").on('click', function(e) {
					e.preventDefault();
					
					var idx = $(this).attr("id").substring($(this).attr("id").indexOf("_")+1);
			
					var dialog = $("#showMsgDialog_"+idx).removeClass('hide').dialog({
						modal: true,
						title: "<div class='widget-header widget-header-small center'><h4 class='smaller'>偵錯訊息</h4></div>",
						title_html: true,
						buttons: [ 
							{
								text: "確認",
								"class" : "btn btn-primary btn-minier",
								click: function() {
									$( this ).dialog( "close" ); 
								} 
							}
						],
						width: 600
					});
				});
				
			}
			
			//取得參數
			function request(paras){   
		        var url = location.href;   
		        var paraString = url.substring(url.indexOf("?")+1,url.length).split("&");   
		        var paraObj = {}   
		        for (i=0; j=paraString[i]; i++){   
		            paraObj[j.substring(0,j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=")+1,j.length);   
		        }   
		        var returnValue = paraObj[paras.toLowerCase()];   
		        if(typeof(returnValue)=="undefined"){   
		            return "";   
		        }else{   
		            return returnValue;   
		        }   
			}
			
//			function retunrSearch(){
//				window.location.href = "/eservice_adm/eventRecord";
//			}
//		]]>	