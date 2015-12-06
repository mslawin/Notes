package pl.mslawin.notes.dto.request;

public class LoginRequest {

    private String loginToken;

    public LoginRequest() {
    }

    public LoginRequest(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }
}
