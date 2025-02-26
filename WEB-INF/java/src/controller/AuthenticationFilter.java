package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AuthenticationFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        // Si la session est null ou l'utilisateur n'est pas authentifié, rediriger vers login.jsp
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect("login.jsp");
        } else {
            chain.doFilter(request, response); // Continuer la chaîne de filtres
        }
    }

    public void destroy() {}
}
