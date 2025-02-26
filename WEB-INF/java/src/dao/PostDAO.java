package dao;

import dto.Post;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class PostDAO {
    private DS ds;

    public PostDAO() throws IOException {
        this.ds = new DS();
    }

    // Ajouter un message
    public boolean addPost(int userId, int threadId, String contenu) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String sql = "INSERT INTO post (contenu, usr_id, thread_id) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, contenu);
                stmt.setInt(2, userId);
                stmt.setInt(3, threadId);
                return stmt.executeUpdate() > 0;
            }
        }
    }

    // Récupérer tous les messages d'un fil de discussion
    public List<Post> getPostsByThreadId(int threadId) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            String sql = "SELECT * FROM post WHERE thread_id = ? ORDER BY id ASC";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, threadId);
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
        }
    }

}
