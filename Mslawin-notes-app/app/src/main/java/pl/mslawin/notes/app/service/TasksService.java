package pl.mslawin.notes.app.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.request.CompleteTask;
import pl.mslawin.notes.app.service.request.GetListsOfTasksList;
import pl.mslawin.notes.app.service.request.GetTasksList;

/**
 * Created by maciej on 10/11/15.
 */
public class TasksService {

    public List<TasksList> getListsForUser(String email) throws TasksListException {
        GetListsOfTasksList getListsOfTasksList = new GetListsOfTasksList();
        getListsOfTasksList.execute(email);
        try {
            return getListsOfTasksList.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new TasksListException(e);
        }
    }

    public TasksList getTaskList(Long id) {
        GetTasksList getTasksList = new GetTasksList();
        getTasksList.execute(id);
        try {
            return getTasksList.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new TasksListException(e);
        }
    }

    public boolean completeTask(long tasksListId, long taskId) {
        try {
            CompleteTask completeTask = new CompleteTask();
            completeTask.execute(tasksListId, taskId);
            return true;
        } catch (TasksListException e) {
            return false;
        }
    }
}