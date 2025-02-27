<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.sql.*, java.util.*" %>
<%@ page import="dao.DS" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="dto.User" %>

<%
    // Récupération de l'ID du thread depuis l'URL
    String threadIdStr = request.getParameter("threadId");
    int threadId = (threadIdStr != null) ? Integer.parseInt(threadIdStr) : -1;

    // Vérification de l'utilisateur connecté
    HttpSession userSession = request.getSession();
    User user = (User) userSession.getAttribute("user");
    Integer userId = user.getId();

    if (userId == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Connexion à la base de données
    DS ds = new DS();

    // Récupération du titre du thread
    String threadTitle = "";
    List<String[]> messages = new ArrayList<>();
    try (Connection conn = ds.getConnection()){
        PreparedStatement stmtThread = conn.prepareStatement("SELECT title FROM thread WHERE id = ?");
        stmtThread.setInt(1, threadId);
        ResultSet rsThread = stmtThread.executeQuery();
        if (rsThread.next()) {
            threadTitle = rsThread.getString("title");
        }

        // Récupération des messages associés au thread
        PreparedStatement stmtMessages = conn.prepareStatement(
                "SELECT post.contenu, usr.name FROM post JOIN usr ON post.usr_id = usr.id WHERE thread_id = ? ORDER BY post.id ASC"
        );
        stmtMessages.setInt(1, threadId);
        ResultSet rsMessages = stmtMessages.executeQuery();
        while (rsMessages.next()) {
            messages.add(new String[]{rsMessages.getString("name"), rsMessages.getString("contenu")});
        }
    }catch (SQLException e){
        System.out.println(e.getMessage());
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
        input, button { width: 100%; padding: 8px; margin: 5px 0; }
        button { background-color: #007BFF; color: white; border: none; cursor: pointer; }
        button:hover { background-color: #0056b3; }
    </style>
</head>

<header>
    <button onclick="window.location.href='dashboard.jsp'">Retour au Dashboard</button>
</header>
<body>

<div class="container">
    <h2>Thread: <%= threadTitle %></h2>

    <!-- Affichage des messages -->
    <div>
        <% for (String[] message : messages) { %>
        <div class="message">
            <span class="user"><%= message[0] %> :</span>
            <p><%= message[1] %></p>
        </div>
        <% } %>
    </div>

    <!-- Formulaire pour publier un message -->
    <h3>Publier un Message</h3>
    <form action="PostMessageServlet" method="post">
        <input type="hidden" name="threadId" value="<%= threadId %>">
        <textarea name="contenu" rows="3" required placeholder="Écrivez votre message ici" style="width: 100%; height: 100px; resize: none;"></textarea>
        <button type="submit">Envoyer</button>
    </form>
</div>

</body>
</html>
