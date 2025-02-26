<%@ page session="true" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="dto.User" %>
<%
    HttpSession ses = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
    } else {
        User usr = (User) session.getAttribute("user");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
</head>
<body>
<h2>Welcome, <%= usr.getName() %>!</h2>
<p>This is your dashboard.</p>
<a href="logout.jsp">Logout</a>
</body>
</html>

<% } %>
