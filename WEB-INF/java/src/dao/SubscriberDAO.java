package dao;

import java.io.IOException;
import java.sql.*;

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

    public boolean isSubscribed(int userId, int threadId) throws SQLException {
        try(Connection connection = ds.getConnection()) {
            String sql = "SELECT 1 FROM subscriber WHERE usr_id = ? AND thread_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, threadId);
                ResultSet rs = stmt.executeQuery();
                return rs.next();
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
}
