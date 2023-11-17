package com.thomasjayconsulting.badwordcheckerservice.exception;

/**
 * Exception when checking sentence fails
 *
  * @author Thomas Jay
 *
 */
public class CheckSentenceException extends Exception {

    public CheckSentenceException(String message) {
        super(message);
    }

    public CheckSentenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
