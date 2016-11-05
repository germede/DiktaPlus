package singularfactory.app.models;

public class Text {
    public Text(int id, String title, String content, String language, String difficulty) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.language = language;
        this.difficulty = difficulty;
        this.bestScore = 0;
    }

    private int id;
    private String title;
    private String content;
    private String language;
    private String difficulty;

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    private int bestScore;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
