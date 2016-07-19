package pl.mslawin.notes.app.service.request.task;

import android.app.Application;
import android.os.AsyncTask;

import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.exception.UserNotAuthenticatedException;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.request.AsyncTaskResult;
import pl.mslawin.notes.app.service.request.AsyncTaskResult.ErrorCode;
import pl.mslawin.notes.app.service.request.RequestExecutor;

/**
 * Created by mslawin on 1/11/16.
 */
public class GetTasksListTask extends AsyncTask<Long, Long, AsyncTaskResult<TasksList>> {

    private final Application application;

    public GetTasksListTask(Application application) {
        this.application = application;
    }

    @Override
    protected AsyncTaskResult<TasksList> doInBackground(Long... params) {
        try {
            TasksList taskList = new RequestExecutor(application).getTaskList(params[0]);
            return new AsyncTaskResult<>(taskList);
        } catch (UserNotAuthenticatedException e) {
            return new AsyncTaskResult<>(ErrorCode.NOT_AUTHENTICATED);
        } catch (TasksListException e) {
            return new AsyncTaskResult<>(ErrorCode.ERROR_GENERAL);
        } catch (NoNetworkConnectionException e) {
            return new AsyncTaskResult<>(ErrorCode.NO_NETWORK_CONNECTION);
        }
    }
}