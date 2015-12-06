package pl.mslawin.notes.dto.request;

public class AddTaskRequest {

    private String text;
    private Long listId;

    public AddTaskRequest() {
    }

    public AddTaskRequest(String text, Long listId) {
        this.text = text;
        this.listId = listId;
    }

    public String getText() {
        return text;
    }

    public Long getListId() {
        return listId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }
}