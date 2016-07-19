package pl.mslawin.notes.app.service.request;

import android.app.Application;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.joda.time.LocalDateTime;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.mslawin.notes.app.TaskListApplication;
import pl.mslawin.notes.app.exception.NoNetworkConnectionException;
import pl.mslawin.notes.app.exception.TasksListException;
import pl.mslawin.notes.app.exception.UserNotAuthenticatedException;
import pl.mslawin.notes.app.model.Task;
import pl.mslawin.notes.app.model.TasksList;
import pl.mslawin.notes.app.service.request.task.GetListsOfTasksList;
import pl.mslawin.notes.app.service.user.UserAuthentication;
import pl.mslawin.notes.dto.model.ListDto;
import pl.mslawin.notes.dto.model.TaskDto;
import pl.mslawin.notes.dto.model.TaskListDto;
import pl.mslawin.notes.dto.model.UsersListDto;
import pl.mslawin.notes.dto.request.AddTaskRequest;
import pl.mslawin.notes.dto.request.CompleteTaskRequest;
import pl.mslawin.notes.dto.request.CreateListRequest;
import pl.mslawin.notes.dto.request.DeleteListRequest;
import pl.mslawin.notes.dto.request.LoginRequest;
import pl.mslawin.notes.dto.request.ShareListRequest;

/**
 * Created by mslawin on 10/11/15.
 */
public class RequestExecutor {

    private static final String BASE_URL = "https://mnotes.herokuapp.com";
    private static final String BASE_LIST_URL = "/list";
    private static final String BASE_USER_URL = "/user";
    private static final Logger logger = Logger.getLogger(GetListsOfTasksList.class.getName());

    private static final Gson GSON = new Gson();

    private final Application application;

    public RequestExecutor(Application application) {
        this.application = application;
    }

    public List<TasksList> getListOfTasksList() throws TasksListException, NoNetworkConnectionException {
        ListDto listDtoList = GSON.fromJson(executeGet(BASE_LIST_URL + "/getForUser"), ListDto.class);
        return transformListDtoToTaskList(listDtoList);
    }

    public TasksList createNewList(String name) throws TasksListException, NoNetworkConnectionException {
        String response = executeUpdate(BASE_LIST_URL + "/create", new CreateListRequest(name), "PUT");
        return transformTaskListDtoToTaskList(GSON.fromJson(response, TaskListDto.class));
    }

    public void deleteList(long id) throws TasksListException, NoNetworkConnectionException {
        executeUpdate(BASE_LIST_URL + "/delete", new DeleteListRequest(id), "DELETE");
    }

    public TasksList getTaskList(long id) throws TasksListException, NoNetworkConnectionException {
        TaskListDto taskListDto = GSON.fromJson(executeGet(BASE_LIST_URL + "/" + id), TaskListDto.class);
        return transformTaskListDtoToTaskList(taskListDto);
    }

    public void completeTask(long tasksListId, long taskId) throws TasksListException, NoNetworkConnectionException {
        executeUpdate(BASE_LIST_URL + "/complete", new CompleteTaskRequest(tasksListId, taskId), "POST");
    }

    public Task createTask(long tasksListId, String taskValue) throws TasksListException, NoNetworkConnectionException {
        String response = executeUpdate(BASE_LIST_URL + "/addTask", new AddTaskRequest(taskValue, tasksListId), "POST");
        TaskDto taskDto = GSON.fromJson(response, TaskDto.class);
        return transformTaskDtoToTask(taskDto);
    }

    public Collection<String> getConnectedUsers() throws TasksListException, NoNetworkConnectionException {
        UsersListDto usersListDto = GSON.fromJson(executeGet(BASE_USER_URL + "/connections"), UsersListDto.class);
        return usersListDto.getUsers();
    }

    public void shareList(long listId, String shareWith) throws TasksListException, NoNetworkConnectionException {
        executeUpdate(BASE_LIST_URL + "/share", new ShareListRequest(listId, shareWith), "POST");
    }

    public UserAuthentication authenticateUser(String loginToken) throws TasksListException, NoNetworkConnectionException {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = prepareHttpURLConnection(BASE_USER_URL + "/login", new LoginRequest(loginToken), "POST");
            String headerField = httpURLConnection.getHeaderField("Set-Cookie");
            String jsessionid = null;
            for (HttpCookie httpCookie : HttpCookie.parse(headerField)) {
                if ("JSESSIONID".equals(httpCookie.getName())) {
                    jsessionid = httpCookie.getValue();
                    break;
                }
            }
            StringWriter stringWriter = new StringWriter();
            IOUtils.copy(httpURLConnection.getInputStream(), stringWriter);
            return new UserAuthentication(stringWriter.toString(), jsessionid);
        } catch (ConnectException e) {
            throw new NoNetworkConnectionException();
        } catch (IOException e) {
            throw new TasksListException();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    public void logoutUser() throws TasksListException, NoNetworkConnectionException {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = prepareHttpURLConnection(BASE_USER_URL + "/logout", null, "POST");
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new TasksListException("Unable to logout user");
            }
            ((TaskListApplication) application).removeUserAuthentication();
        } catch (ConnectException e) {
            throw new NoNetworkConnectionException();
        } catch (IOException e) {
            throw new TasksListException(e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    private String executeGet(String requestUrl) throws TasksListException, NoNetworkConnectionException {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(BASE_URL + requestUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            setSessionId(httpURLConnection);
            InputStream inputStream;
            inputStream = httpURLConnection.getInputStream();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                throw new UserNotAuthenticatedException();
            }
            StringWriter stringWriter = new StringWriter();
            IOUtils.copy(inputStream, stringWriter);
            return stringWriter.toString();
        } catch (ConnectException e) {
            logger.log(Level.SEVERE, "Unable to create Internet connection");
            throw new NoNetworkConnectionException();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to connect to notes service", e);
            throw new TasksListException();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    private String executeUpdate(String requestUrl, Object data, String requestMethod) throws TasksListException, NoNetworkConnectionException {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = prepareHttpURLConnection(requestUrl, data, requestMethod);

            InputStream inputStream = urlConnection.getInputStream();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                throw new UserNotAuthenticatedException();
            } else if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                throw new TasksListException();
            }
            StringWriter stringWriter = new StringWriter();
            IOUtils.copy(inputStream, stringWriter);
            return stringWriter.toString();
        } catch (ConnectException e) {
            throw new NoNetworkConnectionException();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to connect to notes service");
            throw new TasksListException(e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private HttpURLConnection prepareHttpURLConnection(String requestUrl, Object data, String requestMethod) throws IOException {
        HttpURLConnection urlConnection;
        URL url = new URL(BASE_URL + requestUrl);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod(requestMethod);
        urlConnection.setRequestProperty("Content-type", "application/json");
        urlConnection.setRequestProperty("Accept", "*/*");

        setSessionId(urlConnection);
        OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
        writer.write(GSON.toJson(data));
        writer.flush();
        return urlConnection;
    }

    private void setSessionId(HttpURLConnection httpURLConnection) {
        UserAuthentication userAuthentication = ((TaskListApplication) application).getUserAuthentication();
        if (userAuthentication != null) {
            String sessionId = userAuthentication.getSessionId();
            httpURLConnection.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
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
        tasksList.setSharedWith(taskListDto.getSharedWith());
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