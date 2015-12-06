package pl.mslawin.notes.dto.request;

public class ShareListRequest {

    private long listId;
    private String shareWith;

    public ShareListRequest() {
    }

    public ShareListRequest(long listId, String shareWith) {
        this.listId = listId;
        this.shareWith = shareWith;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public String getShareWith() {
        return shareWith;
    }

    public void setShareWith(String shareWith) {
        this.shareWith = shareWith;
    }
}