package pl.mslawin.notes.app.view.listener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.logging.Logger;

import pl.mslawin.notes.app.R;
import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.TasksService;

/**
 * Created by mslawin on 1/17/16.
 */
public class CreateListConfirmListener implements DialogInterface.OnClickListener {

    private static final Logger logger = Logger.getLogger(CreateListConfirmListener.class.getName());

    private final Activity activity;
    private final TasksService tasksService;
    private final List<String> tasksListNames;
    private final List<TasksList> tasksLists;
    private final ArrayAdapter<String> adapter;

    public CreateListConfirmListener(Activity activity, TasksService tasksService, List<String> tasksListNames,
                                     List<TasksList> tasksLists, ArrayAdapter<String> adapter) {
        this.activity = activity;
        this.tasksService = tasksService;
        this.tasksListNames = tasksListNames;
        this.tasksLists = tasksLists;
        this.adapter = adapter;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        EditText editText = (EditText) ((AlertDialog)dialog).findViewById(R.id.createListDialogText);
        TasksList tasksList = null;
        try {
            tasksList = tasksService.createList(activity.getApplication(), editText.getText().toString());
        } catch (NoNetworkConnectionException e) {
            TasksService.handleNoNetworkException(logger, e, ((AlertDialog) dialog).getContext());
        } catch (TasksListException e) {
            TasksService.handleTasksListException(logger, e, ((AlertDialog) dialog).getContext());
        }
        if (tasksList != null) {
            tasksListNames.add(tasksList.getName());
            tasksLists.add(tasksList);
            adapter.notifyDataSetChanged();
            Toast.makeText(activity, R.string.createTaskSuccess, Toast.LENGTH_SHORT).show();
        }
    }
}