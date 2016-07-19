package pl.mslawin.notes.app;

import android.app.Application;

import pl.mslawin.notes.app.service.user.UserAuthentication;

/**
 * Created by maciej on 1/11/16.
 */
public class TaskListApplication extends Application {

    private UserAuthentication userAuthentication;

    public UserAuthentication getUserAuthentication() {
        return userAuthentication;
    }

    public void setUserAuthentication(UserAuthentication userAuthentication) {
        this.userAuthentication = userAuthentication;
    }

    public void removeUserAuthentication() {
        this.userAuthentication = null;
    }
}