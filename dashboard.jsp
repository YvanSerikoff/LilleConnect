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
        response.sendRedirect("index.html");
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
    <title>Dashboard - Lille Connect</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="d-flex flex-column align-items-center bg-light py-5">
<div class="container text-center">
    <h1 class="mb-4">Lille Connect</h1>
    <h2>Bienvenue, <%= user.getName() %> 👋</h2>

    <!-- Formulaire de création de thread -->
    <div class="card p-4 shadow-sm mt-4" style="max-width: 500px;">
        <h3>Créer un nouveau fil de discussion</h3>
        <form action="CreateThreadServlet" method="post">
            <div class="mb-3">
                <input type="text" name="title" class="form-control" placeholder="Titre du thread" required>
            </div>
            <button type="submit" class="btn btn-primary w-100">Créer</button>
        </form>
    </div>

    <!-- Bouton de déconnexion -->
    <form action="LogoutServlet" method="post" class="mt-3">
        <button type="submit" class="btn btn-danger">Déconnexion</button>
    </form>

    <!-- Liste des threads abonnés -->
    <div class="card p-4 shadow-sm mt-4" style="max-width: 500px;">
        <h3>Vos fils de discussion</h3>
        <ul class="list-group">
            <% if (subscribedThreads != null && !subscribedThreads.isEmpty()) {
                for (Thread thread : subscribedThreads) { %>
            <li class="list-group-item">
                <a href="thread.jsp?threadId=<%= thread.getId() %>" class="text-decoration-none">
                    <%= thread.getTitle() %>
                </a>
            </li>
            <% }
            } else { %>
            <p class="text-muted">Vous n'êtes abonné à aucun fil de discussion.</p>
            <% } %>
        </ul>
    </div>
</div>

</body>
</html>
