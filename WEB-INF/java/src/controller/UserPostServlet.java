package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.PostDAO;
import dto.Post;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/posts/*")
public class UserPostServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String info = request.getPathInfo();
        ObjectMapper objectMapper = new ObjectMapper();

        if (info == null || info.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"ID utilisateur requis.\"}");
            return;
        }

        String[] split = info.split("/");
        if (split.length != 2) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Format incorrect. Utilisation : /posts/{id}\"}");
            return;
        }

        try {
            int userId = Integer.parseInt(split[1]);
            PostDAO postDAO = new PostDAO();
            List<Post> posts = postDAO.findByUserId(userId);
            String json = objectMapper.writeValueAsString(posts);
            response.getWriter().write(json);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"ID utilisateur invalide.\"}");
        }
    }
}
