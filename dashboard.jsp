<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.ThreadDAO" %>
<%@ page import="dto.Thread" %>
<%@ page import="dto.User" %>
<%@ page import="java.sql.SQLException" %>
<%
    // Vérifier si l'utilisateur est connecté
    HttpSession sessionObj = request.getSession();
    User user = (User) sessionObj.getAttribute("user");

    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Récupérer les threads auxquels l'utilisateur est abonné
    List<Thread> subscribedThreads = null;
    try {
        ThreadDAO threadDAO = new ThreadDAO();
        subscribedThreads = threadDAO.getSubscribedThreads(user.getId());
    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération des threads abonnés: " + e.getMessage());
    }
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; text-align: center; }
        form, .thread-list { margin: 20px auto; width: 50%; }
        input, button { margin-top: 10px; padding: 10px; }
        ul { list-style-type: none; padding: 0; }
        li { margin: 10px 0; }
    </style>
</head>
<body>

<h2>Bienvenue, <%= user.getName() %> 👋</h2>

<!-- Formulaire de création de thread -->
<h3>Créer un nouveau fil de discussion</h3>
<form action="CreateThreadServlet" method="post">
    <input type="text" name="title" placeholder="Titre du thread" required>
    <button type="submit">Créer</button>
</form>

<!-- Bouton de déconnexion -->
<form action="LogoutServlet" method="post">
    <button type="submit" style="background-color: red; color: white;">Déconnexion</button>
</form>

<!-- Liste des threads abonnés -->
<h3>Vos fils de discussion</h3>
<div class="thread-list">
    <ul>
        <% if (subscribedThreads != null && !subscribedThreads.isEmpty()) {
            for (Thread thread : subscribedThreads) { %>
        <li>
            <a href="thread.jsp?threadId=<%= thread.getId() %>">
                <%= thread.getTitle() %>
            </a>
        </li>
        <% }
        } else { %>
        <p>Vous n'êtes abonné à aucun fil de discussion.</p>
        <% } %>
    </ul>
</div>

</body>
</html>
