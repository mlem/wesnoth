package org.wesnoth.connection;

public class ExternalServiceException extends Exception {
    public ExternalServiceException(String message, Exception e) {
        super(message, e);
    }
}
