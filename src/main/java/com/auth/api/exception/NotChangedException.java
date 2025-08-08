package com.auth.api.exception;

import java.io.Serial;

public class NotChangedException extends RuntimeException{

	@Serial
	private static final long serialVersionUID = 1L;

	public NotChangedException() {
        super("There was no change!");
    }

}
