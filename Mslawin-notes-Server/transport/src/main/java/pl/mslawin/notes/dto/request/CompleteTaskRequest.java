package pl.mslawin.notes.dto.request;

public class CompleteTaskRequest {

    private long listId;
    private long taskId;

    public CompleteTaskRequest() {
    }

    public CompleteTaskRequest(long listId, long taskId) {
        this.listId = listId;
        this.taskId = taskId;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }
}