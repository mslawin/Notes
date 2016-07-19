package pl.mslawin.notes.app.view.listener;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.view.View;

import java.util.logging.Logger;

import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.service.TasksService;
import pl.mslawin.notes.app.service.UserService;
import pl.mslawin.notes.app.view.GoogleSignInActivity;

/**
 * Created by mslawin on 4/10/16.
 */
public class LogoutButtonListener implements View.OnClickListener {

    private static final Logger logger = Logger.getLogger(LogoutButtonListener.class.getName());

    private final UserService userService;
    private final Application application;
    private final Activity currentActivity;

    public LogoutButtonListener(UserService userService, Application application, Activity currentActivity) {
        this.userService = userService;
        this.application = application;
        this.currentActivity = currentActivity;
    }

    @Override
    public void onClick(View v) {
        try {
            userService.logout(application);
            Intent intent = new Intent(currentActivity, GoogleSignInActivity.class);
            currentActivity.startActivity(intent);
            currentActivity.finish();
        } catch (TasksListException e) {
            TasksService.handleTasksListException(logger, e, application.getApplicationContext());
        } catch (NoNetworkConnectionException e) {
            TasksService.handleNoNetworkException(logger, e, application.getApplicationContext());
        }
    }
}