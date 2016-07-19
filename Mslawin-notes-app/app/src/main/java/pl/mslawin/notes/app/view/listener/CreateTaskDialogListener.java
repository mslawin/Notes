package pl.mslawin.notes.app.view.listener;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import pl.mslawin.notes.app.R;

/**
 * Created by maciej on 10/31/15.
 */
public class CreateTaskDialogListener implements View.OnClickListener {

    private static final int TEXT_COLOR = Color.parseColor("#452222");

    private final FragmentActivity activity;
    private final CreateTaskConfirmListener createTaskConfirmListener;

    public CreateTaskDialogListener(FragmentActivity activity,
                                    CreateTaskConfirmListener createTaskConfirmListener) {
        this.activity = activity;
        this.createTaskConfirmListener = createTaskConfirmListener;
    }

    @Override
    public void onClick(View v) {
        TextView title = new TextView(v.getContext());
        title.setText(v.getResources().getString(R.string.createTaskDialogTitle));
        title.setGravity(Gravity.CENTER);
        title.setTextColor(TEXT_COLOR);
        title.setBackgroundColor(Color.LTGRAY);
        title.setTextSize(40);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        new AlertDialog.Builder(activity)
                .setCustomTitle(title)
                .setMessage(R.string.createTaskDialogMessage)
                .setView(R.layout.create_task_dialog)
                .setPositiveButton(R.string.ok, createTaskConfirmListener)
                .setNegativeButton(R.string.cancel, new DismissDialogListener())
                .create()
                .show();
    }
}