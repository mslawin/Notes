package pl.mslawin.notes.ws;

import static pl.mslawin.notes.constants.NotesConstants.USER_PARAM;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import pl.mslawin.notes.dto.model.ListDto;
import pl.mslawin.notes.dto.model.TaskDto;
import pl.mslawin.notes.dto.model.TaskListDto;
import pl.mslawin.notes.dto.request.AddTaskRequest;
import pl.mslawin.notes.dto.request.CompleteTaskRequest;
import pl.mslawin.notes.dto.request.CreateListRequest;
import pl.mslawin.notes.dto.request.DeleteListRequest;
import pl.mslawin.notes.dto.request.ShareListRequest;
import pl.mslawin.notes.excpetion.AlreadySharedException;
import pl.mslawin.notes.excpetion.NotAuthenticatedException;
import pl.mslawin.notes.service.TaskService;

@RestController
@RequestMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiresAuthentication
public class TaskResource {

    private static final Logger logger = LoggerFactory.getLogger(TaskResource.class);

    private final TaskService taskService;

    @Inject
    public TaskResource(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.PUT)
    public ResponseEntity<TaskListDto> createList(HttpServletRequest request, @RequestBody CreateListRequest createListRequest) {
        String login = getUserFromSession(request);
        TaskListDto list = taskService.createList(createListRequest.getName(), login);
        logger.info("User {} created a list with id {}", login, list.getId());
        return ResponseEntity.ok(list);
    }

    @RequestMapping(value = "/addTask", method = RequestMethod.POST)
    public ResponseEntity<TaskDto> createTask(HttpServletRequest request, @RequestBody AddTaskRequest addTaskRequest) throws NotAuthenticatedException {
        String login = getUserFromSession(request);
        verifyListOwnedByUser(addTaskRequest.getListId(), login);

        TaskDto taskDto = taskService.addTask(addTaskRequest, login);
        logger.info("User {} created a task with id {} for list {}", login, taskDto.getId(), addTaskRequest.getListId());
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
        String login = getUserFromSession(request);
        verifyListOwnedByUser(completeTaskRequest.getListId(), login);
        taskService.completeTask(completeTaskRequest.getListId(), completeTaskRequest.getTaskId());
        logger.info("User {} completed task with id {}", login, completeTaskRequest.getTaskId());
        return ResponseEntity.ok("OK");
    }

    @RequestMapping(value = "/share", method = RequestMethod.POST)
    public ResponseEntity<String> shareList(HttpServletRequest request, @RequestBody ShareListRequest shareListRequest)
            throws NotAuthenticatedException, AlreadySharedException {
        String login = getUserFromSession(request);
        verifyListOwnedByUser(shareListRequest.getListId(), login);
        taskService.shareList(shareListRequest.getListId(), shareListRequest.getShareWith());
        logger.info("User {} shared list {} with user {}", login, shareListRequest.getListId(), shareListRequest.getShareWith());
        return ResponseEntity.ok("OK");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteList(HttpServletRequest request, @RequestBody DeleteListRequest deleteListRequest) throws NotAuthenticatedException {
        String login = getUserFromSession(request);
        verifyListOwnedByUser(deleteListRequest.getListId(), login);
        taskService.deleteList(deleteListRequest.getListId());
        logger.info("User {} deleted list with id {}", login, deleteListRequest.getListId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(NotAuthenticatedException.class)
    public void handleAuthentication(HttpServletResponse response, NotAuthenticatedException e) throws IOException {
        logger.error("Not authenticated request", e);
        response.sendError(HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(AlreadySharedException.class)
    public void handleAlreadySharedException(HttpServletResponse response, AlreadySharedException e) throws IOException {
        logger.error("Trying to share with the same user", e);
        response.sendError(HttpStatus.BAD_REQUEST.value());
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