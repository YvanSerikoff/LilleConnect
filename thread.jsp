<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.sql.*, java.util.*" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="dto.User" %>
<%@ page import="org.apache.tomcat.jakartaee.commons.lang3.StringEscapeUtils"%>
<%@ page import="dao.*" %>

<%
    String threadIdStr = request.getParameter("threadId");
    if (threadIdStr == null) {
        response.sendRedirect("dashboard.jsp");
        return;
    }
    int threadId = Integer.parseInt(threadIdStr);

    HttpSession userSession = request.getSession();
    User user = (User) userSession.getAttribute("user");

    if (user == null) {
        response.sendRedirect("/LilleConnect/index.html");
        return;
    }

    int userId = user.getId();
    int likeCount;
    boolean isAdmin = false;
    String threadTitle = "";

    LikeDAO likeDAO = new LikeDAO();
    SubscriberDAO subscriberDAO = new SubscriberDAO();
    ThreadDAO threadDAO = new ThreadDAO();
    PostDAO postDAO = new PostDAO();

    List<String[]> messages = new ArrayList<>();
    List<String[]> subscribers = new ArrayList<>();
    List<String[]> nonSubscribers = new ArrayList<>();

    try {
        threadTitle = threadDAO.getThreadTitle(threadId);
        isAdmin = threadDAO.isAdministrator(userId, threadId);
        messages = postDAO.getPostsByThreadId(threadId);
        subscribers = subscriberDAO.getSubscribers(threadId);
        nonSubscribers = subscriberDAO.getNonSubscribers(threadId);
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title><%= threadTitle %> - Discussion</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        body { font-family: Arial, sans-serif; }
        .container { max-width: 800px; margin: 20px auto; }

        /* Messages */
        .message-container { margin-bottom: 15px; display: flex; }
        .message { padding: 10px; border-radius: 10px; max-width: 70%; word-wrap: break-word; }
        .message-left { background-color: #f1f1f1; align-self: flex-start; }
        .message-right { background-color: #007bff; color: white; align-self: flex-end; }

        /* Mobile Optimisation */
        @media (max-width: 768px) {
            .container { width: 95%; } /* Évite le dézoom */
            .message { max-width: 90%; } /* Messages plus larges */
            .btn { width: 100%; } /* Boutons plus grands */
            textarea { font-size: 16px; } /* Texte plus lisible */
        }
    </style>
</head>
<body>

<header class="bg-primary text-white py-4 text-center">
    <div class="container">
        <h1 class="display-4 mb-0">Lille Connect</h1>
        <button class="btn btn-light mt-2" onclick="window.location.href='dashboard.jsp'">Retour au Dashboard</button>
    </div>
</header>

<div class="container">
    <h2 class="mt-4 text-center"><%= threadTitle %></h2>

    <div class="mt-4">
        <% for (String[] message : messages) {
            boolean isAuthor = Integer.parseInt(message[2]) == userId;
        %>
        <div class="message-container <%= isAuthor ? "justify-content-end" : "justify-content-start" %>">
            <div class="message <%= isAuthor ? "message-right" : "message-left" %> p-3 rounded">
                <div class="username"><%= message[0] %></div>
                <p><%= message[1] %></p>
                <% likeCount = likeDAO.getLikeCount(Integer.parseInt(message[3])); %>
                <div class="d-flex align-items-center">
                    <form action="like" method="post" class="me-2">
                        <input type="hidden" name="messageId" value="<%= message[3] %>">
                        <input type="hidden" name="threadId" value="<%= threadId %>">
                        <button type="submit" class="btn btn-danger">❤ <%= likeCount %></button>
                    </form>

                    <% if (isAuthor) { %>
                    <form action="deletePost" method="post">
                        <input type="hidden" name="postId" value="<%= message[3] %>">
                        <input type="hidden" name="threadId" value="<%= threadId %>">
                        <button type="submit" class="btn btn-warning btn-sm">🗑 Supprimer</button>
                    </form>
                    <% } %>
                </div>
            </div>
        </div>
        <% } %>
    </div>

    <h3 class="mt-4 text-center">Publier un Message</h3>
    <form action="postMessage" method="post">
        <input type="hidden" name="threadId" value="<%= threadId %>">
        <textarea name="contenu" rows="3" class="form-control" required placeholder="Écrivez votre message ici"></textarea>
        <button type="submit" class="btn btn-primary mt-2">Envoyer</button>
    </form>

    <% if (isAdmin) { %>
    <div class="admin-section mt-4">
        <h3>Gestion des abonnés</h3>
        <h4>Liste des abonnés :</h4>
        <ul class="list-group">
            <% for (String[] sub : subscribers) { %>
            <li class="list-group-item d-flex justify-content-between align-items-center">
                <%= StringEscapeUtils.escapeHtml4(sub[1]) %>
                <% if (Integer.parseInt(sub[0]) != userId) { %>
                <form action="unsubscribe" method="post">
                    <input type="hidden" name="threadId" value="<%= threadId %>">
                    <input type="hidden" name="userId" value="<%= sub[0] %>">
                    <button type="submit" class="btn btn-danger btn-sm">Désinscrire</button>
                </form>
                <% } %>
            </li>
            <% } %>
        </ul>

        <h4 class="mt-3">Inviter un utilisateur :</h4>
        <form action="invite" method="post">
            <input type="hidden" name="threadId" value="<%= threadId %>">
            <select name="userId" class="form-select mb-2">
                <% for (String[] userEntry : nonSubscribers) { %>
                <option value="<%= userEntry[0] %>"><%= userEntry[1] %></option>
                <% } %>
            </select>
            <button type="submit" class="btn btn-success">Inviter</button>
        </form>
    </div>
    <% } %>

    <% if (isAdmin) { %>
    <div class="mt-5">
        <form action="deleteThread" method="post">
            <input type="hidden" name="threadId" value="<%= threadId %>">
            <button type="submit" class="btn btn-danger mt-3 w-100">🗑 Supprimer le Thread</button>
        </form>
    </div>
    <% } %>
</div>

</body>
</html>
