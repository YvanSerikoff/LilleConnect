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
import java.sql.SQLException;



@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
    UserDAO userDAO;

    public void init() {
        try {
            userDAO = new UserDAO();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("name");
        String password = request.getParameter("password");

            User usr = userDAO.getIfExists(login, password);
            if (usr != null) {
                // Si l'authentification réussit, démarrer une session et rediriger l'utilisateur vers le tableau de bord
                HttpSession session = request.getSession();
                session.setAttribute("user", usr);
                response.sendRedirect("dashboard.jsp"); // Remplace par la page d'accueil de l'application
            } else {
                // Si l'authentification échoue, rediriger vers la page de connexion avec un message d'erreur
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        }
    }
