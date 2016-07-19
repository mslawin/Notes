package pl.mslawin.notes.app.service;

import android.app.Application;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.service.request.AsyncTaskResult;
import pl.mslawin.notes.app.service.request.task.GetConnectedUsersTask;
import pl.mslawin.notes.app.service.request.task.LoginTask;
import pl.mslawin.notes.app.service.request.task.LogoutTask;
import pl.mslawin.notes.app.service.user.UserAuthentication;

import static pl.mslawin.notes.app.service.TasksService.handleResult;

/**
 * Created by maciej on 1/11/16.
 */
public class UserService {

    public UserAuthentication authenticate(Application application, String idToken) throws TasksListException, NoNetworkConnectionException {
        LoginTask loginTask = new LoginTask(application);
        loginTask.execute(idToken);
        try {
            AsyncTaskResult<UserAuthentication> result = loginTask.get();
            return handleResult(application, result);
        } catch (InterruptedException | ExecutionException e) {
            throw new TasksListException(e);
        }
    }

    public Collection<String> getConnectedUsers(Application application) throws TasksListException, NoNetworkConnectionException {
        GetConnectedUsersTask task = new GetConnectedUsersTask(application);
        task.execute();
        try {
            AsyncTaskResult<Collection<String>> result = task.get();
            return handleResult(application, result);
        } catch (InterruptedException | ExecutionException e) {
            throw new TasksListException(e);
        }
    }

    public void logout(Application application) throws TasksListException, NoNetworkConnectionException {
        LogoutTask logoutTask = new LogoutTask(application);
        logoutTask.execute();
        try {
            AsyncTaskResult<Boolean> result = logoutTask.get();
            handleResult(application, result);
        } catch (InterruptedException | ExecutionException e) {
            throw new TasksListException();
        }
    }
}