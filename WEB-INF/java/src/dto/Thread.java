package dto;

public class Thread {
    private int id;
    private String title;
    private int adminId;

    // Constructeur
    public Thread(int id, String title, int adminId) {
        this.id = id;
        this.title = title;
        this.adminId = adminId;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}
