package pl.mslawin.notes.app.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.mslawin.notes.app.R;
import pl.mslawin.notes.app.model.Task;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.view.adapter.TaskListAdapter;
import pl.mslawin.notes.app.view.listener.CreateTaskConfirmListener;
import pl.mslawin.notes.app.view.listener.CreateTaskDialogListener;
import pl.mslawin.notes.app.view.listener.ShareListDialogListener;
import pl.mslawin.notes.app.view.listener.TaskListLongClickListener;

import static pl.mslawin.notes.app.constants.NotesConstants.TASKS_PARAM;
import static pl.mslawin.notes.app.constants.NotesConstants.USER_EMAIL_PARAM;

/**
 * A fragment representing a single Task detail screen.
 * This fragment is either contained in a {@link TaskListActivity}
 * in two-pane mode (on tablets) or a {@link TaskDetailActivity}
 * on handsets.
 */
public class TaskDetailFragment extends Fragment {

    private TasksList tasksList;

    private ListView listView;

    private String userEmail;

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
            userEmail = getArguments().getString(USER_EMAIL_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (listView == null) {
            listView = ((ListView) getActivity().findViewById(R.id.task_detail));
        }
        List<Task> tasks = tasksList.getTasks();
        Collections.sort(tasks);
        List<Object> objectList = convertToObjects(tasks);
        ArrayAdapter<Object> adapter = new TaskListAdapter(getActivity().getApplicationContext(),
                R.layout.activity_task_detail_text, objectList, tasksList, userEmail, getResources());

        new ArrayAdapter<Object>(getActivity().getApplicationContext(),
                R.layout.activity_task_detail_text, objectList) {
        };
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new TaskListLongClickListener(getResources(),
                getActivity().getApplicationContext(), tasksList));
        handleCreateTask(objectList, adapter);
        handleShareList();
    }

    private void handleCreateTask(List<Object> objectList, ArrayAdapter<Object> adapter) {
        CreateTaskConfirmListener createTaskConfirmListener = new CreateTaskConfirmListener(tasksList,
                userEmail, objectList, adapter);
        getActivity().findViewById(R.id.createTaskButton)
                .setOnClickListener(new CreateTaskDialogListener(getActivity(), createTaskConfirmListener));
    }

    private void handleShareList() {
        ShareListDialogListener shareListDialogListener = new ShareListDialogListener(getActivity());
        getActivity().findViewById(R.id.shareButton)
                .setOnClickListener(shareListDialogListener);
    }

    private List<Object> convertToObjects(List<Task> tasks) {
        List<Object> objectList = new ArrayList<>();
        for (Task task : tasks) {
            objectList.add(task);
        }
        return objectList;
    }
}