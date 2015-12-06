package pl.mslawin.notes.app.service.request;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.joda.time.LocalDateTime;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.model.Task;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.dto.model.ListDto;
import pl.mslawin.notes.dto.model.TaskDto;
import pl.mslawin.notes.dto.model.TaskListDto;
import pl.mslawin.notes.dto.request.AddTaskRequest;
import pl.mslawin.notes.dto.request.CompleteTaskRequest;

/**
 * Created by maciej on 10/11/15.
 */
public class RequestExecutor {

    private static final String BASE_URL = "http://192.168.1.12:9090/list";
    private static final Logger logger = Logger.getLogger(GetListsOfTasksList.class.getName());

    private static final Gson GSON = new Gson();

    public List<TasksList> getListOfTasksList(String email) {
        ListDto listDtoList = GSON.fromJson(executeGet("/getForUser?email=" + email), ListDto.class);
        return transformListDtoToTaskList(listDtoList);
    }

    public TasksList getTaskList(long id) {
        TaskListDto taskListDto = GSON.fromJson(executeGet("/" + id), TaskListDto.class);
        return transformTaskListDtoToTaskList(taskListDto);
    }

    public void completeTask(long tasksListId, long taskId) {
        executePost("/complete", new CompleteTaskRequest(tasksListId, taskId));
    }

    public Task createTask(long tasksListId, String taskValue, String user) {
        String response = executePost("/addTask", new AddTaskRequest(user, taskValue, tasksListId));
        TaskDto taskDto = GSON.fromJson(response, TaskDto.class);
        return transformTaskDtoToTask(taskDto);
    }

    private String executeGet(String requestUrl) {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(BASE_URL + requestUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream;
            inputStream = httpURLConnection.getInputStream();
            StringWriter stringWriter = new StringWriter();
            IOUtils.copy(inputStream, stringWriter);
            return stringWriter.toString();
        } catch (ConnectException e) {
            logger.log(Level.SEVERE, "Unable to create Internet connection");
            throw new TasksListException();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to connect to notes service", e);
            throw new TasksListException();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    private String executePost(String requestUrl, Object data) {
        HttpURLConnection urlConnection;
        try {
            URL url = new URL(BASE_URL + requestUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setRequestProperty("Accept", "*/*");

            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
            writer.write(GSON.toJson(data));
            writer.flush();

            StringWriter stringWriter = new StringWriter();
            IOUtils.copy(urlConnection.getInputStream(), stringWriter);
            return stringWriter.toString();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to connect to notes service");
            throw new TasksListException(e);
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
        tasksList.setName(taskListDto.getName());
        tasksList.setOwner(taskListDto.getOwner());
        List<Task> tasks = new ArrayList<>();
        for (TaskDto taskDto : taskListDto.getTasks()) {
            tasks.add(transformTaskDtoToTask(taskDto));
        }
        tasksList.setTasks(tasks);
        return tasksList;
    }

    private Task transformTaskDtoToTask(TaskDto taskDto) {
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setAuthor(taskDto.getAuthor());
        task.setCreationTime(LocalDateTime.fromDateFields(new Date(taskDto.getCreationDate())));
        task.setText(taskDto.getText());
        task.setCompleted(taskDto.isCompleted());
        return task;
    }
}