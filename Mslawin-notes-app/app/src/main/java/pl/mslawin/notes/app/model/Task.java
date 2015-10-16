package pl.mslawin.notes.app.model;

import org.joda.time.LocalDateTime;

import java.io.Serializable;

/**
 * Created by maciej on 10/11/15.
 */
public class Task implements Serializable {

    private Long id;
    private String text;
    private String author;
    private LocalDateTime creationTime;

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
}
