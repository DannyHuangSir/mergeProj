$(document).ready(function() {

    var accessHeight = function() {
        // change window height when sidebar-offcanvas show
        var sHeight = $('.sidebar-offcanvas').height();
        var cHeight = $('#list2').height() + 100;

        // console.log(cHeight);

        var height = ((sHeight > cHeight) ? sHeight : cHeight);

        if ($('.row-offcanvas').hasClass('active')) {
            $('section .container').height(height);
        }
        else {
            $('section .container').height('auto');
        }
    };

    $('[data-toggle="offcanvas"]').click(function() {
        $('.row-offcanvas').toggleClass('active');
        accessHeight();
    });

    $('[data-toggle="true"]').click(function() {
        accessHeight();
    });

});

/*

$(function(){
  $('.toggle-menu').click(function(e){
    e.preventDefault();
    $('.sidebar').toggleClass('toggled');
  });
});

*/