package pl.mslawin.notes.app.service;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.mslawin.notes.app.R;
import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.model.Task;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.request.AsyncTaskResult;
import pl.mslawin.notes.app.service.request.task.CompleteTask;
import pl.mslawin.notes.app.service.request.task.CreateListTask;
import pl.mslawin.notes.app.service.request.task.CreateTask;
import pl.mslawin.notes.app.service.request.task.DeleteListTask;
import pl.mslawin.notes.app.service.request.task.GetListsOfTasksList;
import pl.mslawin.notes.app.service.request.task.GetTasksListTask;
import pl.mslawin.notes.app.service.request.task.ShareListTask;
import pl.mslawin.notes.app.view.GoogleSignInActivity;

/**
 * Created by mslawin on 10/11/15.
 */
public class TasksService {

    public List<TasksList> getListsForUser(Application application, String email) throws TasksListException, NoNetworkConnectionException {
        GetListsOfTasksList getListsOfTasksList = new GetListsOfTasksList(application);
        getListsOfTasksList.execute(email);
        try {
            AsyncTaskResult<List<TasksList>> result = getListsOfTasksList.get();
            return handleResult(application, result);
        } catch (InterruptedException | ExecutionException e) {
            throw new TasksListException(e);
        }
    }

    public TasksList createList(Application application, String name) throws TasksListException, NoNetworkConnectionException {
        CreateListTask createListTask = new CreateListTask(application);
        createListTask.execute(name);
        try {
            return handleResult(application, createListTask.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new TasksListException(e);
        }
    }

    public Task createTask(Application application, String taskValue, long listId) throws TasksListException, NoNetworkConnectionException {
        CreateTask createTask = new CreateTask(listId, application);
        createTask.execute(taskValue);
        try {
            return handleResult(application, createTask.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new TasksListException(e);
        }
    }

    public boolean completeTask(Application application, long tasksListId, long taskId) throws TasksListException, NoNetworkConnectionException {
        try {
            CompleteTask completeTask = new CompleteTask(application);
            completeTask.execute(tasksListId, taskId);
            AsyncTaskResult<Boolean> result = completeTask.get();
            return handleResult(application, result);
        } catch (InterruptedException | ExecutionException e) {
            throw new TasksListException(e);
        }
    }

    public TasksList getTaskList(Application application, long tasksListId) throws TasksListException, NoNetworkConnectionException {
        GetTasksListTask getTasksListTask = new GetTasksListTask(application);
        getTasksListTask.execute(tasksListId);
        try {
            AsyncTaskResult<TasksList> result = getTasksListTask.get();
            return handleResult(application, result);
        } catch (InterruptedException | ExecutionException e) {
            throw new TasksListException();
        }
    }

    public void shareList(Application application, long tasksListId, String shareWith) throws TasksListException, NoNetworkConnectionException {
        ShareListTask shareListTask = new ShareListTask(application);
        shareListTask.execute(Long.toString(tasksListId), shareWith);
        try {
            AsyncTaskResult<Boolean> result = shareListTask.get();
            handleResult(application, result);
        } catch (InterruptedException | ExecutionException e) {
            throw new TasksListException(e);
        }
    }

    public void deleteList(Application application, long tasksListId) throws TasksListException, NoNetworkConnectionException {
        DeleteListTask deleteListTask = new DeleteListTask(application);
        deleteListTask.execute(tasksListId);
        try {
            AsyncTaskResult<Boolean> result = deleteListTask.get();
            handleResult(application, result);
        } catch (InterruptedException | ExecutionException e) {
            throw new TasksListException(e);
        }
    }

    public static <T> T handleResult(Application application, AsyncTaskResult<T> result) throws NoNetworkConnectionException, TasksListException {
        if (result.isSuccess()) {
            return result.getResult();
        }
        if (result.getErrorCode() == AsyncTaskResult.ErrorCode.NOT_AUTHENTICATED) {
            application.startActivity(new Intent(application.getApplicationContext(), GoogleSignInActivity.class));
        }
        if (result.getErrorCode() == AsyncTaskResult.ErrorCode.NO_NETWORK_CONNECTION) {
            throw new NoNetworkConnectionException();
        }
        if (result.getErrorCode() == AsyncTaskResult.ErrorCode.ERROR_GENERAL) {
            throw new TasksListException();
        }
        return null;
    }

    public static void handleNoNetworkException(Logger logger, NoNetworkConnectionException e, Context context) {
        logger.log(Level.SEVERE, "No network connection", e);
        Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
    }

    public static void handleTasksListException(Logger logger, TasksListException e, Context context) {
        logger.log(Level.SEVERE, "General exception occured", e);
        Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
    }
}