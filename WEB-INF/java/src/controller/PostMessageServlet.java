package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dao.DS;
import dao.ThreadDAO;
import dto.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/PostMessageServlet")
public class PostMessageServlet extends HttpServlet {

    ThreadDAO threadDAO;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Vérification de l'utilisateur connecté
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Integer userId = user.getId();

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Récupération des paramètres du formulaire
        String contenu = request.getParameter("contenu");
        int threadId = Integer.parseInt(request.getParameter("threadId"));

        // Insertion du message dans la base de données
        DS ds = new DS();
        threadDAO = new ThreadDAO();
        try (Connection conn = ds.getConnection()) {

            if (threadDAO.isUserSubscribed(userId,threadId)){
                String sql = "INSERT INTO post (contenu, usr_id, thread_id) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, contenu);
                    stmt.setInt(2, userId);
                    stmt.setInt(3, threadId);
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du message : " + e.getMessage());
            response.sendRedirect("error.jsp");
            return;
        }

        // Redirection vers la page du thread
        response.sendRedirect("thread.jsp?threadId=" + threadId);
    }
}
