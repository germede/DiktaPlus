package singularfactory.app.views.fragments;

/**
 * Created by gerardo on 26.10.16.
 */
public class DiktaplusText {
    public DiktaplusText(String title, String content, String language, String difficulty) {
        this.title = title;
        this.content = content;
        this.language = language;
        this.difficulty = difficulty;
    }

    private String title;
    private String content;
    private String language;
    private String difficulty;

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
