package pl.mslawin.notes.app.view.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.mslawin.notes.app.R;
import pl.mslawin.notes.app.model.Task;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.request.CreateTask;

/**
 * Created by maciej on 10/31/15.
 */
public class CreateTaskConfirmListener implements DialogInterface.OnClickListener {

    private static final Logger logger = Logger.getLogger(CreateTaskConfirmListener.class.getName());
    private final TasksList taskList;
    private final String user;
    private final List<Object> objectsList;
    private final ArrayAdapter<Object> taskListAdapter;

    public CreateTaskConfirmListener(TasksList taskList, String user, List<Object> objectsList,
                                     ArrayAdapter<Object> taskListAdapter) {
        this.taskList = taskList;
        this.user = user;
        this.objectsList = objectsList;
        this.taskListAdapter = taskListAdapter;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        EditText taskValueText = (EditText) ((AlertDialog) dialog).findViewById(R.id.createTaskDialogText);
        String taskValue = taskValueText.getText().toString();
        CreateTask createTask = new CreateTask(taskList.getId());
        createTask.execute(taskValue, user);
        try {
            Task task = createTask.get();
            if (task != null) {
                taskList.getTasks().add(task);
                objectsList.add(taskValue);
                taskListAdapter.notifyDataSetChanged();
                Toast.makeText(((AlertDialog) dialog).getContext(), R.string.createTaskSuccess, Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.log(Level.SEVERE, "Error creating new task for list: " + taskList.getId(), e);
        }
    }
}