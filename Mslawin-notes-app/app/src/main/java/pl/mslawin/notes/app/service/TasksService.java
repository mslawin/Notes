package pl.mslawin.notes.app.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import pl.mslawin.notes.app.exception.GetTasksListException;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.request.GetListsOfTasksList;
import pl.mslawin.notes.app.service.request.GetTasksList;

/**
 * Created by maciej on 10/11/15.
 */
public class TasksService {

    public List<TasksList> getListsForUser(String email) throws GetTasksListException {
        GetListsOfTasksList getListsOfTasksList = new GetListsOfTasksList();
        getListsOfTasksList.execute(email);
        try {
            return getListsOfTasksList.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new GetTasksListException(e);
        }
    }

    public TasksList getTaskList(String id) {
        GetTasksList getTasksList = new GetTasksList();
        getTasksList.execute(Long.valueOf(id));
        try {
            return getTasksList.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new GetTasksListException(e);
        }
    }
}