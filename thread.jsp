<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.sql.*, java.util.*" %>
<%@ page import="dao.DS" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="dto.User" %>

<%
    // Récupération de l'ID du thread depuis l'URL
    String threadIdStr = request.getParameter("threadId");
    if (threadIdStr == null) {
        response.sendRedirect("dashboard.jsp");
        return;
    }
    int threadId = Integer.parseInt(threadIdStr);

    // Vérification de l'utilisateur connecté
    HttpSession userSession = request.getSession();
    User user = (User) userSession.getAttribute("user");

    if (user == null) {
        response.sendRedirect("index.html");
        return;
    }

    Integer userId = user.getId();
    boolean isSubscribed = false;
    boolean isAdmin = false;
    String threadTitle = "";
    List<String[]> messages = new ArrayList<>();
    List<String[]> subscribers = new ArrayList<>();
    List<String[]> nonSubscribers = new ArrayList<>();

    DS ds = new DS();

    try (Connection conn = ds.getConnection()) {
        // Vérifier si l'utilisateur est abonné
        PreparedStatement checkSub = conn.prepareStatement("SELECT COUNT(*) FROM subscriber WHERE usr_id = ? AND thread_id = ?");
        checkSub.setInt(1, userId);
        checkSub.setInt(2, threadId);
        ResultSet rsSub = checkSub.executeQuery();
        if (rsSub.next()) {
            isSubscribed = rsSub.getInt(1) > 0;
        }

        // Récupérer le titre et l'admin du thread
        PreparedStatement stmtThread = conn.prepareStatement("SELECT title, admin_id FROM thread WHERE id = ?");
        stmtThread.setInt(1, threadId);
        ResultSet rsThread = stmtThread.executeQuery();
        if (rsThread.next()) {
            threadTitle = rsThread.getString("title");
            int adminId = rsThread.getInt("admin_id");
            isAdmin = (adminId == userId); // Vérifie si l'utilisateur est l'admin
        } else {
            response.sendRedirect("dashboard.jsp");
            return;
        }

        // Récupérer les messages du thread
        PreparedStatement stmtMessages = conn.prepareStatement(
                "SELECT post.contenu, usr.name FROM post JOIN usr ON post.usr_id = usr.id WHERE thread_id = ? ORDER BY post.id ASC"
        );
        stmtMessages.setInt(1, threadId);
        ResultSet rsMessages = stmtMessages.executeQuery();
        while (rsMessages.next()) {
            messages.add(new String[]{rsMessages.getString("name"), rsMessages.getString("contenu")});
        }

        // Récupérer la liste des abonnés
        PreparedStatement stmtSubscribers = conn.prepareStatement(
                "SELECT usr.id, usr.name FROM subscriber JOIN usr ON subscriber.usr_id = usr.id WHERE subscriber.thread_id = ?"
        );
        stmtSubscribers.setInt(1, threadId);
        ResultSet rsSubscribers = stmtSubscribers.executeQuery();
        while (rsSubscribers.next()) {
            subscribers.add(new String[]{rsSubscribers.getString("id"), rsSubscribers.getString("name")});
        }

        // Récupérer la liste des utilisateurs non abonnés (pour les invitations)
        PreparedStatement stmtNonSubscribers = conn.prepareStatement(
                "SELECT usr.id, usr.name FROM usr WHERE usr.id NOT IN (SELECT usr_id FROM subscriber WHERE thread_id = ?) AND usr.id != ?"
        );
        stmtNonSubscribers.setInt(1, threadId);
        stmtNonSubscribers.setInt(2, userId); // Exclure l'admin de cette liste
        ResultSet rsNonSubscribers = stmtNonSubscribers.executeQuery();
        while (rsNonSubscribers.next()) {
            nonSubscribers.add(new String[]{rsNonSubscribers.getString("id"), rsNonSubscribers.getString("name")});
        }

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }

    if (!isSubscribed) {
        response.sendRedirect("dashboard.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= threadTitle %> - Discussion</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-dark bg-primary px-3">
    <a class="navbar-brand" href="dashboard.jsp">Lille Connect</a>
    <button class="btn btn-light" onclick="window.location.href='dashboard.jsp'">Retour</button>
</nav>

<div class="container mt-4">
    <div class="row">
        <div class="col-md-8">
            <h2><%= threadTitle %></h2>
            <div class="list-group mb-3">
                <% for (String[] message : messages) { %>
                <div class="list-group-item">
                    <strong><%= message[0] %> :</strong>
                    <p class="mb-0"><%= message[1] %></p>
                </div>
                <% } %>
            </div>
            <h4>Publier un Message</h4>
            <form action="PostMessageServlet" method="post">
                <input type="hidden" name="threadId" value="<%= threadId %>">
                <textarea class="form-control mb-2" name="contenu" rows="3" required placeholder="Écrivez votre message ici"></textarea>
                <button type="submit" class="btn btn-primary">Envoyer</button>
            </form>
        </div>
        <div class="col-md-4">
            <% if (isAdmin) { %>
            <h4>Gestion des abonnés</h4>
            <h5>Liste des abonnés :</h5>
            <ul class="list-group mb-3">
                <% for (String[] sub : subscribers) { %>
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    <%= sub[1] %>
                    <% if (Integer.parseInt(sub[0]) != userId) { %>
                    <form action="UnsubscribeServlet" method="post" class="d-inline">
                        <input type="hidden" name="threadId" value="<%= threadId %>">
                        <input type="hidden" name="userId" value="<%= sub[0] %>">
                        <button type="submit" class="btn btn-danger btn-sm">Désinscrire</button>
                    </form>
                    <% } %>
                </li>
                <% } %>
            </ul>
            <h5>Inviter un utilisateur :</h5>
            <form action="InviteUserServlet" method="post">
                <input type="hidden" name="threadId" value="<%= threadId %>">
                <select class="form-select mb-2" name="userId" required>
                    <% for (String[] userEntry : nonSubscribers) { %>
                    <option value="<%= userEntry[0] %>"><%= userEntry[1] %></option>
                    <% } %>
                </select>
                <button type="submit" class="btn btn-success">Inviter</button>
            </form>
            <% } %>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>