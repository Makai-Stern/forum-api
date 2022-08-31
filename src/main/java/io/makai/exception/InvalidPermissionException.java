package io.makai.exception;

public class InvalidPermissionException extends RuntimeException {

    public InvalidPermissionException() {
        super("You cannot edit this resource");
    }

    public InvalidPermissionException(String message) {
        super(message);
    }
}
