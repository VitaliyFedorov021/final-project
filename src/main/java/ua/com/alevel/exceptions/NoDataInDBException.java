package ua.com.alevel.exceptions;

public class NoDataInDBException extends Exception {
    public NoDataInDBException() {
    }

    public NoDataInDBException(String message) {
        super(message);
    }

    public NoDataInDBException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDataInDBException(Throwable cause) {
        super(cause);
    }

    public NoDataInDBException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
