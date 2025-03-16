<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.ThreadDAO" %>
<%@ page import="dto.Thread" %>
<%@ page import="dto.User" %>
<%@ page import="java.sql.SQLException" %>

<%
    HttpSession sessionObj = request.getSession();
    User user = (User) sessionObj.getAttribute("user");

    if (user == null) {
        response.sendRedirect("/LilleConnect/index.html");
        return;
    }

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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        body { font-family: Arial, sans-serif; background-color: #f8f9fa; }
        .main-container { max-width: 900px; margin: auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); }
        .sidebar { background: #f8f9fa; padding: 15px; border-radius: 10px; }
        .logout-btn { background: red; color: white; border: none; padding: 5px 10px; }
        .logout-btn:hover { background: darkred; }
    </style>
</head>
<body>

<header class="bg-primary text-white py-4 text-center">
    <div class="container">
        <h1 class="display-4 mb-0">Lille Connect</h1>
        <div class="d-flex justify-content-center align-items-center mt-2">
            <span class="me-3">Bonjour <%= user.getName() %></span>
            <form action="logout" method="post">
                <button type="submit" class="logout-btn btn btn-sm">Déconnexion</button>
            </form>
        </div>
    </div>
</header>

<div class="container main-container mt-4">
    <div class="row">
        <!-- Barre latérale -->
        <nav class="col-lg-4 col-md-5 sidebar mx-auto mb-4">
            <h3 class="h5 text-center">Vos threads</h3>
            <ul class="list-group">
                <% if (subscribedThreads != null && !subscribedThreads.isEmpty()) {
                    for (Thread thread : subscribedThreads) { %>
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    <a href="thread.jsp?threadId=<%= thread.getId() %>" class="text-decoration-none flex-grow-1">
                        <%= thread.getTitle() %>
                    </a>
                </li>
                <% } } else { %>
                <li class="list-group-item text-center">Vous n'êtes abonné à aucun thread.</li>
                <% } %>
            </ul>
        </nav>

        <!-- Contenu principal -->
        <main class="col-lg-8 col-md-7 mx-auto">
            <h3 class="h5 text-center">Créer un nouveau thread</h3>
            <form action="createThread" method="post" class="mb-3">
                <div class="input-group">
                    <input type="text" name="title" class="form-control" placeholder="Titre du thread" required>
                    <button type="submit" class="btn btn-primary">Créer</button>
                </div>
            </form>
        </main>
    </div>
</div>

</body>
</html>
