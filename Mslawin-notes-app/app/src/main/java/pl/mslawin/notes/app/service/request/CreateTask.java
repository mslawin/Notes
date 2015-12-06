package pl.mslawin.notes.app.service.request;

import android.os.AsyncTask;

import pl.mslawin.notes.app.model.Task;

/**
 * Created by maciej on 10/31/15.
 */
public class CreateTask extends AsyncTask<String, Long, Task> {

    private final Long taskListId;

    public CreateTask(Long taskListId) {
        this.taskListId = taskListId;
    }

    @Override
    protected Task doInBackground(String... params) {
        RequestExecutor requestExecutor = new RequestExecutor();
        return requestExecutor.createTask(taskListId, params[0], params[1]);
    }
}