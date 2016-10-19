package singularfactory.app.models;

public class ErrorData {
    private String errorId;
    private String message;

    public String getErrorId() {
        return errorId;
    }
    public void setErrorId(String errorId) { this.errorId = errorId; }
    public String getMessage() { return message; }
    public void setMessage(String message) {
        this.message = message;
    }
}
