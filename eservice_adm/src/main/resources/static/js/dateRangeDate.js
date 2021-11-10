		/*<![CDATA[*/
			$(function() {
				$('input[id=dateRangeDate]').daterangepicker({
					'autoUpdateInput' : false,
					'applyClass' : 'btn-sm btn-success',
					'cancelClass' : 'btn-sm btn-default',
					maxDate : new Date(),
					format:'YYYY/MM/DD',
					locale: {
						applyLabel: 'Apply',
						cancelLabel: 'Cancel'
					}
				});
				
				$('input[id=dateRangeDate]').on('apply.daterangepicker', function(ev, picker) {
					$("#startDate").val(picker.startDate.format('YYYY-MM-DD'));
					$("#endDate").val(picker.endDate.format('YYYY-MM-DD'));
					$(this).val(picker.startDate.format('YYYY-MM-DD') + ' ~ ' + picker.endDate.format('YYYY-MM-DD'));
				});
	
				$('input[id=dateRangeDate]').on('cancel.daterangepicker', function(ev, picker) {
					$("#startDate").val("");
					$("#endDate").val("");
					$(this).val("");
				});
			});
			
			function changeEventDate(){
				var eventDate = $("#dateRangeDate").val();
				$("#startDate").val(eventDate.substring(0,eventDate.indexOf("~")).trim());
				$("#endDate").val(eventDate.substring(eventDate.indexOf("~")+1).trim());
			}
			/*]]>*/