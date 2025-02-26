<%@ page session="true" %>
<%
    HttpSession ses = request.getSession(false);
    if (session != null) {
        session.invalidate(); // Invalider la session
    }
    response.sendRedirect("login.jsp"); // Rediriger vers la page de login
%>
