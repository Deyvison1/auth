package com.auth.api.exception;

import com.auth.api.exception.dto.StandardErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(NotFoundEntityException.class)
	public ResponseEntity<StandardErrorDTO> resourceNotFound(NotFoundEntityException e, HttpServletRequest request) {
		String error = "Entity not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardErrorDTO err = StandardErrorDTO.builder().timestamp(Instant.now()).status(status.value()).error(error)
				.message(e.getMessage()).path(request.getRequestURI()).build();
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		String message = "Nickname already exists"; // ou pegue do ex.getMessage() se quiser mais técnico
		return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
	    List<String> errors = ex.getConstraintViolations()
	        .stream()
	        .map(violation -> violation.getMessage()) // ou .getPropertyPath() + ": " + .getMessage()
	        .collect(Collectors.toList());

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

}
