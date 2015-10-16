package pl.mslawin.notes.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.mslawin.notes.dao.ListDao;
import pl.mslawin.notes.domain.notes.Task;
import pl.mslawin.notes.domain.notes.TasksList;
import pl.mslawin.notes.dto.AddTaskRequest;
import pl.mslawin.notes.dto.ListDto;
import pl.mslawin.notes.dto.TaskListDto;
import pl.mslawin.notes.dto.TaskDto;
import pl.mslawin.notes.dto.UserDto;

@Service
public class TaskService {

    private final ListDao listDao;

    @Inject
    public TaskService(ListDao listDao) {
        this.listDao = listDao;
    }

    @Transactional
    public TasksList createList(String email) {
        TasksList tasksList = new TasksList(email);
        return listDao.save(tasksList);
    }

    @Transactional
    public void addTask(AddTaskRequest addTaskRequest) {
        Task task = new Task(addTaskRequest.getText(), addTaskRequest.getUser().getEmail());
        TasksList list = listDao.findById(addTaskRequest.getListId());
        list.getTasks().add(task);
        listDao.save(list);
    }

    @Transactional(readOnly = true)
    public TaskListDto getList(Long id) {
        TasksList list = listDao.findOne(id);
        return transformListToListDto(list);
    }

    @Transactional(readOnly = true)
    public ListDto getAllListsForUser(String email) {
        List<TaskListDto> result = new ArrayList<>();
        for (TasksList tasksList : listDao.findByOwner(email)) {
            result.add(transformListToListDto(tasksList));
        }
        return new ListDto(result);
    }

    private TaskListDto transformListToListDto(TasksList list) {
        List<TaskDto> tasksDto = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list.getTasks())) {
            for (Task task : list.getTasks()) {
                TaskDto taskDto = new TaskDto(new UserDto(task.getAuthor()), task.getText(),
                        task.getCreationTime().toDate().getTime(), task.getId());
                tasksDto.add(taskDto);
            }
        }
        return new TaskListDto(new UserDto(list.getOwner()), tasksDto, list.getId());
    }
}