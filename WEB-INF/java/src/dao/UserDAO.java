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
        String safePassword = StringEscapeUtils.escapeHtml4(password);
        try(Connection connection = ds.getConnection()) {
            String sql = "INSERT INTO usr (name, pwd) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, safeInput);
                stmt.setString(2, safePassword);
                return stmt.executeUpdate() > 0;
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
}
