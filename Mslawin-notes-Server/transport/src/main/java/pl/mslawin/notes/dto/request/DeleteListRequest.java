package pl.mslawin.notes.dto.request;

public class DeleteListRequest {

    private long listId;

    public DeleteListRequest() {
    }

    public DeleteListRequest(long listId) {
        this.listId = listId;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }
}