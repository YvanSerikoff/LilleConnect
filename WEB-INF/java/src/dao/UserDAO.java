package dao;

import dto.User;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class UserDAO {
    private DS ds;

    public UserDAO() throws IOException {
        this.ds = new DS();
    }

    // Ajouter un utilisateur
    public boolean addUser(String name, String email, String password) throws SQLException {
        try(Connection connection = ds.getConnection()) {
            String sql = "INSERT INTO usr (name, email, pwd) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, password); // Assure-toi de crypter le mot de passe avant d'appeler cette méthode
                return stmt.executeUpdate() > 0;
            }
        }
    }

    // Récupérer un utilisateur par email
    public User getUserByEmail(String email) throws SQLException {
        try(Connection connection = ds.getConnection()) {
            String sql = "SELECT * FROM usr WHERE email = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("pwd")
                    );
                }
                return null;
            }
        }
    }

    // Récupérer un utilisateur par ID
    public User getUserById(int id) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String sql = "SELECT * FROM usr WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("pwd")
                    );
                }
                return null;
            }
        }
    }
}
