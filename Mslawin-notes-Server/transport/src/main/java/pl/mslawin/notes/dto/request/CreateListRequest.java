package pl.mslawin.notes.dto.request;

public class CreateListRequest {

    private String name;

    public CreateListRequest() {
    }

    public CreateListRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}