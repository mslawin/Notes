package pl.mslawin.notes.app.service.request;

import android.os.AsyncTask;

import pl.mslawin.notes.app.model.TasksList;

/**
 * Created by maciej on 10/11/15.
 */
public class GetTasksList extends AsyncTask<Long, Long, TasksList> {

    @Override
    protected TasksList doInBackground(Long... params) {
        RequestExecutor requestExecutor = new RequestExecutor();
        return requestExecutor.getTaskList(params[0]);
    }
}