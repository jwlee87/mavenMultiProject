<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta http-equiv="Cache-Control" content="No-Cache">
    <meta http-equiv="Pragma" content="No-Cache">
    <meta http-equiv="Expires" content="0">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,height=device-height">
    <meta name="format-detection" content="telephone=no">
    <meta http-equiv="imagetoolbar" content="no">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">


    <link rel="stylesheet" type="text/css" href="/share/css/calendar.css?${scriptCachedate}" />
    <link rel="stylesheet" type="text/css" href="/share/css/jquery.scrollbar.css?${scriptCachedate}" />


    <script src="/share/js/jquery/jquery-1.12.3.min.js?${scriptCachedate}"></script>
    <script src="/share/js/jquery/jquery.scrollbar.min.js?${scriptCachedate}"></script>
    <script src="/share/js/jquery/jquery.form.js?${scriptCachedate}"></script>
    <script src="/share/js/lib/underscore.js?${scriptCachedate}"></script>
    <script src="/share/js/lib/backbone.js?${scriptCachedate}"></script>
    <script src="/share/js/lib/json2.js?${scriptCachedate}"></script>
    <script src="/share/js/lib/jscolor.js?${scriptCachedate}"></script>

    <script src="/share/js/common.js?${scriptCachedate}"></script>
    <script src="/share/js/utils.js?${scriptCachedate}"></script>
    <script src="/share/js/init-datepicker.js"></script>
    <script src="/share/js/footer.js?${scriptCachedate}"></script>

    <style>

        .config_btn {
            height : 21px;
            line-height: normal;
        }

    </style>

<decorator:head/>
</head>

<body>
<div class="wrap">

    <header id="header" class="">
        <page:applyDecorator name="defaultHeader"/>
    </header>

    <c:if test="${ not empty maskingEmail and bizState != null and bizState != 'N' and not empty companyCd}">
        <nav class="">
            <page:applyDecorator name="defaultNav"/>
        </nav>

        <main id="content" class="content_wrap">
            <section class="main">
                <div class="main_content">
                    <decorator:body/>
                </div>
            </section>
        </main>
    </c:if>


    <c:if test="${ empty maskingEmail or bizState == null or bizState == 'N' or empty companyCd}">
                <decorator:body/>
    </c:if>


    <footer class="">
        <page:applyDecorator name="defaultFooter"/>
    </footer>
</div>

<script>

</script>

</body>
</html>