package controller;

import dao.ThreadDAO;
import dto.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/DeleteThreadServlet")
public class DeleteThreadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("/LilleConnect/index.html");
            return;
        }

        int threadId = Integer.parseInt(request.getParameter("threadId"));
        ThreadDAO threadDAO = new ThreadDAO();
        boolean deleted = threadDAO.deleteThread(threadId, user.getId());

        if (deleted) {
            response.sendRedirect("dashboard.jsp");
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Action non autorisée");
        }
    }
}
