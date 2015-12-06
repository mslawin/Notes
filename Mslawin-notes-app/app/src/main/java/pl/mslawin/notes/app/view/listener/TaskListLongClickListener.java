package pl.mslawin.notes.app.view.listener;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import pl.mslawin.notes.app.R;
import pl.mslawin.notes.app.model.Task;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.TasksService;

/**
 * Created by maciej on 10/18/15.
 */
public class TaskListLongClickListener implements AdapterView.OnItemLongClickListener {

    private final Resources resources;
    private final Context context;
    private final TasksService tasksService;
    private final TasksList tasksList;

    public TaskListLongClickListener(Resources resources, Context context, TasksList tasksList) {
        this.resources = resources;
        this.context = context;
        this.tasksList = tasksList;
        this.tasksService = new TasksService();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view;
        Task task = tasksList.getTasks().get(position);
        if (!task.isCompleted() && tasksService.completeTask(tasksList.getId(), task.getId())) {
            task.setCompleted(true);
            Bitmap tickImage = BitmapFactory.decodeResource(resources, R.drawable.tick);
            SpannableStringBuilder ssb = new SpannableStringBuilder(textView.getText());
            ssb.insert(0, " ");
            ssb.setSpan(new ImageSpan(context, tickImage), 0, 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            textView.setText(ssb);
            return true;
        }
        return false;
    }
}