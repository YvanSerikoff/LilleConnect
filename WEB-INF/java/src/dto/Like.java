package dto;

public class Like {
    private int usr_id;
    private int post_id;

    public Like(int usr_id, int post_id) {
        this.usr_id = usr_id;
        this.post_id = post_id;
    }

    public int getUser_id() {
        return usr_id;
    }

    public void setUser_id(int usr_id) {
        this.usr_id = usr_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
}
