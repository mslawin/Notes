package pl.mslawin.notes.dto.model;

import java.util.List;

public class TaskListDto {

    private String name;
    private String owner;
    private List<TaskDto> tasks;
    private long id;
    private List<String> sharedWith;

    public TaskListDto() {
    }

    public TaskListDto(String name, String owner, List<TaskDto> tasks, long id, List<String> sharedWith) {
        this.name = name;
        this.owner = owner;
        this.tasks = tasks;
        this.id = id;
        this.sharedWith = sharedWith;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
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

    public List<String> getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(List<String> sharedWith) {
        this.sharedWith = sharedWith;
    }
}