package pl.mslawin.notes.dto.model;

public class TaskDto {

    private String author;
    private String text;
    private long creationDate;
    private long id;
    private boolean completed;

    public TaskDto() {
    }

    public TaskDto(String author, String text, long creationDate, long id, boolean completed) {
        this.author = author;
        this.text = text;
        this.creationDate = creationDate;
        this.id = id;
        this.completed = completed;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}