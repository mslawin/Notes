package pl.mslawin.notes.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import pl.mslawin.notes.dao.ListDao;
import pl.mslawin.notes.domain.notes.Task;
import pl.mslawin.notes.domain.notes.TasksList;
import pl.mslawin.notes.dto.model.ListDto;
import pl.mslawin.notes.dto.model.TaskDto;
import pl.mslawin.notes.dto.model.TaskListDto;
import pl.mslawin.notes.dto.request.AddTaskRequest;
import pl.mslawin.notes.excpetion.AlreadySharedException;

@Service
public class TaskService {

    private static final String SHARED_STRING_SPLIT = ";";
    private final ListDao listDao;

    @Inject
    public TaskService(ListDao listDao) {
        this.listDao = listDao;
    }

    @Transactional
    public TaskListDto createList(String name, String email) {
        TasksList tasksList = new TasksList(name, email);
        TasksList list = listDao.save(tasksList);
        return transformListToListDto(list);
    }

    @Transactional
    public TaskDto addTask(AddTaskRequest addTaskRequest, String login) {
        Task task = new Task(addTaskRequest.getText(), login);
        TasksList list = listDao.findById(addTaskRequest.getListId());
        List<Long> taskIdsBeforeInsert = getTaskIds(list);
        list.getTasks().add(task);
        list = listDao.save(list);
        List<Long> taskIdsAfterInsert = getTaskIds(list);
        taskIdsAfterInsert.removeAll(taskIdsBeforeInsert);
        task.setId(taskIdsAfterInsert.size() > 0 ? taskIdsAfterInsert.get(0) : -1L);
        return transformTaskToTaskDto(task);
    }

    @Transactional(readOnly = true)
    public TaskListDto getList(Long id) {
        TasksList list = listDao.findOne(id);
        return transformListToListDto(list);
    }

    @Transactional(readOnly = true)
    public ListDto getAllListsForUser(String email) {
        List<TaskListDto> result = listDao.findByOwner(email)
                .stream()
                .map(this::transformListToListDto)
                .collect(Collectors.toList());
        List<TaskListDto> sharedWith = listDao.findBySharedWith("%;" + email + ";%")
                .stream()
                .map(this::transformListToListDto)
                .collect(Collectors.toList());
        result.addAll(sharedWith);
        return new ListDto(result);
    }

    @Transactional
    public void completeTask(long listId, long taskId) {
        TasksList tasksList = listDao.findById(listId);
        tasksList.getTasks().stream()
                .filter(task -> task.getId().equals(taskId))
                .forEach(task -> task.setCompleted(true));
        listDao.save(tasksList);
    }

    @Transactional
    public void shareList(long listId, String emailToShare) throws AlreadySharedException {
        TasksList tasksList = listDao.findById(listId);
        if (isAlreadySharedWith(tasksList, emailToShare)) {
            throw new AlreadySharedException();
        }
        String sharedWith = tasksList.getSharedWith();
        if (StringUtils.isEmpty(sharedWith)) {
            sharedWith = emailToShare;
        } else {
            sharedWith += SHARED_STRING_SPLIT + emailToShare;
        }
        tasksList.setSharedWith(sharedWith);
        listDao.save(tasksList);
    }

    @Transactional
    public void deleteList(long listId) {
        listDao.delete(listId);
    }

    private boolean isAlreadySharedWith(TasksList tasksList, String emailToShare) {
        for (String sharedWith : transformSharedWIthToList(tasksList.getSharedWith())) {
            if (sharedWith.equalsIgnoreCase(emailToShare)) {
                return true;
            }
        }
        return tasksList.getOwner().equalsIgnoreCase(emailToShare);
    }

    private TaskListDto transformListToListDto(TasksList list) {
        List<TaskDto> tasksDto = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list.getTasks())) {
            tasksDto.addAll(list.getTasks()
                    .stream()
                    .map(this::transformTaskToTaskDto)
                    .collect(Collectors.toList()));
        }
        String sharedWith = list.getSharedWith();

        List<String> sharedWithList;
        sharedWithList = StringUtils.isEmpty(sharedWith) ? new ArrayList<>() : transformSharedWIthToList(sharedWith);
        return new TaskListDto(list.getName(), list.getOwner(), tasksDto, list.getId(), sharedWithList);
    }

    private List<String> transformSharedWIthToList(String sharedWith) {
        if (StringUtils.isEmpty(sharedWith)) {
            return Collections.emptyList();
        }
        return Lists.newArrayList(sharedWith.split(SHARED_STRING_SPLIT));
    }

    private TaskDto transformTaskToTaskDto(Task task) {
        return new TaskDto(task.getAuthor(), task.getText(),
                task.getCreationTime().toDate().getTime(), task.getId() != null ? task.getId() : -1, task.isCompleted());
    }

    private List<Long> getTaskIds(TasksList list) {
        return list
                .getTasks()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toList());
    }
}