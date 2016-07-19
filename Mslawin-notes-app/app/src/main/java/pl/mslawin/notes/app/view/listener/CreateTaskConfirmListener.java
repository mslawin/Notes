package pl.mslawin.notes.app.view.listener;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.logging.Logger;

import pl.mslawin.notes.app.R;
import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.model.Task;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.TasksService;

/**
 * Created by maciej on 10/31/15.
 */
public class CreateTaskConfirmListener implements DialogInterface.OnClickListener {

    private static final Logger logger = Logger.getLogger(CreateTaskConfirmListener.class.getName());
    private final TasksList taskList;
    private final List<Object> objectsList;
    private final ArrayAdapter<Object> taskListAdapter;
    private final Application application;
    private final TasksService tasksService;

    public CreateTaskConfirmListener(TasksList taskList, List<Object> objectsList, ArrayAdapter<Object> taskListAdapter,
                                     Application application, TasksService tasksService) {
        this.taskList = taskList;
        this.objectsList = objectsList;
        this.taskListAdapter = taskListAdapter;
        this.application = application;
        this.tasksService = tasksService;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        EditText taskValueText = (EditText) ((AlertDialog) dialog).findViewById(R.id.createTaskDialogText);
        if (taskValueText.getText() == null || taskValueText.getText().length() == 0) {
            return;
        }
        String taskValue = taskValueText.getText().toString();
        try {
            Task task = tasksService.createTask(application, taskValue, taskList.getId());
            if (task != null) {
                taskList.getTasks().add(task);
                objectsList.add(taskValue);
                taskListAdapter.notifyDataSetChanged();
                Toast.makeText(((AlertDialog) dialog).getContext(), R.string.createTaskSuccess, Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (NoNetworkConnectionException e) {
            TasksService.handleNoNetworkException(logger, e, application.getApplicationContext());
        } catch (TasksListException e) {
            TasksService.handleTasksListException(logger, e, application.getApplicationContext());
        }
    }
}