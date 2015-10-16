package pl.mslawin.notes.app.exception;

/**
 * Created by maciej on 10/11/15.
 */
public class GetTasksListException extends RuntimeException {

    public GetTasksListException() {
    }

    public GetTasksListException(String detailMessage) {
        super(detailMessage);
    }

    public GetTasksListException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public GetTasksListException(Throwable throwable) {
        super(throwable);
    }
}