package pl.mslawin.notes.excpetion;

public class AlreadySharedException extends Exception {

    public AlreadySharedException() {
    }

    public AlreadySharedException(String message) {
        super(message);
    }

    public AlreadySharedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadySharedException(Throwable cause) {
        super(cause);
    }

    public AlreadySharedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
