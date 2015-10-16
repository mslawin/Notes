package pl.mslawin.notes.app.service.request;

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
public class RequestExecutor {

    private static final String BASE_URL = "http://192.168.1.12:9090/list";
    private static final Logger logger = Logger.getLogger(GetListsOfTasksList.class.getName());

    private static final Gson GSON = new Gson();

    public List<TasksList> getListOfTasksList(String email) {
        ListDto listDtoList = GSON.fromJson(execute("/getForUser?email=" + email), ListDto.class);
        return transformListDtoToTaskList(listDtoList);
    }

    public TasksList getTaskList(long id) {
        TaskListDto taskListDto = GSON.fromJson(execute("/" + id), TaskListDto.class);
        return transformTaskListDtoToTaskList(taskListDto);
    }

    private String execute(String requestUrl) {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(BASE_URL + requestUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream;
            inputStream = httpURLConnection.getInputStream();
            StringWriter stringWriter = new StringWriter();
            IOUtils.copy(inputStream, stringWriter);
            return stringWriter.toString();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to connect to notes service", e);
            throw new GetTasksListException();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    private List<TasksList> transformListDtoToTaskList(ListDto listDto) {
        List<TasksList> result = new ArrayList<>();
        for (TaskListDto taskListDto : listDto.getTaskListDtoList()) {
            TasksList tasksList = transformTaskListDtoToTaskList(taskListDto);
            result.add(tasksList);
        }
        return result;
    }

    private TasksList transformTaskListDtoToTaskList(TaskListDto taskListDto) {
        TasksList tasksList = new TasksList();
        tasksList.setId(taskListDto.getId());
        tasksList.setOwner(taskListDto.getOwner().getEmail());
        List<Task> tasks = new ArrayList<>();
        for (TaskDto taskDto : taskListDto.getTasks()) {
            Task task = transformTaskDtoToTask(taskDto);
            tasks.add(task);
        }
        tasksList.setTasks(tasks);
        return tasksList;
    }

    private Task transformTaskDtoToTask(TaskDto taskDto) {
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setAuthor(taskDto.getAuthor().getEmail());
        task.setCreationTime(LocalDateTime.fromDateFields(new Date(taskDto.getCreationDate())));
        task.setText(taskDto.getText());
        return task;
    }
}
