<%--
  Created by IntelliJ IDEA.
  User: vladislavarsenev
  Date: 05.10.2020
  Time: 12:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<table>
    <c:forEach items="${mealList}" var="meal">
<%--        <tr>--%>
<%--            <td></td>--%>
<%--            <td></td>--%>
<%--            <td></td>--%>
<%--        </tr>--%>
        <p>${meal}</p>
    </c:forEach>
</table>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Users</h2>
</body>
</html>
