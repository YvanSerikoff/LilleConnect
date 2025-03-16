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
        .message-container { margin-bottom: 15px; }
        .message { padding: 10px; border-radius: 10px; max-width: 70%; word-wrap: break-word; }
        .message-left { background-color: #f1f1f1; align-self: flex-start; }
        .message-right { background-color: #007bff; color: white; align-self: flex-end; }
        .username { font-weight: bold; margin-bottom: 5px; }
        .admin-section { background: #f8f9fa; padding: 10px; margin-top: 20px; border-radius: 10px; }
    </style>
</head>
<body>

<header class="bg-primary text-white text-center py-3">
    <h1>Lille Connect</h1>
    <button class="btn btn-light" onclick="window.location.href='dashboard.jsp'">Retour au Dashboard</button>
</header>

<div class="container">
    <h2 class="mt-4"><%= threadTitle %></h2>

    <div class="mt-4">
        <% for (String[] message : messages) {
            boolean isAuthor = Integer.parseInt(message[2]) == userId;
        %>
        <div class="d-flex <%= isAuthor ? "justify-content-end" : "justify-content-start" %> message-container">
            <div class="message <%= isAuthor ? "message-right" : "message-left" %>">
                <div class="username"><%= message[0] %></div>
                <p><%= message[1] %></p>
                <% likeCount = likeDAO.getLikeCount(Integer.parseInt(message[3])); %>
                <form action="like" method="post" style="display:inline;">
                    <input type="hidden" name="messageId" value="<%= message[3] %>">
                    <input type="hidden" name="threadId" value="<%= threadId %>">
                    <button type="submit" class="btn btn-danger"> ❤ <%= likeCount%></button>
                </form>

                <% if (isAuthor) { %>
                <form action="deletePost" method="post" style="display:inline;">
                    <input type="hidden" name="postId" value="<%= message[3] %>">
                    <input type="hidden" name="threadId" value="<%= threadId %>">
                    <button type="submit" class="btn btn-warning btn-sm">🗑 Supprimer</button>
                </form>
                <% } %>
            </div>
        </div>
        <% } %>
    </div>

    <h3 class="mt-4">Publier un Message</h3>
    <form action="postMessage" method="post">
        <input type="hidden" name="threadId" value="<%= threadId %>">
        <textarea name="contenu" rows="3" class="form-control" required placeholder="Écrivez votre message ici"></textarea>
        <button type="submit" class="btn btn-primary mt-2">Envoyer</button>
    </form>

    <% if (isAdmin) { %>
    <div class="admin-section">
        <h3 class="mt-3">Gestion des abonnés</h3>
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
    <div class="mt-5">
        <% if (isAdmin) { %>
        <form action="deleteThread" method="post">
            <input type="hidden" name="threadId" value="<%= threadId %>">
            <button type="submit" class="btn btn-danger mt-3">🗑 Supprimer le Thread</button>
        </form>
        <% } %>
    </div>
</div>


</body>
</html>
