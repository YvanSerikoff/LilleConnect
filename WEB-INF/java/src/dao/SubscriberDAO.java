package dao;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubscriberDAO {
    private DS ds = new DS();

    public SubscriberDAO() throws IOException {
    }

    public void addSubscriber(int userId, int threadId, int adminId) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String sql = "SELECT admin_id FROM thread WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, threadId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt("admin_id") == adminId) {
                    String sql2 = "INSERT INTO subscriber (usr_id, thread_id) VALUES (?, ?)";
                    try (PreparedStatement stmt2 = connection.prepareStatement(sql2)) {
                        stmt2.setInt(1, userId);
                        stmt2.setInt(2, threadId);
                        stmt2.executeUpdate();
                    }catch (SQLException e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        }

    }

    public void removeSubscriber(int userId, int threadId, int admin_id) throws SQLException {
        try(Connection connection = ds.getConnection()) {
            String sql = "SELECT admin_id FROM thread WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, threadId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt("admin_id") == admin_id) {
                    String sql2 = "DELETE FROM subscriber WHERE usr_id = ? AND thread_id = ?";
                    try (PreparedStatement stmt2 = connection.prepareStatement(sql2)) {
                        stmt2.setInt(1, userId);
                        stmt2.setInt(2, threadId);
                        stmt2.executeUpdate();
                    }catch (SQLException e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    public List<String[]> getSubscribers(int threadId) throws SQLException {
        List<String[]> users = new ArrayList<>();
        try(Connection connection = ds.getConnection()) {
            String sql = "SELECT u.id, u.name FROM usr u JOIN subscriber s ON u.id = s.usr_id AND s.thread_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, threadId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new String[]{rs.getString("id"), rs.getString("name")});
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return users;
    }

    public List<String[]> getNonSubscribers(int threadId) throws SQLException {
        List<String[]> users = new ArrayList<>();
        try(Connection connection = ds.getConnection()) {
            String sql = "SELECT u.id, u.name FROM usr u LEFT JOIN subscriber s ON u.id = s.usr_id AND s.thread_id = ? WHERE s.usr_id IS NULL";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, threadId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new String[]{rs.getString("id"), rs.getString("name")});
            }
            System.out.println(users);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return users;
    }
}
