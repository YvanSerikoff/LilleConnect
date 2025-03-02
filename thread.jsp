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
        response.sendRedirect("login.jsp");
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
    <title><%= threadTitle %> - Discussion</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .container { max-width: 600px; margin: auto; }
        .message { border-bottom: 1px solid #ddd; padding: 10px; }
        .user { font-weight: bold; }
        form { margin-top: 20px; }
        textarea, button, select { width: 100%; padding: 8px; margin: 5px 0; }
        button { background-color: #007BFF; color: white; border: none; cursor: pointer; }
        button:hover { background-color: #0056b3; }
        .alert { color: red; font-weight: bold; margin-top: 20px; }
    </style>
</head>

<header>
    <button onclick="window.location.href='dashboard.jsp'">Retour au Dashboard</button>
</header>
<body>

<div class="container">
    <h2>Thread: <%= threadTitle %></h2>

    <div>
        <% for (String[] message : messages) { %>
        <div class="message">
            <span class="user"><%= message[0] %> :</span>
            <p><%= message[1] %></p>
        </div>
        <% } %>
    </div>

    <h3>Publier un Message</h3>
    <form action="PostMessageServlet" method="post">
        <input type="hidden" name="threadId" value="<%= threadId %>">
        <textarea name="contenu" rows="3" required placeholder="Écrivez votre message ici" style="width: 100%; height: 100px; resize: none;"></textarea>
        <button type="submit">Envoyer</button>
    </form>

    <% if (isAdmin) { %>
    <h3>Gestion des abonnés</h3>
    <h4>Liste des abonnés :</h4>
    <ul>
        <% for (String[] sub : subscribers) { %>
        <li><%= sub[1] %>
            <% if (Integer.parseInt(sub[0]) != userId) {%>
            <form action="UnsubscribeServlet" method="post" style="display:inline;">
                <input type="hidden" name="threadId" value="<%= threadId %>">
                <input type="hidden" name="userId" value="<%= sub[0] %>">
                <button type="submit">Désinscrire</button>
            </form>
            <% } %>
        </li>
        <% } %>
    </ul>

    <h4>Inviter un utilisateur :</h4>
    <form action="InviteUserServlet" method="post">
        <input type="hidden" name="threadId" value="<%= threadId %>">
        <select name="userId" required>
            <% for (String[] userEntry : nonSubscribers) { %>
            <option value="<%= userEntry[0] %>"><%= userEntry[1] %></option>
            <% } %>
        </select>
        <button type="submit">Inviter</button>
    </form>
    <% } %>
</div>

</body>
</html>
