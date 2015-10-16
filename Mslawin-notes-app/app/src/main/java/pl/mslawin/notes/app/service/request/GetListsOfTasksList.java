package pl.mslawin.notes.app.service.request;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.joda.time.LocalDateTime;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.mslawin.notes.app.exception.GetTasksListException;
import pl.mslawin.notes.app.model.Task;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.dto.ListDto;
import pl.mslawin.notes.dto.TaskDto;
import pl.mslawin.notes.dto.TaskListDto;

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