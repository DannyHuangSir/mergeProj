var eserviceAdmEvent = function() {
	return {
		initChosenSelect : function() {
			$('.chosen-select').chosen({allow_single_deselect:true});
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
				if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
				 else $('#form-field-select-4').removeClass('tag-input-style');
			});
		},
		initDateRangePicker : function(idSelector) {
			$(idSelector).daterangepicker({
				applyClass : 'btn-sm btn-success',
				cancelClass : 'btn-sm btn-default',
				startDate : moment().add(-12, 'month'),
				maxDate : new Date(),
				format:'YYYY/MM/DD',
				locale: {
					applyLabel: 'Apply',
					cancelLabel: 'Cancel'
				}
			});
		},
		initDateRangePickerNotAutoUpdate : function(idSelector) {
			$(idSelector).daterangepicker({
				autoUpdateInput : false,
				applyClass : 'btn-sm btn-success',
				cancelClass : 'btn-sm btn-default',
				maxDate : new Date(),
				format:'YYYY/MM/DD',
				locale: {
					applyLabel: 'Apply',
					cancelLabel: 'Cancel'
				}
			});
			
			$(idSelector).on('apply.daterangepicker', function(ev, picker) {
				$(this).val(picker.startDate.format('YYYY-MM-DD') + ' ~ ' + picker.endDate.format('YYYY-MM-DD'));
			});

			$(idSelector).on('cancel.daterangepicker', function(ev, picker) {
				$(this).val('');
			});
		},
		initDialog : function() {
			$.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
				_title: function(title) {
					var $title = this.options.title || '&nbsp;'
					if(("title_html" in this.options) && this.options.title_html == true)
						title.html($title);
					else title.text($title);
				}
			}));
		}
	};
}();