package org.tha3rav.rimaz.exceptions;

public class InvalidLayoutAttributeException extends Exception
{
    private static final long serialVersionUID = 4052340010511368863L;

    public InvalidLayoutAttributeException() {
    }

    public InvalidLayoutAttributeException(String message) {
        super(message);
    }

    public InvalidLayoutAttributeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidLayoutAttributeException(Throwable cause) {
        super(cause);
    }

    protected InvalidLayoutAttributeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
