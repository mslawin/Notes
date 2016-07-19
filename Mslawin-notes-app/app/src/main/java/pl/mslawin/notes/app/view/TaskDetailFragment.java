package pl.mslawin.notes.app.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import pl.mslawin.notes.app.R;
import pl.mslawin.notes.app.TaskListApplication;
import pl.mslawin.notes.app.constants.NotesConstants;
import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.model.Task;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.TasksService;
import pl.mslawin.notes.app.view.adapter.TaskListAdapter;
import pl.mslawin.notes.app.view.listener.CreateTaskConfirmListener;
import pl.mslawin.notes.app.view.listener.CreateTaskDialogListener;
import pl.mslawin.notes.app.view.listener.DismissDialogListener;
import pl.mslawin.notes.app.view.listener.ShareListDialogListener;
import pl.mslawin.notes.app.view.listener.TaskListLongClickListener;

/**
 * A fragment representing a single Task detail screen.
 * This fragment is either contained in a {@link TaskListActivity}
 * in two-pane mode (on tablets) or a {@link TaskDetailActivity}
 * on handsets.
 */
public class TaskDetailFragment extends Fragment {

    private static final Logger logger = Logger.getLogger(TaskDetailFragment.class.getName());

    private final TasksService tasksService = new TasksService();

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
        long tasksListId = getArguments().getLong(NotesConstants.TASKS_LIST_ID_PARAM);
        try {
            this.tasksList = tasksService.getTaskList(getActivity().getApplication(), tasksListId);
        } catch (NoNetworkConnectionException e) {
            TasksService.handleNoNetworkException(logger, e, getContext());
            this.tasksList = new TasksList();
        } catch (TasksListException e) {
            TasksService.handleTasksListException(logger, e, getContext());
            this.tasksList = new TasksList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                R.layout.activity_task_detail_text, objectList, tasksList, getEmail(), getResources());
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new TaskListLongClickListener(getResources(),
                getActivity().getApplicationContext(), tasksList, getActivity().getApplication()));
        handleCreateTask(objectList, adapter);
        handleShareList();
        handleSharedWith();
    }

    private void handleCreateTask(List<Object> objectList, ArrayAdapter<Object> adapter) {
        CreateTaskConfirmListener createTaskConfirmListener = new CreateTaskConfirmListener(tasksList,
                objectList, adapter, getActivity().getApplication(), tasksService);
        getActivity().findViewById(R.id.createTaskButton)
                .setOnClickListener(new CreateTaskDialogListener(getActivity(), createTaskConfirmListener));
    }

    private String getEmail() {
        return ((TaskListApplication) getActivity().getApplication()).getUserAuthentication().getEmail();
    }

    private void handleShareList() {
        ShareListDialogListener shareListDialogListener = new ShareListDialogListener(tasksList, getActivity());
        getActivity().findViewById(R.id.shareButton)
                .setOnClickListener(shareListDialogListener);
    }

    private void handleSharedWith() {
        getActivity().findViewById(R.id.sharedWithButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collection<String> sharedWith = tasksList.getSharedWith();
                sharedWith.add(tasksList.getOwner());
                sharedWith.remove(getEmail());
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.listDetailsSharedWithTitle)
                        .setItems(sharedWith.toArray(new String[sharedWith.size()]), new DismissDialogListener())
                        .setCancelable(true)
                        .create()
                        .show();
            }
        });
    }

    private List<Object> convertToObjects(List<Task> tasks) {
        List<Object> objectList = new ArrayList<>();
        for (Task task : tasks) {
            objectList.add(task);
        }
        return objectList;
    }
}