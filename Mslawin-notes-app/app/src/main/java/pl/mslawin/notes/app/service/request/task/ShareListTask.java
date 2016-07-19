package pl.mslawin.notes.app.service.request.task;

import android.app.Application;
import android.os.AsyncTask;

import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.exception.UserNotAuthenticatedException;
import pl.mslawin.notes.app.service.request.AsyncTaskResult;
import pl.mslawin.notes.app.service.request.AsyncTaskResult.ErrorCode;
import pl.mslawin.notes.app.service.request.RequestExecutor;

/**
 * Created by mslawin on 1/17/16.
 */
public class ShareListTask extends AsyncTask<String, Long, AsyncTaskResult<Boolean>> {

    private final Application application;

    public ShareListTask(Application application) {
        this.application = application;
    }

    @Override
    protected AsyncTaskResult<Boolean> doInBackground(String... params) {
        try {
            new RequestExecutor(application).shareList(Long.parseLong(params[0]), params[1]);
            return new AsyncTaskResult<>(Boolean.TRUE, null);
        } catch (UserNotAuthenticatedException e) {
            return new AsyncTaskResult<>(ErrorCode.NOT_AUTHENTICATED);
        } catch (TasksListException e) {
            return new AsyncTaskResult<>(ErrorCode.ERROR_GENERAL);
        } catch (NoNetworkConnectionException e) {
            return new AsyncTaskResult<>(ErrorCode.NO_NETWORK_CONNECTION);
        }
    }
}