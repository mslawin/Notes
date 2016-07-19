package pl.mslawin.notes.app.service.user;

/**
 * Created by maciej on 1/11/16.
 */
public class UserAuthentication {

    private String email;
    private String sessionId;

    public UserAuthentication(String email, String sessionId) {
        this.email = email;
        this.sessionId = sessionId;
    }

    public String getEmail() {
        return email;
    }

    public String getSessionId() {
        return sessionId;
    }
}