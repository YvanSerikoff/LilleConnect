package dao;

import dto.Post;
import org.apache.tomcat.jakartaee.commons.lang3.StringEscapeUtils;
import java.io.IOException;
import java.sql.*;
import java.util.*;


public class PostDAO {
    private DS ds;


    public PostDAO() throws IOException {
        this.ds = new DS();
    }

    public void addPost(int userId, int threadId, String contenu) throws SQLException {
        String safeInput = StringEscapeUtils.escapeHtml4(contenu);
        try (Connection connection = ds.getConnection()) {
            String sql = "INSERT INTO post (contenu, usr_id, thread_id) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, safeInput);
                stmt.setInt(2, userId);
                stmt.setInt(3, threadId);
                stmt.executeUpdate();
            }
        }
    }

    public List<String[]> getPostsByThreadId(int threadId) throws SQLException {
        List<String[]> messages = new ArrayList<>();
        try (Connection conn = ds.getConnection()) {
            PreparedStatement stmtMessages = conn.prepareStatement(
                    "SELECT post.id, post.contenu, usr.name, post.usr_id FROM post " +
                            "JOIN usr ON post.usr_id = usr.id WHERE thread_id = ? ORDER BY post.id ASC"
            );
            stmtMessages.setInt(1, threadId);
            ResultSet rsMessages = stmtMessages.executeQuery();
            while (rsMessages.next()) {
                int messageId = rsMessages.getInt("id");
                messages.add(new String[]{
                        rsMessages.getString("name"),
                        rsMessages.getString("contenu"),
                        rsMessages.getString("usr_id"),
                        String.valueOf(messageId),
                });
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public List<Post> findByUserId(int id) {
        try (Connection connection = ds.getConnection()) {
            String sql = "SELECT * FROM post WHERE usr_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                List<Post> posts = new ArrayList<>();
                while (rs.next()) {
                    posts.add(new Post(
                            rs.getInt("id"),
                            rs.getString("contenu"),
                            rs.getInt("usr_id"),
                            rs.getInt("thread_id")
                    ));
                }
                return posts;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deletePost(int postId, int id) {
        try (Connection connection = ds.getConnection()) {
            String sql = "DELETE FROM post WHERE id = ? AND usr_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, postId);
                stmt.setInt(2, id);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
