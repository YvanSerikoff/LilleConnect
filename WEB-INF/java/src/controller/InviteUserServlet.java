package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dao.DS;
import dao.SubscriberDAO;
import dao.ThreadDAO;
import dto.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/InviteUserServlet")
public class InviteUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("/LilleConnect/index.html");
            return;
        }

        int adminId = user.getId();
        int threadId = Integer.parseInt(request.getParameter("threadId"));
        int invitedUserId = Integer.parseInt(request.getParameter("userId"));

        SubscriberDAO subscriberDAO = new SubscriberDAO();

        try{
            subscriberDAO.addSubscriber(invitedUserId, threadId, adminId);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        response.sendRedirect("thread.jsp?threadId=" + threadId);
    }
}
