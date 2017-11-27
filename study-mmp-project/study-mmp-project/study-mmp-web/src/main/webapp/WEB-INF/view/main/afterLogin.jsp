<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<!DOCTYPE html>
<html lang="ko">
<head>
</head>
<meta charset="utf-8">
<title>센터</title>

<body>
<script>

    setTimeout(function(){

        var openWindow = parent.window.opener;
        <c:if test="${bizState ne 'N'}">
        var nextURL = '${nextUrl}';
        if(nextURL && nextURL !== ''){
            openWindow.location.href = nextURL;
        }else{
            openWindow.location.reload();
        }
        window.close();
        </c:if>
        $.when(openWindow.location.reload())
            .then( function () {
                location.href="$=" +
                    encodeURIComponent("${url}/afterLogin.do");
            });

    }, 100);
</script>

</body>
</html>