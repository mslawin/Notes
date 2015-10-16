package pl.mslawin.notes.dto;

import java.util.List;

public class TaskListDto {

    private UserDto owner;
    private List<TaskDto> tasks;
    private long id;

    public TaskListDto() {
    }

    public TaskListDto(UserDto owner, List<TaskDto> tasks, long id) {
        this.owner = owner;
        this.tasks = tasks;
        this.id = id;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
