package pl.mslawin.notes.app.service.request;

/**
 * Created by mslawin on 1/17/16.
 */
public class AsyncTaskResult<T> {

    private final T result;
    private final ErrorCode errorCode;

    public AsyncTaskResult(T result) {
        this.result = result;
        this.errorCode = null;
    }

    public AsyncTaskResult(T result, ErrorCode errorCode) {
        this.result = result;
        this.errorCode = errorCode;
    }

    public AsyncTaskResult(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.result = null;
    }

    public T getResult() {
        return result;
    }

    public boolean isSuccess() {
        return result != null;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public enum ErrorCode {
        NOT_AUTHENTICATED, NO_NETWORK_CONNECTION, ERROR_GENERAL
    }
}