package pl.mslawin.notes.ws;

import static pl.mslawin.notes.constants.NotesConstants.USER_PARAM;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.mslawin.notes.annotation.RequiresAuthentication;
import pl.mslawin.notes.domain.notes.Task;
import pl.mslawin.notes.domain.notes.TasksList;
import pl.mslawin.notes.dto.model.ListDto;
import pl.mslawin.notes.dto.model.TaskDto;
import pl.mslawin.notes.dto.model.TaskListDto;
import pl.mslawin.notes.dto.request.AddTaskRequest;
import pl.mslawin.notes.dto.request.CompleteTaskRequest;
import pl.mslawin.notes.dto.request.CreateListRequest;
import pl.mslawin.notes.dto.request.ShareListRequest;
import pl.mslawin.notes.excpetion.NotAuthenticatedException;
import pl.mslawin.notes.service.TaskService;

@RestController
@RequestMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiresAuthentication
public class TaskResource {

    private final TaskService taskService;

    @Inject
    public TaskResource(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public Task getTask() {
        return new Task("siema", "m@slawin.pl");
    }

    @RequestMapping(value = "/create", method = RequestMethod.PUT)
    public ResponseEntity<Long> createList(HttpServletRequest request, @RequestBody CreateListRequest createListRequest) {
        TasksList list = taskService.createList(createListRequest.getName(), getUserFromSession(request));
        return ResponseEntity.ok(list.getId());
    }

    @RequestMapping(value = "/addTask", method = RequestMethod.POST)
    public ResponseEntity<TaskDto> createTask(HttpServletRequest request, @RequestBody AddTaskRequest addTaskRequest) throws NotAuthenticatedException {
        String login = getUserFromSession(request);
        verifyListOwnedByUser(addTaskRequest.getListId(), login);

        TaskDto taskDto = taskService.addTask(addTaskRequest, login);
        return ResponseEntity.ok(taskDto);
    }

    @RequestMapping(value = "/{id}")
    public ResponseEntity<TaskListDto> getList(HttpServletRequest request, @PathVariable("id") long id) throws NotAuthenticatedException {
        verifyListOwnedByUser(id, getUserFromSession(request));
        return ResponseEntity.ok(taskService.getList(id));
    }

    @RequestMapping(value = "/getForUser")
    public ResponseEntity<ListDto> getAllListsForUser(HttpServletRequest request) {
        return ResponseEntity.ok(taskService.getAllListsForUser(getUserFromSession(request)));
    }

    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    public ResponseEntity<String> completeTask(HttpServletRequest request, @RequestBody CompleteTaskRequest completeTaskRequest) throws NotAuthenticatedException {
        verifyListOwnedByUser(completeTaskRequest.getListId(), getUserFromSession(request));
        taskService.completeTask(completeTaskRequest.getListId(), completeTaskRequest.getTaskId());
        return ResponseEntity.ok("OK");
    }

    @RequestMapping(value = "/share", method = RequestMethod.POST)
    public ResponseEntity<String> shareList(HttpServletRequest request, @RequestBody ShareListRequest shareListRequest) throws NotAuthenticatedException {
        verifyListOwnedByUser(shareListRequest.getListId(), getUserFromSession(request));
        taskService.shareList(shareListRequest.getListId(), shareListRequest.getShareWith());
        return ResponseEntity.ok("OK");
    }

    @ExceptionHandler(NotAuthenticatedException.class)
    public void handleAuthentication(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value());
    }

    private String getUserFromSession(HttpServletRequest request) {
        return (String) request.getSession().getAttribute(USER_PARAM);
    }

    private void verifyListOwnedByUser(Long listId, String login) throws NotAuthenticatedException {
        if (taskService.getAllListsForUser(login)
                .getTaskListDtoList()
                .stream()
                .filter(taskListDto -> taskListDto.getId() == listId)
                .collect(Collectors.toList())
                .isEmpty()) {
            throw new NotAuthenticatedException();
        }
    }
}