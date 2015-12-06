package pl.mslawin.notes.app.service.request;

import android.os.AsyncTask;

import java.util.List;

import pl.mslawin.notes.app.model.TasksList;

/**
 * Created by maciej on 10/11/15.
 */
public class GetListsOfTasksList extends AsyncTask<String, Long, List<TasksList>> {

    @Override
    protected List<TasksList> doInBackground(String... params) {
        RequestExecutor executor = new RequestExecutor();
        return executor.getListOfTasksList(params[0]);
    }
}