package pl.mslawin.notes.app.exception;

/**
 * Created by mslawin on 1/11/16.
 */
public class UserNotAuthenticatedException extends TasksListException {

    public UserNotAuthenticatedException() {
    }

    public UserNotAuthenticatedException(String detailMessage) {
        super(detailMessage);
    }

    public UserNotAuthenticatedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UserNotAuthenticatedException(Throwable throwable) {
        super(throwable);
    }
}