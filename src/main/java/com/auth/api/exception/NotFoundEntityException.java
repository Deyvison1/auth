package com.auth.api.exception;

public class NotFoundEntityException extends RuntimeException {

    public NotFoundEntityException() {
        super("Entity not found");
    }
}
