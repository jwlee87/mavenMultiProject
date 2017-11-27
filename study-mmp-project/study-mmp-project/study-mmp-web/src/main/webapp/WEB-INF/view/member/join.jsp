<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<title>회원가입</title>
</head>
<body style="height:500px;">
<form id="joinMember" action="/member/memberList/joinAjax.json">
	<table>
		<tr>
			<th>회사코드</th>
			<td><input type="text" name="companyCd" value="TEST"></td>
		</tr>
		<tr>
			<th>회원명</th>
			<td><input type="text" name="memberNm" value="이현세"></td>
		</tr>
		<tr>
			<th>생년월일</th>
			<td><input type="text" name="birthday" value="19990101"></td>
		</tr>
		<tr>
			<th>성별</th>
			<td><label><input type="radio" name="gender" value="M" checked>남자</label>
					<label><input type="radio" name="gender" value="F">여자</label>
			</td>
		</tr>
	</table>
	
	<a id="submitBtn">등록</a>
</form>
	
<script>
$(document).ready(function() {
	$('#submitBtn').click(joinMember);
});

function joinMember(){
	
	$.ajax({
		headers : {
			"Content-Type" : "application/x-www-form-urlencoded;charset=UTF-8;"
		},
		url : '/member/..../joinAjax.do',
		type : "POST",
		data : $('#joinMember').serialize(),
		success : function(result, status) {
			
		}
	});
	
}
</script>
</body>
</html>