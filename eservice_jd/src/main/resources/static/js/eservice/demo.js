

$("[data-toggle='popover']").popover();


// back to top
if (is_touch_device) {
	console.log(is_touch_device)
    scrollTrigger = 200;
} else {
    scrollTrigger = 1300;
}
makeButton();
backToTop();

$('#backToTopBtn').on('click', function(e) {
    e.preventDefault();
    $('html,body').animate({
        scrollTop: 0
    }, 700);
});

$(window).on('scroll', function() {
    backToTop();
});

function backToTop() {
    var scrollTop = $(window).scrollTop();
    if (scrollTop > scrollTrigger) {
        $('#backToTopBtn').fadeIn('fast');
    } else {
        $('#backToTopBtn').hide();
    }
}

function makeButton() {
    $('body').append('<div id="backToTopBtn">Top</div>');
}
function is_touch_device() {
    return 'ontouchstart' in window // works on most browsers 
     || 'onmsgesturechange' in window; // works on ie10
}