package pl.mslawin.notes.app.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import pl.mslawin.notes.app.R;


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
 * to listen for item selections.
 */
public class TaskListActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        if (savedInstanceState == null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            TaskListFragment taskListFragment = new TaskListFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.task_list, taskListFragment)
                    .commit();
        }
    }
}