package pl.mslawin.notes.dto;

import java.util.List;

public class ListDto {

    private UserDto owner;
    private List<TaskDto> tasks;

    public ListDto() {
    }

    public ListDto(UserDto owner, List<TaskDto> tasks) {
        this.owner = owner;
        this.tasks = tasks;
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
}
