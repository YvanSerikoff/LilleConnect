package dao;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import dto.Thread;

public class ThreadDAO {
    private final DS ds;

    public ThreadDAO() throws IOException {
        this.ds = new DS();
    }

    // Ajouter un fil de discussion
    public int addThread(String title, int adminId) throws SQLException {
        int threadId = 0;

        try(Connection connection = ds.getConnection()) {
            String sql = "INSERT INTO thread (title, admin_id) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, title);
                stmt.setInt(2, adminId);
                stmt.executeUpdate();

                // Récupération de l'ID du thread inséré
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    threadId = rs.getInt(1);
                }

                return threadId;
            }
        }
    }

    // Récupérer un fil de discussion par ID
    public Thread getThreadById(int id) throws SQLException {
        try(Connection connection = ds.getConnection()) {
            String sql = "SELECT * FROM thread WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new Thread(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getInt("admin_id")
                    );
                }
                return null;
            }
        }
    }

    // Récupérer tous les fils de discussion d'un utilisateur
    public List<Thread> getThreadsByUserId(int userId) throws SQLException {
        try(Connection connection = ds.getConnection()) {
            String sql = "SELECT t.* FROM thread t JOIN subscriber s ON t.id = s.thread_id WHERE s.usr_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                List<Thread> threads = new ArrayList<>();
                while (rs.next()) {
                    threads.add(new Thread(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getInt("admin_id")
                    ));
                }
                return threads;
            }
        }
    }
}