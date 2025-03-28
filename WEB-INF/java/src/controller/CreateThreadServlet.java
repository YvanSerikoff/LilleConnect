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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = request.getParameter("title");

        HttpSession session = request.getSession();
        User usr = (User) session.getAttribute("user");
        if (usr == null) {
            response.sendRedirect("/LilleConnect/index.html");
            return;
        }

        int userId = usr.getId();

        try {
            int threadId = threadDAO.addThread(title, userId);

            System.out.println("threadId: " + threadId);
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
