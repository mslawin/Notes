package pl.mslawin.notes.domain.notes;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.joda.time.LocalDateTime;

@Entity
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "creation_time", nullable = false)
    private LocalDateTime creationTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "list_id")
    private TasksList tasksList;

    public Task() {
    }

    public Task(String text, String author) {
        this.text = text;
        this.author = author;
        this.creationTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public TasksList getTasksList() {
        return tasksList;
    }

    public void setTasksList(TasksList tasksList) {
        this.tasksList = tasksList;
    }
}