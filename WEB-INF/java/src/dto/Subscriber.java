package dto;

public class Subscriber {
    private int userId;
    private int threadId;

    // Constructeur
    public Subscriber(int userId, int threadId) {
        this.userId = userId;
        this.threadId = threadId;
    }

    // Getters et Setters
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
