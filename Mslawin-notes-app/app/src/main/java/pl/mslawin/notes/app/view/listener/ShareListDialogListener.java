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
import android.widget.Toast;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import pl.mslawin.notes.app.R;
import pl.mslawin.notes.app.TaskListApplication;
import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.TasksService;
import pl.mslawin.notes.app.service.UserService;

/**
 * Created by mslawin on 10/31/15.
 */
public class ShareListDialogListener implements View.OnClickListener {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Logger logger = Logger.getLogger(ShareListDialogListener.class.getName());

    private final TasksList taskList;
    private final FragmentActivity activity;
    private final Pattern pattern;
    private final UserService userService;
    private final TasksService tasksService;

    public ShareListDialogListener(TasksList taskList, FragmentActivity activity) {
        this.taskList = taskList;
        this.activity = activity;
        this.pattern = Pattern.compile(EMAIL_PATTERN);
        this.userService = new UserService();
        this.tasksService = new TasksService();
    }

    @Override
    public void onClick(View v) {
        List<String> contacts = new ArrayList<>();
        try {
            contacts.addAll(userService.getConnectedUsers(activity.getApplication()));
        } catch (NoNetworkConnectionException e) {
            TasksService.handleNoNetworkException(logger, e, v.getContext());
        } catch (TasksListException e) {
            TasksService.handleTasksListException(logger, e, v.getContext());
        }
        contacts.addAll(getContacts());
        View view = activity.getLayoutInflater().inflate(R.layout.share_task_dialog, null);
        final AutoCompleteTextView shareListEmailPicker = (AutoCompleteTextView) view.findViewById(R.id.shareListPickEmail);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_expandable_list_item_1, contacts);
        shareListEmailPicker.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.shareListDialogTitle)
                .setMessage(R.string.shareListDialogMessage)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String shareWith = shareListEmailPicker.getText().toString();
                        String user = ((TaskListApplication) activity.getApplication()).getUserAuthentication().getEmail();
                        if (!isEmail(shareWith)) {
                            showMessage(R.string.shareListIncorrectEmail);
                        } else if (shareWith.equalsIgnoreCase(user)) {
                            showMessage(R.string.shareListCantShareWithUser);
                        } else if (isAlreadySharedWith(shareWith)) {
                            showMessage(R.string.shareListAlreadySharedWith);
                        } else {
                            try {
                                tasksService.shareList(activity.getApplication(), taskList.getId(), shareWith);
                                showMessage(R.string.shareListSuccess);
                                taskList.getSharedWith().add(shareWith);
                            } catch (NoNetworkConnectionException e) {
                                TasksService.handleNoNetworkException(logger, e, activity.getApplicationContext());
                            } catch (TasksListException e) {
                                TasksService.handleTasksListException(logger, e, activity.getApplicationContext());
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DismissDialogListener())
                .create();
        dialog.show();
    }

    private boolean isAlreadySharedWith(String shareWith) {
        for (String sharedWith : taskList.getSharedWith()) {
            if (sharedWith.equalsIgnoreCase(shareWith)) {
                return true;
            }
        }
        return taskList.getOwner().equalsIgnoreCase(shareWith);
    }

    private void showMessage(int message) {
        Toast.makeText(activity.getApplicationContext(), activity.getResources().getText(message), Toast.LENGTH_LONG).show();
    }

    private List<String> getContacts() {
        List<String> contacts = new ArrayList<>();
        ContentResolver contentResolver = activity.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                contacts.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return Lists.newArrayList(Collections2.filter(contacts, getEmailFilter()));
    }

    private Predicate<String> getEmailFilter() {
        return new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return isEmail(input);
            }
        };
    }

    private boolean isEmail(String input) {
        return input != null && pattern.matcher(input).matches();
    }
}