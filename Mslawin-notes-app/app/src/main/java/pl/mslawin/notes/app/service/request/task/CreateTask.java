package pl.mslawin.notes.app.service.request.task;

import android.app.Application;
import android.os.AsyncTask;

import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.exception.UserNotAuthenticatedException;
import pl.mslawin.notes.app.model.Task;
import pl.mslawin.notes.app.service.request.AsyncTaskResult;
import pl.mslawin.notes.app.service.request.AsyncTaskResult.ErrorCode;
import pl.mslawin.notes.app.service.request.RequestExecutor;

/**
 * Created by mslawin on 10/31/15.
 */
public class CreateTask extends AsyncTask<String, Long, AsyncTaskResult<Task>> {

    private final Long taskListId;
    private final Application application;

    public CreateTask(Long taskListId, Application application) {
        this.taskListId = taskListId;
        this.application = application;
    }

    @Override
    protected AsyncTaskResult<Task> doInBackground(String... params) {
        RequestExecutor requestExecutor = new RequestExecutor(application);
        try {
            Task task = requestExecutor.createTask(taskListId, params[0]);
            return new AsyncTaskResult<>(task);
        } catch (UserNotAuthenticatedException e) {
            return new AsyncTaskResult<>(ErrorCode.NOT_AUTHENTICATED);
        } catch (TasksListException e) {
            return new AsyncTaskResult<>(ErrorCode.ERROR_GENERAL);
        } catch (NoNetworkConnectionException e) {
            return new AsyncTaskResult<>(ErrorCode.NO_NETWORK_CONNECTION);
        }
    }
}