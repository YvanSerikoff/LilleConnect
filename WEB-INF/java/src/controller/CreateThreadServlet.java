package controller;

import java.io.IOException;
import java.sql.SQLException;

import dao.ThreadDAO;
import dto.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CreateThreadServlet")
public class CreateThreadServlet extends HttpServlet {
    ThreadDAO threadDAO;

    public void init() {
        try {
            threadDAO = new ThreadDAO();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer les données du formulaire
        String title = request.getParameter("title");

        // Récupérer l'ID de l'utilisateur connecté (depuis la session)
        HttpSession session = request.getSession();
        User usr = (User) session.getAttribute("user");
        int userId = usr.getId();

        try {
            int threadId = threadDAO.addThread(title, userId);
            if (threadId > 0) {
                response.sendRedirect("thread.jsp?threadId=" + threadId);
            } else {
                request.setAttribute("errorMessage", "Failed to create thread. Please try again.");
                response.sendRedirect("dashboard.jsp");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            request.setAttribute("errorMessage", "Failed to create thread. Please try again.");
            response.sendRedirect("dashboard.jsp");
            return;
        }
    }
}
