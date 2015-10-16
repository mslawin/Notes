package pl.mslawin.notes.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;

import pl.mslawin.notes.app.model.Task;
import pl.mslawin.notes.app.model.TasksList;

import static pl.mslawin.notes.app.constants.NotesConstants.TASKS_PARAM;

/**
 * A fragment representing a single Task detail screen.
 * This fragment is either contained in a {@link TaskListActivity}
 * in two-pane mode (on tablets) or a {@link TaskDetailActivity}
 * on handsets.
 */
public class TaskDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private TasksList tasksList;

    private ListView listView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(TASKS_PARAM)) {
            tasksList = (TasksList) getArguments().getSerializable(TASKS_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_detail, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (listView == null) {
            listView = ((ListView) getView().findViewById(R.id.task_detail));
        }
        // Show the dummy content as text in a TextView.
        if (tasksList != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getView().getContext(),
                    R.layout.activity_task_detail_text, new ArrayList<>(getTransformer()));
            listView.setAdapter(adapter);
        }
    }

    private Collection<String> getTransformer() {
        return Collections2.transform(tasksList.getTasks(), new Function<Task, String>() {
            @Override
            public String apply(Task input) {
                return input.getAuthor() + ": " + input.getText();
            }
        });
    }
}