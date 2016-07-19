package pl.mslawin.notes.app.exception;

import pl.mslawin.notes.app.R;

/**
 * Created by maciej on 10/11/15.
 */
public class TasksListException extends Exception {

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