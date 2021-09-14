// 		<![CDATA[
   		    var businessEventId = "";
			jQuery(function($) {
				console.log("getBussinessEventDetail by js");
				
				businessEventId = request("businessEventId");

				postJson("/eservice_adm/eventRecord/getBusinessEventDetail", businessEventId);
				
				//彈出視窗上方
				$.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
					_title: function(title) {
						var $title = this.options.title || '&nbsp;'
						if( ("title_html" in this.options) && this.options.title_html == true )
							title.html($title);
						else title.text($title);
					}
				}));
				
				//彈出視窗
				$("#id-btn-dialog1").on('click', function(e) {
					e.preventDefault();
			
					var dialog = $( "#dialog-message" ).removeClass('hide').dialog({
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
			});
			
			function postJson(url, data) {
				$.ajax({
					type : 'POST',
					contentType : 'application/json',
					url : url,
					data : data,
					success : function(response) {
						$("#userId").text(response.resultData.userId);
						$("#eventDate").text(response.resultData.eventDate);
						$("#sourceIp").text(response.resultData.sourceIp);
						$("#targetIp").text(response.resultData.targetIp);
						$("#targetSystemId").text(response.resultData.targetSystemId);
						$("#eventName").text(response.resultData.eventName);
						$("#eventCode").text(response.resultData.eventCode);
						var stat = "";
						if(response.resultData.eventStatus == "1"){
							stat = "成功";
						}else{
							stat = "失敗";
						}
						$("#eventStatus").text(stat);
						$.each(response.resultData.eventConditions, function(idx, obj) {
							var td = "<td>"+obj.eventConditionName+"</td>";
							td = td + "<td>"+obj.eventConditionKey+"</td>";
							td = td + "<td>"+obj.eventConditionValue+"</td>";
							$('#eventCondition').find('tbody').append("<tr>"+td+"</tr>");
						});
						$.each(response.resultData.eventParameters, function(idx, obj) {
							var td = "<td>"+obj.eventParameterName+"</td>";
							td = td + "<td>"+obj.eventParameterCode+"</td>";
							td = td + "<td>"+obj.eventParameterValue+"</td>";
							$('#eventParameter').find('tbody').append("<tr>"+td+"</tr>");
						});
						$("#eventMsg").append(response.resultData.eventMsg);
					},
					error : function() {
						alert('error!')
					}
				});
			}
			
			function searchSystemEventDetail(){
				window.location.href = "/eservice_adm/systemEventDetail?businessEventId="+businessEventId;
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
//		 ]]>