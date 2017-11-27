$(function(){
    'use strict';

    var scrollEl = [];
    
    scrollEl.push($('.scroll_wrap'));
    scrollEl.push($('.content_secondary'));
    scrollEl.push($('.tbl_scroll_wrap'));
    

     $('.select').click(function(){
         if($(this).hasClass('is_open')){
             $(this).removeClass('is_open');
         }else{
             $('.select').removeClass('is_open');
             $(this).addClass('is_open');
         }
     });
     $('.ly_pop .ly_btn_close').click(function(){
         $(this).closest('.ly_pop_wrap').hide();
         $('.dimmed').hide();
     });
     $('.btn_fold').on('click',function(){
         $('body').toggleClass('is_fold');
     })

     
     if ($('.content_secondary').length == 1){
        for (var i=0; scrollEl.length > i; i++){
            scrollEl[i].addClass('scrollbar-chrome');            
        }
        $('.scrollbar-chrome').scrollbar();
     }

});
