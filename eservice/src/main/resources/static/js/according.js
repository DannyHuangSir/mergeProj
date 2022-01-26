$(function() {

    $(document).on('click', '.mobile-toggle[data-toggle="true"]', function(e) {
    	
    	if($(e.target).hasClass('mobile-toggle') || $(e.target).hasClass('foo-angle-up') || $(e.target).hasClass('foo-angle-down') ){
	        // console.log('mobile-toggle click');
	    	
	        //$(".fa-angle-down").removeClass("fa-angle-down").addClass("fa-angle-right");
	        //var toggle = $(this).parents(".mobile-toggle");
	        var toggle = $(this);
	        var idx = $(".mobile-toggle").index(toggle);
	        var arrowTarget = $(this).find('.mobile');
	
	        //解決保單內容的class不一致
	        // $(".foo-angle-down").each(function() {
	        //     $(this).removeClass("foo-angle-down").addClass("fa-angle-up fa-fw");
	        // });
	        // $(".foo-angle-right").each(function() {
	        //     $(this).removeClass("foo-angle-right").addClass("fa-angle-right fa-fw");
	        // });
	
	        // stupid function cause dashboard.html doesn't need arror.... 
	        var $desktopTarget = $(this).find('i:eq(1)');
	        var $mobileTarget = $(this).find('i.mobile');
	
	        if ($desktopTarget.hasClass('foo-angle-up')) {
	            $desktopTarget.removeClass('foo-angle-up').addClass('foo-angle-down');
	        }
	        else if ($desktopTarget.hasClass('foo-angle-down')) {
	            $desktopTarget.removeClass('foo-angle-down').addClass('foo-angle-up');
	        }
	
	        if (arrowTarget.hasClass('foo-angle-up')) {
	            arrowTarget.removeClass('foo-angle-up').addClass('foo-angle-down');
	        }
	        else if (arrowTarget.hasClass('foo-angle-down')) {
	            arrowTarget.removeClass('foo-angle-down').addClass('foo-angle-up');
	        }
	
	        // 20220120 by 203990
			$(this).find('svg').each(function(i, obj) {
				if ($(obj).attr('data-icon') == 'chevron-down') {
					$(obj).attr('data-icon', 'chevron-up');
					$(obj).removeClass('fa-chevron-down');
					$(obj).addClass('fa-chevron-up');
				} else if ($(obj).attr('data-icon') == 'chevron-up') {
					$(obj).attr('data-icon', 'chevron-down');
					$(obj).removeClass('fa-chevron-up');
					$(obj).addClass('fa-chevron-down');
				}
			});

	        //$(".mobile-toggle-panel").not(":eq("+idx+")").slideUp().delay(250).addClass("toggle-hide");
	        $(".mobile-toggle-panel").eq(idx).slideToggle('500').delay(250).removeClass("toggle-hide");
	
	
	
	        // if (arrowTarget.hasClass('fa-angle-right')) {
	        //     arrowTarget.removeClass("fa-angle-right").addClass("fa-angle-down");
	        // }
	        // else {
	        //     arrowTarget.removeClass("fa-angle-down").addClass("fa-angle-up");
	        // }
        
    	}
    });




});