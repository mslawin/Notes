package pl.mslawin.notes.app.service.request.task;

import android.app.Application;
import android.os.AsyncTask;

import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.service.request.AsyncTaskResult;
import pl.mslawin.notes.app.service.request.AsyncTaskResult.ErrorCode;
import pl.mslawin.notes.app.service.request.RequestExecutor;
import pl.mslawin.notes.app.service.user.UserAuthentication;

/**
 * Created by mslawin on 1/11/16.
 */
public class LoginTask extends AsyncTask<String, Long, AsyncTaskResult<UserAuthentication>> {

    private final Application application;

    public LoginTask(Application application) {
        this.application = application;
    }

    @Override
    protected AsyncTaskResult<UserAuthentication> doInBackground(String... params) {
        RequestExecutor requestExecutor = new RequestExecutor(application);
        try {
            UserAuthentication userAuthentication = requestExecutor.authenticateUser(params[0]);
            return new AsyncTaskResult<>(userAuthentication);
        } catch (TasksListException e) {
            return new AsyncTaskResult<>(ErrorCode.ERROR_GENERAL);
        } catch (NoNetworkConnectionException e) {
            return new AsyncTaskResult<>(ErrorCode.NO_NETWORK_CONNECTION);
        }
    }
}