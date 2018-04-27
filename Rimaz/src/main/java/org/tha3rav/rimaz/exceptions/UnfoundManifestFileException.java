package org.tha3rav.rimaz.exceptions;

public class UnfoundManifestFileException extends Exception
{
    private static final long serialVersionUID = 4699100745036595088L;

    public UnfoundManifestFileException() {
    }

    public UnfoundManifestFileException(String message) {
        super(message);
    }

    public UnfoundManifestFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnfoundManifestFileException(Throwable cause) {
        super(cause);
    }

    protected UnfoundManifestFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
