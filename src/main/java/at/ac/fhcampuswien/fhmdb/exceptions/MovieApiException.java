package at.ac.fhcampuswien.fhmdb.exceptions;


/**
 * Thrown to indicate something went wrong when calling the Movie API.
 */

public class MovieApiException extends Exception {
    private final String code;
    private final String userMessage;

    public MovieApiException(String code, String message, String userMessage) {
        super(message);
        this.code = code;
        this.userMessage = userMessage;
    }

    public MovieApiException(String code, String message, String userMessage, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.userMessage = userMessage;
    }

    public String getCode() {
        return code;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
