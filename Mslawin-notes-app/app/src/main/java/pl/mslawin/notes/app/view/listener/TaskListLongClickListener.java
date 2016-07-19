package pl.mslawin.notes.app.view.listener;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.logging.Logger;

import pl.mslawin.notes.app.R;
import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.model.Task;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.TasksService;

/**
 * Created by mslawin on 10/18/15.
 */
public class TaskListLongClickListener implements AdapterView.OnItemLongClickListener {

    private static final Logger logger = Logger.getLogger(TaskListLongClickListener.class.getName());

    private final Resources resources;
    private final Context context;
    private final TasksService tasksService;
    private final TasksList tasksList;
    private final Application application;

    public TaskListLongClickListener(Resources resources, Context context, TasksList tasksList,
                                     Application application) {
        this.resources = resources;
        this.context = context;
        this.tasksList = tasksList;
        this.tasksService = new TasksService();
        this.application = application;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view;
        Task task = tasksList.getTasks().get(position);
        try {
            if (!task.isCompleted() && tasksService.completeTask(application, tasksList.getId(), task.getId())) {
                task.setCompleted(true);
                Bitmap tickImage = BitmapFactory.decodeResource(resources, R.drawable.tick);
                SpannableStringBuilder ssb = new SpannableStringBuilder(textView.getText());
                ssb.insert(0, " ");
                ssb.setSpan(new ImageSpan(context, tickImage), 0, 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                textView.setText(ssb);
                return true;
            }
        } catch (NoNetworkConnectionException e) {
            TasksService.handleNoNetworkException(logger, e, view.getContext());
        } catch (TasksListException e) {
            TasksService.handleTasksListException(logger, e, view.getContext());
        }
        return false;
    }
}