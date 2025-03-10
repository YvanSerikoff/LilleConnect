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
        body { font-family: Arial, sans-serif; }
        .sidebar { background: #f8f9fa; padding: 20px; overflow-y: auto; }
        .content { padding: 20px; }
        .header { background: #007bff; color: white; padding: 10px; text-align: center; }
        .logout-btn { background: red; color: white; border: none; padding: 5px 10px; }
        .logout-btn:hover { background: darkred; }
    </style>
</head>
<body>

<div class="header">
    <h1 class="h4">Lille Connect</h1>
    <form action="logout" method="post" class="d-inline">
        <button type="submit" class="logout-btn btn btn-sm">Déconnexion</button>
    </form>
</div>

<div class="container-fluid">
    <div class="row">
        <nav class="col-md-3 col-12 sidebar">
            <h3 class="h5 text-center text-md-start">Vos fils de discussion</h3>
            <ul class="list-group">
                <% if (subscribedThreads != null && !subscribedThreads.isEmpty()) {
                    for (Thread thread : subscribedThreads) { %>
                <li class="list-group-item text-center text-md-start">
                    <a href="thread.jsp?threadId=<%= thread.getId() %>" class="text-decoration-none"><%= thread.getTitle() %></a>
                </li>
                <% } } else { %>
                <p class="text-center text-md-start">Vous n'êtes abonné à aucun fil de discussion.</p>
                <% } %>
            </ul>
        </nav>

        <main class="col-md-9 col-12 content">
            <h3 class="h5">Créer un nouveau fil de discussion</h3>
            <form action="createThread" method="post" class="mb-3">
                <input type="text" name="title" class="form-control mb-2" placeholder="Titre du thread" required>
                <button type="submit" class="btn btn-primary w-100">Créer</button>
            </form>
        </main>
    </div>
</div>
</body>
</html>