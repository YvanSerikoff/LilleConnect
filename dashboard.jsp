<%@ page import="dto.User" %>
<%@ page import="dao.UserDAO" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <title>Dashboard</title>
</head>

<%
    User user = (User) session.getAttribute("user");
    String name = "";
    if (user == null) {
        name = "test";
    }else{
        name = user.getName();
    }
%>

<body>
<h1>Dashboard de <%=name%></h1>
<c:if test="${not empty errorMessage}">
    <p class="error" style="color: red">${errorMessage}</p>
</c:if>


<form action="CreateThreadServlet" method="post">
    <label for="title">Title:</label>
    <input type="text" id="title" name="title" required>
    <button type="submit">Create Thread</button>
</form>
</body>
</html>