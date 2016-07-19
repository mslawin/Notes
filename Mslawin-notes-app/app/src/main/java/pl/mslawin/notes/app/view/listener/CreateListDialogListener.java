package pl.mslawin.notes.app.view.listener;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pl.mslawin.notes.app.R;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.TasksService;

/**
 * Created by mslawin on 1/17/16.
 */
public class CreateListDialogListener implements View.OnClickListener {

    private static final int TEXT_COLOR = Color.parseColor("#452222");
    private final FragmentActivity activity;
    private final TasksService tasksService;
    private final List<TasksList> tasksLists;
    private final List<String> tasksListNames;
    private final ArrayAdapter<String> adapter;

    public CreateListDialogListener(FragmentActivity activity, TasksService tasksService, List<TasksList> tasksLists,
                                    List<String> tasksListNames, ArrayAdapter<String> adapter) {
        this.activity = activity;
        this.tasksService = tasksService;
        this.tasksLists = tasksLists;
        this.tasksListNames = tasksListNames;
        this.adapter = adapter;
    }

    @Override
    public void onClick(View v) {
        TextView title = new TextView(v.getContext());
        title.setText(v.getResources().getString(R.string.tasksListCreateListDialogTitle));
        title.setGravity(Gravity.CENTER);
        title.setTextColor(TEXT_COLOR);
        title.setBackgroundColor(Color.LTGRAY);
        title.setTextSize(40);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        new AlertDialog.Builder(activity)
                .setCustomTitle(title)
                .setMessage(R.string.tasksListCreateListDialogMessage)
                .setView(R.layout.create_list_dialog)
                .setPositiveButton(R.string.ok, new CreateListConfirmListener(activity, tasksService, tasksListNames, tasksLists, adapter))
                .setNegativeButton(R.string.cancel, new DismissDialogListener())
                .create()
                .show();
    }
}