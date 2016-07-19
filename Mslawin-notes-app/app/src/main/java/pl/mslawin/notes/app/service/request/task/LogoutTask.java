package pl.mslawin.notes.app.service.request.task;

import android.app.Application;
import android.os.AsyncTask;

import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.service.request.AsyncTaskResult;
import pl.mslawin.notes.app.service.request.AsyncTaskResult.ErrorCode;
import pl.mslawin.notes.app.service.request.RequestExecutor;

/**
 * Created by mslawin on 4/10/16.
 */
public class LogoutTask extends AsyncTask<Void, Long, AsyncTaskResult<Boolean>> {

    private final Application application;

    public LogoutTask(Application application) {
        this.application = application;
    }

    @Override
    protected AsyncTaskResult<Boolean> doInBackground(Void... params) {
        RequestExecutor requestExecutor = new RequestExecutor(application);
        try {
            requestExecutor.logoutUser();
            return new AsyncTaskResult<>(Boolean.TRUE);
        } catch (TasksListException e) {
            return new AsyncTaskResult<>(ErrorCode.ERROR_GENERAL);
        } catch (NoNetworkConnectionException e) {
            return new AsyncTaskResult<>(ErrorCode.NO_NETWORK_CONNECTION);
        }
    }
}