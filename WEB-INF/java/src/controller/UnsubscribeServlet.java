package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dao.DS;
import dto.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/UnsubscribeServlet")
public class UnsubscribeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int adminId = user.getId();
        int threadId = Integer.parseInt(request.getParameter("threadId"));
        int userIdToRemove = Integer.parseInt(request.getParameter("userId"));

        DS ds = new DS();

        try (Connection conn = ds.getConnection()) {
            // Vérifier que l'utilisateur connecté est bien l'admin du thread
            PreparedStatement checkAdminStmt = conn.prepareStatement(
                    "SELECT admin_id FROM thread WHERE id = ?"
            );
            checkAdminStmt.setInt(1, threadId);
            var rs = checkAdminStmt.executeQuery();

            if (rs.next() && rs.getInt("admin_id") == adminId) {
                // Supprimer l'utilisateur de subscriber
                PreparedStatement removeStmt = conn.prepareStatement(
                        "DELETE FROM subscriber WHERE usr_id = ? AND thread_id = ?"
                );
                removeStmt.setInt(1, userIdToRemove);
                removeStmt.setInt(2, threadId);
                removeStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        response.sendRedirect("thread.jsp?threadId=" + threadId);
    }
}
