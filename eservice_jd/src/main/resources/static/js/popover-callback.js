$('*[data-toggle="popover"]').on('shown.bs.popover', function () {
	
	$('.popover').hide();

	var _this = $(this);
	var target = $(this).data("target");
	var pop = $(this).next(".popover");
	var paymentStatus = $(this).data("payment");
	var payTarget = $(this).data("paytarget");

	pop.show();
	// pop.css({ "max-width": _this.data('width')+"px" }).addClass(_this.data('class'));
	pop.find(".popover-content").html($(target).clone().html());

	if( pop.find(".popover-title").html()!="" ) {
		pop.find(".popover-title").append(
			$("<a/>",{ class:"close"}).html("&times;").click(function(){ 
				_this.popover("toggle");
			})
		);
	} else {
		pop.find(".close").click(function(){ 
			_this.popover("toggle");
		});
	}
	if(paymentStatus == "show"){
		//pop.find(".payOnline").html($(patTarget).clone().html());
		pop.find(".payOnline").html($(payTarget).clone().html());
	}
	pop.find(".popover-content .items").height(_this.data("scroll-height")+"px").mCustomScrollbar({
	     callbacks:{
                onTotalScroll:function() {
                    popoverCallback()
                }
            }
	    }
	);
}).popover();
