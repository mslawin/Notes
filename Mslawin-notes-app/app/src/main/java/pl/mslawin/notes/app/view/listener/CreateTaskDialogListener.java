package pl.mslawin.notes.app.view.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import pl.mslawin.notes.app.R;

/**
 * Created by maciej on 10/31/15.
 */
public class CreateTaskDialogListener implements View.OnClickListener {

    private final FragmentActivity activity;
    private final CreateTaskConfirmListener createTaskConfirmListener;

    public CreateTaskDialogListener(FragmentActivity activity,
                                    CreateTaskConfirmListener createTaskConfirmListener) {
        this.activity = activity;
        this.createTaskConfirmListener = createTaskConfirmListener;
    }

    @Override
    public void onClick(View v) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.createTaskDialogTitle)
                .setMessage(R.string.createTaskDialogMessage)
                .setView(R.layout.create_task_dialog)
                .setPositiveButton(R.string.ok, createTaskConfirmListener)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
}