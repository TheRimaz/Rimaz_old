package org.tha3rav.rimaz.exceptions;

public class NotEventHandlerOverriderException extends Exception
{
    private static final long serialVersionUID = 4450565356219678106L;

    public NotEventHandlerOverriderException() {
    }

    public NotEventHandlerOverriderException(String message) {
        super(message);
    }

    public NotEventHandlerOverriderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEventHandlerOverriderException(Throwable cause) {
        super(cause);
    }

    protected NotEventHandlerOverriderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
