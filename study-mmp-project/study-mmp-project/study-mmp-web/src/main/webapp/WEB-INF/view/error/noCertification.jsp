<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- [D] CONTENTS -->
<main id="content" class="content_wrap">
    <section class="main">

    </section>
</main>

<script>
    <c:if test="${bizState != 'N'}">
    location.href = '/';

    </c:if>

</script>
<!-- //[D] CONTENTS -->
