package dto;

public class Post {

    private int id;
    private String content;
    private int userId;
    private int threadId;

    public Post(int id, String content, int userId, int threadId) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.threadId = threadId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }
}
