package pl.mslawin.notes.dto;

import java.time.LocalDateTime;

public class TaskDto {

    private UserDto author;
    private String text;
    private LocalDateTime creationDate;

    public TaskDto() {
    }

    public TaskDto(UserDto author, String text, LocalDateTime creationDate) {
        this.author = author;
        this.text = text;
        this.creationDate = creationDate;
    }

    public UserDto getAuthor() {
        return author;
    }

    public void setAuthor(UserDto author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
