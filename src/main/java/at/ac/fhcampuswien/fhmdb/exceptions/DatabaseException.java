package at.ac.fhcampuswien.fhmdb.exceptions;



/**
 * Thrown to indicate something went wrong at the database layer.
 */

public class DatabaseException extends Exception {
    /** A machine‐readable error code (e.g. "DB_SAVE_ERROR") */
    private final String code;
    /** A message safe to show end users (already localized/plain text) */
    private final String userMessage;

    /**
     * @param code         machine‐readable error identifier
     * @param message      developer message (for logging/stack trace)
     * @param userMessage  friendlier text for the UI
     */
    public DatabaseException(String code, String message, String userMessage) {
        super(message);
        this.code = code;
        this.userMessage = userMessage;
    }

    /**
     * @param code         machine‐readable error identifier
     * @param message      developer message (for logging/stack trace)
     * @param userMessage  friendlier text for the UI
     * @param cause        original exception
     */
    public DatabaseException(String code, String message, String userMessage, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.userMessage = userMessage;
    }

    /** @return the internal error code */
    public String getCode() {
        return code;
    }

    /** @return the message to display in the UI */
    public String getUserMessage() {
        return userMessage;
    }
}
