package controller;

import dao.UserDAO;
import dto.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    UserDAO userDAO;

    public void init() {
        try {
            userDAO = new UserDAO();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("name");
        String password = request.getParameter("password");

            User usr = userDAO.getIfExists(login, password);
            if (usr != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", usr);
                response.sendRedirect("dashboard.jsp");
            } else {
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("index.html").forward(request, response);
            }
        }
    }
