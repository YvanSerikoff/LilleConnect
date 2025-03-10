package controller;

import java.io.IOException;
import java.sql.SQLException;
import dao.PostDAO;
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
    PostDAO postDAO;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Integer userId = user.getId();

        if (userId == null) {
            response.sendRedirect("index.html");
            return;
        }

        String contenu = request.getParameter("contenu");
        int threadId = Integer.parseInt(request.getParameter("threadId"));

        threadDAO = new ThreadDAO();
        postDAO = new PostDAO();

        try {
            if (threadDAO.isUserSubscribed(userId,threadId)){
                postDAO.addPost(userId, threadId, contenu);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        response.sendRedirect("thread.jsp?threadId=" + threadId);
    }
}
