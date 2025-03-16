package controller;

import dao.LikeDAO;
import dto.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LikePostServlet")
public class LikePostServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("/LilleConnect/index.html");
            return;
        }

        int userId = user.getId();
        int messageId = Integer.parseInt(request.getParameter("messageId"));
        int threadId = Integer.parseInt(request.getParameter("threadId"));

        LikeDAO likeDAO = new LikeDAO();
        if (likeDAO.hasLiked(userId, messageId)) {
            likeDAO.removeLike(userId, messageId);
            System.out.println("Removed like");
        } else {
            likeDAO.addLike(userId, messageId);
            System.out.println("Added like");
        }

        response.sendRedirect("thread.jsp?threadId=" + threadId);
    }
}
