package controller;

import dao.PostDAO;
import dto.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/DeletePostServlet")
public class DeletePostServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("index.html");
            return;
        }

        int postId = Integer.parseInt(request.getParameter("postId"));
        int threadId = Integer.parseInt(request.getParameter("threadId"));

        PostDAO postDAO = new PostDAO();
        boolean deleted = postDAO.deletePost(postId, user.getId());

        if (deleted) {
            response.sendRedirect("thread.jsp?threadId=" + threadId);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Action non autorisée");
        }
    }
}
