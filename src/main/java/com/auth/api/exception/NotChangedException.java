package com.auth.api.exception;

public class NotChangedException extends RuntimeException{

    public NotChangedException() {
        super("There was no change!");
    }

}
