package pl.mslawin.notes.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import pl.mslawin.notes.app.R;
import pl.mslawin.notes.app.TaskListApplication;
import pl.mslawin.notes.app.constants.NotesConstants;
import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.TasksService;
import pl.mslawin.notes.app.service.UserService;
import pl.mslawin.notes.app.view.listener.CreateListDialogListener;
import pl.mslawin.notes.app.view.listener.DeleteListDialogListener;
import pl.mslawin.notes.app.view.listener.LogoutButtonListener;

/**
 * A list fragment representing a list of TasksList. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link TaskDetailFragment}.
 * <p/>
 */
public class TaskListFragment extends Fragment {

    private static final Logger logger = Logger.getLogger(TaskListFragment.class.getName());
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private final TasksService tasksService = new TasksService();
    private final UserService userService = new UserService();
    private List<TasksList> tasksLists;
    private ListView listView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String usersEmail = getEmail();
        List<String> tasksListNames;
        try {
            tasksListNames = getTasksListNames(usersEmail);
        } catch (NoNetworkConnectionException e) {
            TasksService.handleNoNetworkException(logger, e, getContext());
            tasksListNames = Collections.emptyList();
        } catch (TasksListException e) {
            TasksService.handleTasksListException(logger, e, getContext());
            tasksListNames = Collections.emptyList();
        }
        if (listView == null) {
            listView = (ListView) getActivity().findViewById(R.id.taskListsView);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                tasksListNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                intent.putExtra(NotesConstants.TASKS_LIST_ID_PARAM, tasksLists.get(position).getId());
                intent.putExtra(NotesConstants.TASKS_LIST_NAME_PARAM, tasksLists.get(position).getName());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(
                new DeleteListDialogListener(getActivity(), tasksListNames, tasksService, tasksLists, adapter));
        getActivity().findViewById(R.id.createNewList)
                .setOnClickListener(new CreateListDialogListener(getActivity(), tasksService, tasksLists, tasksListNames, adapter));
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
        getActivity().findViewById(R.id.logoutButton)
                .setOnClickListener(new LogoutButtonListener(userService, getActivity().getApplication(), getActivity()));
    }

    private List<String> getTasksListNames(String usersEmail) throws TasksListException, NoNetworkConnectionException {
        List<String> result = new ArrayList<>();
        if (tasksLists == null) {
            tasksLists = tasksService.getListsForUser(getActivity().getApplication(), usersEmail);
        }
        for (TasksList tasksList : tasksLists) {
            result.add(tasksList.getName());
        }
        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            listView.setItemChecked(mActivatedPosition, false);
        } else {
            listView.setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    private String getEmail() {
        return ((TaskListApplication) getActivity().getApplication()).getUserAuthentication().getEmail();
    }
}