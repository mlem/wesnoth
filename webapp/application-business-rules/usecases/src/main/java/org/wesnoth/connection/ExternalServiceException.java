package org.wesnoth.connection;

public class ExternalServiceException extends Throwable {
    public ExternalServiceException(String message, Exception e) {
        super(message, e);
    }
}
