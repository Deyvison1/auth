package com.auth.api.exception;

import java.io.Serial;

public class NotFoundEntityException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	public NotFoundEntityException() {
		super("Entity not found");
	}
}
