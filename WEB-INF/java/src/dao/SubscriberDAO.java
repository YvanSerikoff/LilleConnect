package dao;

import java.io.IOException;
import java.sql.*;

public class SubscriberDAO {
    private DS ds = new DS();

    public SubscriberDAO() throws IOException {
    }

    // Ajouter un abonnement
    public boolean addSubscriber(int userId, int threadId) throws SQLException {
        try(Connection connection = ds.getConnection()) {
            String sql = "INSERT INTO subscriber (usr_id, thread_id) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, threadId);
                return stmt.executeUpdate() > 0;
            }
        }

    }

    // Vérifier si un utilisateur est abonné à un fil
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

    // Supprimer un abonnement
    public boolean removeSubscriber(int userId, int threadId) throws SQLException {
        try(Connection connection = ds.getConnection()) {
            String sql = "DELETE FROM subscriber WHERE usr_id = ? AND thread_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, threadId);
                return stmt.executeUpdate() > 0;
            }
        }
    }
}
