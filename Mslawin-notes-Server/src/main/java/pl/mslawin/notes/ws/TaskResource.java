package pl.mslawin.notes.ws;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.mslawin.notes.domain.notes.Task;
import pl.mslawin.notes.domain.notes.TasksList;
import pl.mslawin.notes.dto.AddTaskRequest;
import pl.mslawin.notes.dto.ListDto;
import pl.mslawin.notes.dto.UserDto;
import pl.mslawin.notes.service.TaskService;

@RestController
@RequestMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
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
    public ResponseEntity<Long> createList(@RequestBody UserDto user) {
        TasksList list = taskService.createList(user.getEmail());
        return ResponseEntity.ok(list.getId());
    }

    @RequestMapping(value = "/addTask", method = RequestMethod.POST)
    public ResponseEntity<String> createTask(@RequestBody AddTaskRequest addTaskRequest) {
        taskService.addTask(addTaskRequest);
        return ResponseEntity.ok("OK");
    }

    @RequestMapping(value = "/{id}")
    public ResponseEntity<ListDto> getList(@PathVariable("id") String id) {
        Long idLong;
        try {
            idLong = Long.valueOf(id);
        } catch (NumberFormatException nfe) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(taskService.getList(idLong));
    }
}