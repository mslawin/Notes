package pl.mslawin.notes.app.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import pl.mslawin.notes.app.R;
import pl.mslawin.notes.app.model.Task;
import pl.mslawin.notes.app.model.TasksList;

/**
 * Created by maciej on 10/18/15.
 */
public class TaskListAdapter extends ArrayAdapter<Object>{

    private static final String NOTE_TEXT_HTML_TEMPLATE = "<h2>%s</h2>";
    private static final String NOTE_AUTHOR_HTML_TEMPLATE = "%s<br/>%s";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("dd MMM yyyy, HH:mm");

    private final TasksList tasksList;
    private final String userEmail;
    private final Resources resources;

    public TaskListAdapter(Context context, int resource, List<Object> objects, TasksList tasksList, String userEmail, Resources resources) {
        super(context, resource, objects);
        this.tasksList = tasksList;
        this.userEmail = userEmail;
        this.resources = resources;
    }

    @Override
    public Object getItem(int position) {
        Task task = tasksList.getTasks().get(position);
        String textHtml = String.format(NOTE_TEXT_HTML_TEMPLATE, task.getText());
        String authorHtml = String.format(NOTE_AUTHOR_HTML_TEMPLATE, task.getAuthor(),
                DATE_TIME_FORMATTER.print(task.getCreationTime()));
        Spanned textSpanned = Html.fromHtml(textHtml);
        Spanned authorSpanned = Html.fromHtml(authorHtml);
        SpannableString taskHtmlText = new SpannableString(Html.fromHtml(textHtml + authorHtml));
        taskHtmlText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE), textSpanned.length(),
                textSpanned.length() + authorSpanned.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return taskHtmlText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view1 = super.getView(position, convertView, parent);
        if (tasksList.getTasks().get(position).getAuthor().equals(userEmail)) {
            view1.setBackgroundColor(Color.parseColor("#C2C2FF"));
        }
        if (tasksList.getTasks().get(position).isCompleted()) {
            TextView textView = (TextView) view1;
            Bitmap tickImage = BitmapFactory.decodeResource(resources, R.drawable.tick);

            SpannableStringBuilder ssb = new SpannableStringBuilder(textView.getText());
            ssb.insert(0, " ");
            ssb.setSpan(new ImageSpan(getContext(), tickImage), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            textView.setText(ssb);
        }
        return view1;
    }
}