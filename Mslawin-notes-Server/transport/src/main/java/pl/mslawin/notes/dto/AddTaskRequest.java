package pl.mslawin.notes.dto;

public class AddTaskRequest {

    private UserDto user;
    private String text;
    private Long listId;

    public AddTaskRequest() {
    }

    public AddTaskRequest(UserDto user, String text, Long listId) {
        this.user = user;
        this.text = text;
        this.listId = listId;
    }

    public UserDto getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public Long getListId() {
        return listId;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }
}