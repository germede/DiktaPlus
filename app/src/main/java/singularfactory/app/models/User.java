package singularfactory.app.models;

public class User {
    public User(int id, String email, String username, String country, int totalScore, int level) {
        this.id = id;
        this.email = email;
        this.country = country;
        this.username = username;
        this.totalScore = totalScore;
        this.level = level;
    }

    private int id;
    private String email;
    private String country;
    private String username;
    private int totalScore;
    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
