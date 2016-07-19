package pl.mslawin.notes.app.service.request.task;

import android.app.Application;
import android.os.AsyncTask;

import java.util.Collection;

import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.exception.UserNotAuthenticatedException;
import pl.mslawin.notes.app.service.request.AsyncTaskResult;
import pl.mslawin.notes.app.service.request.AsyncTaskResult.ErrorCode;
import pl.mslawin.notes.app.service.request.RequestExecutor;

/**
 * Created by mslawin on 1/17/16.
 */
public class GetConnectedUsersTask extends AsyncTask<Void, Long, AsyncTaskResult<Collection<String>>> {

    private final Application application;

    public GetConnectedUsersTask(Application application) {
        this.application = application;
    }

    @Override
    protected AsyncTaskResult<Collection<String>> doInBackground(Void... params) {
        try {
            Collection<String> connectedUsers = new RequestExecutor(application).getConnectedUsers();
            return new AsyncTaskResult<>(connectedUsers);
        } catch (UserNotAuthenticatedException e) {
            return new AsyncTaskResult<>(ErrorCode.NOT_AUTHENTICATED);
        } catch (TasksListException e) {
            return new AsyncTaskResult<>(ErrorCode.ERROR_GENERAL);
        } catch (NoNetworkConnectionException e) {
            return new AsyncTaskResult<>(ErrorCode.NO_NETWORK_CONNECTION);
        }
    }
}