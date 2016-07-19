package pl.mslawin.notes.app.service.request;

import android.app.Application;
import android.content.Intent;

import pl.mslawin.notes.app.view.GoogleSignInActivity;

/**
 * Created by mslawin on 1/17/16.
 */
public class RequestErrorService {

    public void handleUserNotAuthenticated(Application application) {
        application.startActivity(new Intent(application.getApplicationContext(), GoogleSignInActivity.class));
    }

    public void handleNoNetworkConnectionError() {

    }
}