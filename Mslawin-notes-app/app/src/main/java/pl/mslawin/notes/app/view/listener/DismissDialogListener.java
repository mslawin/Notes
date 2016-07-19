package pl.mslawin.notes.app.view.listener;

import android.content.DialogInterface;

/**
 * Created by mslawin on 1/17/16.
 */
public class DismissDialogListener implements DialogInterface.OnClickListener {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
}