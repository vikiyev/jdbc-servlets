<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Cout Demo</title>
</head>
<body>
	<c:out value="${10 + 9}"/>
	<c:set var="testScore" value="${80}" scope="session"/>
	<c:out value="${testScore}"/>
	
	<c:if test="${testScore >=80 }">
		<p>Your score is awesome</p>
		<c:out value="${testScore }"/>
	</c:if>
	
	<c:choose>
		<c:when test="${testScore>=80}">
			A Grade
		</c:when>
		<c:when test="${testScore>=60 && testScore <=80}">
			B Grade
		</c:when>
		<c:otherwise>
			C Grade
		</c:otherwise>
	</c:choose>
	
	<c:forEach var="i" begin="1" end="3">
		<c:out value="${i}"/>
	</c:forEach>
	
	<%
	List<String> studentNames = new ArrayList<>();
	studentNames.add("Doge");
	studentNames.add("Cate");
	studentNames.add("Fishe");
	
	request.setAttribute("studentNames", studentNames);
	%>
	
	<c:forEach var="studentName" items="${studentNames}">
		<c:out value="${studentName}"/>
	</c:forEach>
	
	<c:remove var="testScore"/>
	
	<c:set var="accountBalance" value="123.456"/>
	<fmt:parseNumber var="i" type="number" value="${accountBalance}"/>
	<p>Balance is: <c:out value="${i}" /></p>

	
	<c:set var="accountBalance" value="7777.4567"/>
	<fmt:formatNumber value="${accountBalance}" type="currency"/>
	<fmt:formatNumber value="${accountBalance}" type="number" maxFractionDigits="3" maxIntegerDigits="2"/>
	<fmt:formatNumber value="${accountBalance}" type="number" pattern="####.##$"/>	
	<fmt:formatNumber value="${accountBalance}" type="percent"/>	
	
	<c:set var="myDate" value="03-21-2023" />
	<fmt:parseDate var="parsedDate" value="${myDate}" pattern="MM-dd-yyyy"/> 
	<c:out value="${parsedDate}" />
</body>
</html>









