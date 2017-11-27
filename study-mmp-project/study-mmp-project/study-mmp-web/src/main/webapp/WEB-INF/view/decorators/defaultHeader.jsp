<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

	<!-- header -->
    <header id="header" class="" style="z-index:9999">
        <div class="notice_lst" id="headerNotice">
        </div>
    </header>
<script>

    function login(e, uri) {
        e.preventDefault ? e.preventDefault() : e.returnValue = false;
        var left = screen.width/2 - 630/2;
        var top = screen.height/2 - 560/2;
        var lp = window.open(');
    }

    function changePassword(e, uri) {
        e.preventDefault ? e.preventDefault() : e.returnValue = false;
        var left = screen.width/2 - 630/2;
        var top = screen.height/2 - 560/2;

        var lp = window.open(');
    }

    function join(e) {
        e.preventDefault ? e.preventDefault() : e.returnValue = false;
        var left = screen.width/2 - 630/2;
        var top = screen.height/2 - 560/2;
        var lp = window.open(');
        lp.focus();
    }
    
    function logout() {
        location.href= " ;
    }

   

    $(document).ready(function () {

        $('.nav_link').on('click',function(){
            $('.nav_link').parent().removeClass('is_selected');
            $(this).parent().toggleClass('is_selected');
        });

        $('.btn_setting').on('click',function(){
            $(this).toggleClass('is_selected');
            $('.setting_box').toggleClass('is_open');
        });

        $('.store_link').click(function() {
            location.href = '/changeCompany.do?companyCd=' + $(this).siblings('span').text() + '&companyNm=' + $(this).text();
        });


        $.each($('a'), function (i, val) {
            if($(val).hasClass('nav_sub_link')) {
                if(window.location.pathname.indexOf($(val).attr('href').replace('.do', '')) != -1) {
                    $(val).parents('.nav_item').toggleClass('is_selected');
                    $(val).parent().toggleClass('is_selected');
                }
            }
        });

        COMMON.dateFormat();
        <c:if test="${accessTypeCd == 'READ'}">
            $('.accessMod').hide();
        </c:if>

    });
</script>