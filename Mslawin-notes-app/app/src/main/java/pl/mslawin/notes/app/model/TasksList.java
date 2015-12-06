package pl.mslawin.notes.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maciej on 10/11/15.
 */
public class TasksList implements Serializable {

    private Long id;
    private String name;
    private String owner;
    private List<Task> tasks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
