package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/app/*")
public class FrontController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        System.out.println("path: " + path);

        if (path == null || path.equals("/")) {
            response.sendRedirect("/index.html");
            return;
        }

        switch (path) {
            case "/like":
                request.getRequestDispatcher("/LikePostServlet").forward(request, response);
                break;

            case "/postMessage":
                request.getRequestDispatcher("/PostMessageServlet").forward(request, response);
                break;

            case "/deletePost":
                request.getRequestDispatcher("/DeletePostServlet").forward(request, response);
                break;

            case "/createThread":
                request.getRequestDispatcher("/CreateThreadServlet").forward(request, response);
                break;

            case "/deleteThread":
                request.getRequestDispatcher("/DeleteThreadServlet").forward(request, response);
                break;

            case "/login":
                request.getRequestDispatcher("/LoginServlet").forward(request, response);
                break;

            case "/logout":
                request.getRequestDispatcher("/LogoutServlet").forward(request, response);
                break;

            case "/createUser":
                request.getRequestDispatcher("/CreateUserServlet").forward(request, response);
                break;

            case "/unsubscribe":
                request.getRequestDispatcher("/UnsubscribeServlet").forward(request, response);
                break;

            case "/invite":
                request.getRequestDispatcher("/InviteUserServlet").forward(request, response);
                break;

            case "/dashboard.jsp":
                request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
                break;

            case "/thread.jsp":
                request.getRequestDispatcher("/thread.jsp").forward(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page non trouvée");
                break;
        }
    }
}