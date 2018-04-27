package org.tha3rav.rimaz.exceptions;

public class ErroneousManifestFileException extends Exception
{
    private static final long serialVersionUID = 392654046129826080L;

    public ErroneousManifestFileException() {
    }

    public ErroneousManifestFileException(String message) {
        super(message);
    }

    public ErroneousManifestFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErroneousManifestFileException(Throwable cause) {
        super(cause);
    }

    protected ErroneousManifestFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
