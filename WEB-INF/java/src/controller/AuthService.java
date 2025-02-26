package controller;

import dao.UserDAO;
import dto.PasswordUtil;
import dto.User;

import java.sql.*;

public class AuthService {
    private UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // Authentifier un utilisateur
    public User authenticate(String email, String password) throws SQLException {
        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            return null;
        }

        // Vérifier le mot de passe
        if (PasswordUtil.checkPassword(password, user.getPassword())) {
            return new User(user.getId(), user.getName(), user.getEmail(), user.getPassword());
        }

        return null; // Mot de passe incorrect
    }
}
