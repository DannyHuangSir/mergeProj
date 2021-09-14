/*!
 * HTML development
 */

/*! Customized JS */

/*! Chart

    var doughnutData = [
        {
          value: 19209,
          color:"#b9e1ec",
          highlight: "#c8f2fe",
          label: "保障型商品"
        },
        {
          value: 14491,
          color: "#00addd",
          highlight: "#00baed",
          label: "投資型保單"
        },

      ];

      window.onload = function(){
        var ctx = document.getElementById("chart-area").getContext("2d");
        window.myDoughnut = new Chart(ctx).Doughnut(doughnutData, {responsive : true});
      };

*/


//clickable
jQuery(document).ready(function($) {
    $(".clickable-row").click(function() {
        window.document.location = $(this).data("href");
    });

    /*
      Custom checkbox and radio button - Jun 18, 2013
      (c) 2013 @ElmahdiMahmoud 
      license: http://www.opensource.org/licenses/mit-license.php
    */
    // $('input[name="radio-btn"]').wrap('<div class="radio-btn"><i></i></div>');


    /* Don't Paste Start */
    // add by moshi 2015/7/9
    $('.radio-btn').on('click', function(e) {
    	e.stopPropagation();
    	
        if($(e.target).hasClass('targetPolicy')){
            if($(e.target).prop('disabled')){
                window['dataCheck'] ? window['dataCheck'].call(this, e.target) : null ;
            }
        }
        var radio = $(this).find('input').attr('name'),
            radioID = $(this).find('input').attr('id'),
            checkedRadio = $('input[name="' + radio + '"]:checked')[0],
            otherRadio = $('input[name="' + radio + '"]'),
            otherRadioParent = otherRadio.closest('.radio-btn');
            $this = $(this);
            
        if ( $(this).find('input').prop('disabled') == true ) {
            $(this).find('input').prop('checked', false);
            // return false;
        }
        else {
            otherRadioParent.removeClass('checkedRadio');
            otherRadio.prop('checked', false);
            $this.addClass('checkedRadio');
             /***** Add execution @Linus_09/24 *****/
            $(this).find(':radio').prop("checked", true);
        }
        
        // console.log(radioID);
    });
    
    // restore radio staus
    $('.radio-btn').find('input:radio').each(function (i, obj) {
        if ($(obj).prop('checked')) {
            $(obj).parents('.radio-btn').addClass('checkedRadio'); 
        }
    });
    
    /*
    if($('.radio-btn').length != 0){
        $('.radio-btn')[0].click();
    }
    */


    // hide by moshi 2015/7/9
    // $('.radio-btn').on('click', function() {
    //     var radio = $(this).find('input').attr('name'),
    //         checkedRadio = $('input[name="' + radio + '"]:checked')[0],
    //         _this = $(this),
    //         block = _this.parent().parent();

    //     console.log(radio);


    // if (!checkedRadio == false) {
    //     checkedRadio.checked = false;
    //     $(".checkedRadio").removeClass("checkedRadio");

    // }

    // if ($(this).find('input').attr('checked') == 'checked') {
    //     _this.find("input")[0].checked = true;
    //     _this.addClass("checkedRadio");
    // }
        

    //     // add by moshi 2015/7/9
    //     // _this.addClass("checkedRadio");
    // });

    // $('input[name="check-box"]').wrap('<div class="check-box"><i></i></div>');
    
    $.fn.toggleCheckbox = function() {
        this.prop('checked', !this.prop('checked'));
    };

    /* Don't Paste Start */
    $('.check-box').on('click', function(e) {
    	e.stopPropagation();
        /*****  Add condition @Linus_09/23 *****/
        if(!$(this).find(':checkbox').prop("disabled")) {
            if ($(this).hasClass('checkedBox')) {
                $(this).find('input:checkbox').prop('checked', false);
                $(this).delay(400).removeClass('checkedBox');
                $(this).find('i').removeClass('checkedBox-i');
            }
            else {
                $(this).find('input:checkbox').prop('checked', true);
                $(this).delay(400).addClass('checkedBox');
                $(this).find('i').addClass('checkedBox-i')
            }
        }
    });
    /* Don't Paste End */
    
    // restore checkbox staus
    $('.check-box').find('input:checkbox').each(function (i, obj) {
        if ($(obj).prop('checked')) {
            $(obj).closest('i').addClass('checkedBox-i');
            $(obj).parents('.check-box').addClass('checkedBox'); 
        }
    });

    // $('.alert1').hide();
    
/***** Comment out whole function and move to myNotice.jsp line:233    @Linus_09/24 *****/
/*    $(document).on('click', '#alert_mobile', function() {
        $('.alert1').slideToggle(10,
            function() {
                if ($(this).css('display') == 'block') {
                    $('#alert_mobile').html('<div class="clearfix"><div style="float: left;"><i class="foo-attention"></i> 我的提醒</div><div style="float: right;"> x </div></div>');
                } else {
                    str = '<i class="foo-attention"></i> 我的提醒 <i class="badge a1">8</i>';
                    $('#alert_mobile').html(str).css('text-align', 'center');
                }
            }
        );
    });*/



    // add new fund  fund3.html
/***** Comment out whole function and move to fundSwitchTransferIn.jsp line:169 ~ 189 @Linus_09/24 *****/
//    $('#add-check-list').click(function() {
//        template = '<div class="check-list2 newfund">' + $('#investment_template').html() + '</div>';
//        $('#fundContent').append(template);
//
//        if ($(".newfund").length > 1) {
//            $('.plus-list').show();
//        }
//        if ($(".newfund").length > 10) {
//            $(this).parent().hide();
//        }
//    });
//
//    $(document).on("click", ".deleteFundItem", function() {
//        $(this).parent().parent().remove();
//        if ($(".newfund").length == 1) {
//            $('.plus-list').hide();
//        }
//        if ($(".newfund").length < 11) {
//            $('#add-check-list').parent().show();
//        }
//
//    });


    $('.modal-header .close').on('click touchstart', function() {
        // alert('model close click');
        $('.modal').modal('hide');
    });


    //show my notification's search panel in tablet and mobile
    if ($(window).width() < 1000) {
        $('#popoverSearchBar').removeClass('collapse');
    }



    /* Don't Paste Start */
    //pop print
    $('#hospital_other').click(function() {
        $('#hospital_other_form').removeClass('displayNone');
    });
    $('#hospital_self').click(function() {
        $('#hospital_other_form').addClass('displayNone');
        $('#hospital_other_form').find("input").val("");
    });
    $('#hospital_help').click(function() {
        $('#hospital_other_form').addClass('displayNone');
        $('#hospital_other_form').find("input").val("");
    });
    $('#accident_other').click(function() {
        $('#accident_other_form').removeClass('displayNone');
    });
    $('#accident_office').click(function() {
        $('#accident_other_form').addClass('displayNone');
        $('#accident_other_form').find("input").val("");
    });
    $('#accident_home').click(function() {
        $('#accident_other_form').addClass('displayNone');
        $('#accident_other_form').find("input").val("");
    });

    $(function() {
        $('.changeDiv').hide();
        $('#cause_selector').change(function() {
            $('.changeDiv').hide();
            $('.changeDiv').find("input").val("");
            $('#' + $(this).val()).show();
        });
    });
    /* Don't Paste End */


    // back to top
    if (is_touch_device) {
        scrollTrigger = 400;
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


    // update StickyFooter
    var updateStickyFooter = function() {
        var totalHeight = $(document).find('#top1').height() + $(document).find('header').height() + $(document).find('.fullContent').height();
        totalHeight = $(window).height() - totalHeight;

        console.log(totalHeight);

        if(!$.browser.mobile){

            if ($('#notification').length !== 0) { // for 有我的通知的版面
                // console.log('notification');
                if (totalHeight < 240) {
                    $('footer').removeClass('stickyFooter');
                }
                else {
                    $('footer').addClass('stickyFooter');
                }
            }
            else {
                // console.log('no notification');
                if (totalHeight < 130) { // for 沒有我的通知的版面 例如註冊頁
                    $('footer').removeClass('stickyFooter');
                }
                else {
                    $('footer').addClass('stickyFooter');
                }
            }
        }
       
    };

    updateStickyFooter();

    $(window).on('resize', function() {
        updateStickyFooter();
    });

    $('.ui-tabs-nav a').on('click', function(){
        updateStickyFooter();
    });
    
    




    // dashboard 顯示全部
    var showAllTrigger = function() {
        var trigger = $('.showAllTrigger');

        trigger.on('click', function(){

            // 投資型保單
            var target = $(this).parents('.mobile-toggle-panel').find('table');

            if (!$(this).hasClass('showAll')) {
                // console.log('not show all');
                $(this).removeClass('notShowAll').addClass('showAll');
                target.find('tr:gt(3)').addClass('hide');
            }
            else {
                // console.log('show all');
                $(this).removeClass('showAll').addClass('notShowAll');
                target.find('tr').removeClass('hide');
            }

        });
    };

    showAllTrigger();


});



//addon AJAX
// Create the tooltips only when document ready
// $(document).ready(function()
// {
//     // MAKE SURE YOUR SELECTOR MATCHES SOMETHING IN YOUR HTML!!!
//     $('a').each(function() {
//         $(this).qtip({
//            content: {
//                text: function(event, api) {
//                    $.ajax({
//                        url: api.elements.target.attr('href') // Use href attribute as URL
//                    })
//                    .then(function(content) {
//                        // Set the tooltip content upon successful retrieval
//                        api.set('content.text', content);
//                    }, function(xhr, status, error) {
//                        // Upon failure... set the tooltip content to error
//                        api.set('content.text', status + ': ' + error);
//                    });

//                    return 'Loading...'; // Set some initial text
//                }
//            },
//            position: {
//                viewport: $(window)
//            },
//            style: 'qtip-wiki'
//         });
//     });
// });

// //