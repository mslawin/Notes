package pl.mslawin.notes.dto;

public class TaskDto {

    private UserDto author;
    private String text;
    private long creationDate;
    private long id;

    public TaskDto() {
    }

    public TaskDto(UserDto author, String text, long creationDate, long id) {
        this.author = author;
        this.text = text;
        this.creationDate = creationDate;
        this.id = id;
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
}