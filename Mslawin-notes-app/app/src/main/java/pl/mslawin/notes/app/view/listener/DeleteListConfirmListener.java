package pl.mslawin.notes.app.view.listener;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.logging.Logger;

import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.TasksService;

/**
 * Created by mslawin on 1/17/16.
 */
public class DeleteListConfirmListener implements DialogInterface.OnClickListener {

    private static final Logger logger = Logger.getLogger(DeleteListConfirmListener.class.getName());

    private final FragmentActivity activity;
    private final TasksService tasksService;
    private final List<TasksList> tasksLists;
    private final List<String> tasksListsNames;
    private final TasksList listToDelete;
    private final ArrayAdapter<String> adapter;

    public DeleteListConfirmListener(FragmentActivity activity, TasksService tasksService, TasksList listToDelete,
                                     List<TasksList> tasksLists, List<String> tasksListsNames, ArrayAdapter<String> adapter) {
        this.activity = activity;
        this.tasksService = tasksService;
        this.tasksLists = tasksLists;
        this.tasksListsNames = tasksListsNames;
        this.listToDelete = listToDelete;
        this.adapter = adapter;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        try {
            tasksService.deleteList(activity.getApplication(), listToDelete.getId());
            tasksLists.remove(listToDelete);
            tasksListsNames.remove(listToDelete.getName());
            adapter.notifyDataSetChanged();
        } catch (NoNetworkConnectionException e) {
            TasksService.handleNoNetworkException(logger, e, activity.getApplicationContext());
        } catch (TasksListException e) {
            TasksService.handleTasksListException(logger, e, activity.getApplicationContext());
        }
    }
}