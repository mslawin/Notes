package pl.mslawin.notes.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import pl.mslawin.notes.app.R;
import pl.mslawin.notes.app.constants.NotesConstants;
import pl.mslawin.notes.app.model.TasksList;


/**
 * An activity representing a list of TasksList. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TaskDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link TaskListFragment} and the item details
 * (if present) is a {@link TaskDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link TaskListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class TaskListActivity extends FragmentActivity implements TaskListFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        if (findViewById(R.id.task_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            TaskListFragment taskListFragment =
                    ((TaskListFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.task_list));
            taskListFragment.setActivateOnItemClick(true);
        }
    }

    /**
     * Callback method from {@link TaskListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(TasksList tasksList, String email) {
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra(NotesConstants.TASKS_PARAM, tasksList);
        intent.putExtra(NotesConstants.USER_EMAIL_PARAM, email);
        startActivity(intent);
    }
}