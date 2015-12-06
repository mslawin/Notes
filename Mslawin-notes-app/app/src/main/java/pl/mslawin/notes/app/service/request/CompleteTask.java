package pl.mslawin.notes.app.service.request;

import android.os.AsyncTask;

import java.util.logging.Logger;

/**
 * Created by maciej on 10/18/15.
 */
public class CompleteTask extends AsyncTask<Long, Long, Long> {

    @Override
    protected Long doInBackground(Long... params) {
        RequestExecutor requestExecutor = new RequestExecutor();
        requestExecutor.completeTask(params[0], params[1]);
        return 1L;
    }
}
