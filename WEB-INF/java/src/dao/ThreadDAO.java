package dao;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import dto.Post;
import dto.Thread;
import org.apache.tomcat.jakartaee.commons.lang3.StringEscapeUtils;

public class ThreadDAO {
    private final DS ds;

    public ThreadDAO() throws IOException {
        this.ds = new DS();
    }

    public int addThread(String title, int adminId) throws SQLException {
        int threadId = 0;
        String safeInput = StringEscapeUtils.escapeHtml4(title);
        try(Connection connection = ds.getConnection()) {
            String sql = "INSERT INTO thread (title, admin_id) VALUES (?, ?)";
            String sub = "INSERT INTO subscriber (usr_id, thread_id) VALUES (?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, safeInput);
                stmt.setInt(2, adminId);
                stmt.executeUpdate();

                String lastId = "SELECT id FROM thread ORDER BY id DESC LIMIT 1";
                PreparedStatement stmt2 = connection.prepareStatement(lastId);
                ResultSet rs = stmt2.executeQuery();
                if (rs.next()) {
                    threadId = rs.getInt(1);
                }
                try (PreparedStatement stmt3 = connection.prepareStatement(sub)) {
                    stmt3.setInt(1, adminId);
                    stmt3.setInt(2, threadId);
                    stmt3.executeUpdate();
                }

                return threadId;
            }
        }
    }

    public boolean isAdministrator(int userId, int threadId) throws SQLException {
        try(Connection connection = ds.getConnection()) {
            String sql = "SELECT admin_id FROM thread WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, threadId);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt("admin_id") == userId;
            }
        }
    }

    public String getThreadTitle(int threadId) throws SQLException {
        try(Connection connection = ds.getConnection()) {
            String sql = "SELECT title FROM thread WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, threadId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("title");
                }
                return null;
            }
        }
    }

    public boolean isUserSubscribed(int userId, int threadId) throws SQLException, IOException {
        String sql = "SELECT COUNT(*) FROM subscriber WHERE usr_id = ? AND thread_id = ?";
        DS ds = new DS();

        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, threadId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return rs.getInt(1) > 0; // S'il y a au moins une ligne, l'utilisateur est abonné
                }
            }

            return false;
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
    }

    public List<Thread> getSubscribedThreads(int userId) throws SQLException, IOException {
        List<Thread> threads = new ArrayList<>();
        DS ds = new DS();
        String sql = "SELECT t.id, t.title FROM thread t " +
                "JOIN subscriber s ON t.id = s.thread_id " +
                "WHERE s.usr_id = ?";

        try (Connection conn = ds.getConnection()){
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    threads.add(new Thread(rs.getInt("id"), rs.getString("title"), userId));
                }
            }
            return threads;
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    public boolean deleteThread(int threadId, int id) {
        try (Connection connection = ds.getConnection()) {
            String sql = "DELETE FROM thread WHERE id = ? AND admin_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, threadId);
                stmt.setInt(2, id);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}