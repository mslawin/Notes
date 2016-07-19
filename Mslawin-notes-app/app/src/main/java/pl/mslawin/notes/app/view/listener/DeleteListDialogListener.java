package pl.mslawin.notes.app.view.listener;

import android.app.AlertDialog;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.List;

import pl.mslawin.notes.app.R;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.TasksService;

/**
 * Created by mslawin on 1/17/16.
 */
public class DeleteListDialogListener implements AdapterView.OnItemLongClickListener {

    private final FragmentActivity activity;
    private final List<String> tasksListNames;
    private final TasksService tasksService;
    private final List<TasksList> tasksLists;
    private final ArrayAdapter<String> adapter;

    public DeleteListDialogListener(FragmentActivity activity, List<String> tasksListNames, TasksService tasksService,
                                    List<TasksList> tasksLists, ArrayAdapter<String> adapter) {
        this.activity = activity;
        this.tasksListNames = tasksListNames;
        this.tasksService = tasksService;
        this.tasksLists = tasksLists;
        this.adapter = adapter;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        String title = activity.getResources().getString(R.string.tasksListDeleteListTitle)
                + " "
                + tasksListNames.get(position)
                + "?";
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setPositiveButton(R.string.tasksListConfirmDelete, new DeleteListConfirmListener(activity, tasksService,
                        tasksLists.get(position), tasksLists, tasksListNames, adapter))
                .setNegativeButton(R.string.cancel, new DismissDialogListener())
                .create()
                .show();
        return true;
    }
}