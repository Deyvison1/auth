package com.auth.api.exception;

import com.auth.api.exception.dto.StandardErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(NotFoundEntityException.class)
    public ResponseEntity<StandardErrorDTO> resourceNotFound(NotFoundEntityException e, HttpServletRequest request) {
        String error = "Entity not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardErrorDTO err = StandardErrorDTO.builder().timestamp(Instant.now()).status(status.value()).error(error).message(e.getMessage()).path(request.getRequestURI()).build();
        return ResponseEntity.status(status).body(err);
    }
}
