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
public class DeleteListTask extends AsyncTask<Long, Long, AsyncTaskResult<Boolean>> {

    private final Application application;

    public DeleteListTask(Application application) {
        this.application = application;
    }

    @Override
    protected AsyncTaskResult<Boolean> doInBackground(Long... params) {
        try {
            new RequestExecutor(application).deleteList(params[0]);
            return new AsyncTaskResult<>(true, null);
        } catch (UserNotAuthenticatedException e) {
            return new AsyncTaskResult<>(ErrorCode.NOT_AUTHENTICATED);
        } catch (TasksListException e) {
            return new AsyncTaskResult<>(ErrorCode.ERROR_GENERAL);
        } catch (NoNetworkConnectionException e) {
            return new AsyncTaskResult<>(ErrorCode.NO_NETWORK_CONNECTION);
        }
    }
}