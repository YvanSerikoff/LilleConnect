package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dao.DS;
import dao.ThreadDAO;
import dao.UserDAO;
import dto.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CreateUserServlet")
public class CreateUserServlet extends HttpServlet{

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("name");
        String password = request.getParameter("password");
        UserDAO userDAO = new UserDAO();

        try {
            userDAO.addUser(login,password);
            response.sendRedirect("index.html");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            request.setAttribute("errorMessage", "Failed to create user. Please try again.");
            response.sendRedirect("creation.html");
            return;
        }
    }
}
