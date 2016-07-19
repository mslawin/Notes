package pl.mslawin.notes.app.service.request.task;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.exception.UserNotAuthenticatedException;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.request.AsyncTaskResult;
import pl.mslawin.notes.app.service.request.RequestExecutor;

import static pl.mslawin.notes.app.service.request.AsyncTaskResult.*;

/**
 * Created by mslawin on 10/11/15.
 */
public class GetListsOfTasksList extends AsyncTask<String, Long, AsyncTaskResult<List<TasksList>>> {

    private final Application application;

    public GetListsOfTasksList(Application application) {
        this.application = application;
    }

    @Override
    protected AsyncTaskResult<List<TasksList>> doInBackground(String... params) {
        RequestExecutor executor = new RequestExecutor(application);
        try {
            List<TasksList> listOfTasksList = executor.getListOfTasksList();
            return new AsyncTaskResult<>(listOfTasksList);
        } catch (UserNotAuthenticatedException e) {
            return new AsyncTaskResult<>(ErrorCode.NOT_AUTHENTICATED);
        } catch (TasksListException e) {
            return new AsyncTaskResult<>(ErrorCode.ERROR_GENERAL);
        } catch (NoNetworkConnectionException e) {
            return new AsyncTaskResult<>(ErrorCode.NO_NETWORK_CONNECTION);
        }
    }
}