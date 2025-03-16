package dao;

import dto.User;
import org.apache.tomcat.jakartaee.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class UserDAO {
    private DS ds;

    public UserDAO() throws IOException {
        this.ds = new DS();
    }

    public boolean addUser(String name, String password) throws SQLException {
        String safeInput = StringEscapeUtils.escapeHtml4(name);
        try(Connection connection = ds.getConnection()) {
            String sql = "INSERT INTO usr (name, pwd) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, password);
                return stmt.executeUpdate() > 0;
            }
        }
    }

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
                            rs.getString("pwd")
                    );
                }
                return null;
            }
        }
    }

    public User getIfExists(String name, String password){
        try (Connection connection = ds.getConnection()) {
            String sql = "SELECT * FROM usr WHERE name = ? AND pwd = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("pwd")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private User getUser(String name, Connection connection, String sql) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("pwd")
                );
            }
            return null;
        }
    }

    public String getUsername(Integer userId) {
        try (Connection connection = ds.getConnection()) {
            String sql = "SELECT name FROM usr WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("name");
                }
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
