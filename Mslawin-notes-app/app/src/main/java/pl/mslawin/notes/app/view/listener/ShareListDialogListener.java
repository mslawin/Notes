package pl.mslawin.notes.app.view.listener;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import pl.mslawin.notes.app.R;

/**
 * Created by maciej on 10/31/15.
 */
public class ShareListDialogListener implements View.OnClickListener {

    private final FragmentActivity activity;

    public ShareListDialogListener(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        List<String> dummyList = getContacts();
        dummyList.add("xxx");
        dummyList.add("yyy");
        dummyList.add("zzz");
        View view = activity.getLayoutInflater().inflate(R.layout.share_task_dialog, null);
        AutoCompleteTextView shareListEmailPicker = (AutoCompleteTextView) view.findViewById(R.id.shareListPickEmail);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_expandable_list_item_1, dummyList);
        shareListEmailPicker.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.shareListDialogTitle)
                .setMessage(R.string.shareListDialogMessage)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();


        dialog.show();
    }

    private List<String> getContacts() {
        List<String> contacts = new ArrayList<>();
        ContentResolver contentResolver = activity.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                contacts.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            }
        }
        return contacts;
    }
}