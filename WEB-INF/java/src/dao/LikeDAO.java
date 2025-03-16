package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeDAO {

    private DS ds =  new DS();


    public LikeDAO() throws IOException {
    }

    public void addLike(int usr_id, int post_id) {
        try (Connection conn = ds.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO likes (usr_id, post_id) VALUES (?, ?)"
            );
            stmt.setInt(1, usr_id);
            stmt.setInt(2, post_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeLike(int usr_id, int post_id) {
        try (Connection conn = ds.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM likes WHERE usr_id = ? AND post_id = ?"
            );
            stmt.setInt(1, usr_id);
            stmt.setInt(2, post_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getLikeCount(int post_id) {
        try (Connection conn = ds.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM likes WHERE post_id = ?"
            );
            stmt.setInt(1, post_id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public boolean hasLiked(int usr_id, int post_id){
        try(Connection conn = ds.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT 1 FROM likes WHERE usr_id = ? AND post_id = ?"
            );
            stmt.setInt(1, usr_id);
            stmt.setInt(2, post_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return true;
            }
            return false;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
