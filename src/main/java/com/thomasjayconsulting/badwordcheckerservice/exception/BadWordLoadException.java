package com.thomasjayconsulting.badwordcheckerservice.exception;

/**
 * Exception when loading bad words fails
 *
 * @author Thomas Jay
 *
 */
public class BadWordLoadException extends Exception {

    public BadWordLoadException(String message) {
        super(message);
    }

    public BadWordLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
