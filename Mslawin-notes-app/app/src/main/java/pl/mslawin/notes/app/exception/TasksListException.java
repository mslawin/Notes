package pl.mslawin.notes.app.exception;

/**
 * Created by maciej on 10/11/15.
 */
public class TasksListException extends RuntimeException {

    public TasksListException() {
    }

    public TasksListException(String detailMessage) {
        super(detailMessage);
    }

    public TasksListException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public TasksListException(Throwable throwable) {
        super(throwable);
    }
}