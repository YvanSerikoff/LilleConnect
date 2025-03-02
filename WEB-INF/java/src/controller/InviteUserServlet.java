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

@WebServlet("/InviteUserServlet")
public class InviteUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int adminId = user.getId();
        int threadId = Integer.parseInt(request.getParameter("threadId"));
        int invitedUserId = Integer.parseInt(request.getParameter("userId"));

        DS ds = new DS();

        try (Connection conn = ds.getConnection()) {
            // Vérifier que l'utilisateur connecté est bien l'admin du thread
            PreparedStatement checkAdminStmt = conn.prepareStatement(
                    "SELECT admin_id FROM thread WHERE id = ?"
            );
            checkAdminStmt.setInt(1, threadId);
            var rs = checkAdminStmt.executeQuery();

            if (rs.next() && rs.getInt("admin_id") == adminId) {
                // Ajouter l'utilisateur dans subscriber
                PreparedStatement inviteStmt = conn.prepareStatement(
                        "INSERT INTO subscriber (usr_id, thread_id) VALUES (?, ?)"
                );
                inviteStmt.setInt(1, invitedUserId);
                inviteStmt.setInt(2, threadId);
                inviteStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        response.sendRedirect("thread.jsp?threadId=" + threadId);
    }
}
