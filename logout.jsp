<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page session="true" %>
<%
    HttpSession ses = request.getSession(false);
    if (session != null) {
        session.invalidate(); // Invalider la session
    }
    response.sendRedirect("index.html"); // Rediriger vers la page de login
%>
