package me.luucka.neweconomy.api;

public class UserNotExistsException extends Exception {

    public UserNotExistsException(final String message) {
        super(message);
    }
}
