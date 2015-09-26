package pl.mslawin.notes.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import pl.mslawin.notes.dao.ListDao;
import pl.mslawin.notes.domain.notes.Task;
import pl.mslawin.notes.domain.notes.TasksList;
import pl.mslawin.notes.dto.AddTaskRequest;
import pl.mslawin.notes.dto.ListDto;
import pl.mslawin.notes.dto.TaskDto;
import pl.mslawin.notes.dto.UserDto;

@Service
public class TaskService {

    private final ListDao listDao;

    @Inject
    public TaskService(ListDao listDao) {
        this.listDao = listDao;
    }

    public TasksList createList(String email) {
        TasksList tasksList = new TasksList(email);
        return listDao.save(tasksList);
    }

    public void addTask(AddTaskRequest addTaskRequest) {
        Task task = new Task(addTaskRequest.getText(), addTaskRequest.getUser().getEmail());
        TasksList list = listDao.findById(addTaskRequest.getListId());
        list.getTasks().add(task);
        listDao.save(list);
    }

    public ListDto getList(Long id) {
        TasksList list = listDao.findOne(id);
        List<TaskDto> tasksDto = new ArrayList<>();
        for (Task task : list.getTasks()) {
            TaskDto taskDto = new TaskDto(new UserDto(task.getAuthor()), task.getText(), task.getCreationTime());
            tasksDto.add(taskDto);
        }
        return new ListDto(new UserDto(list.getOwner()), tasksDto);
    }
}