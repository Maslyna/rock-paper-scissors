package net.roshambo.exception.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import net.roshambo.exception.GlobalServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GlobalServiceException.class)
    public ResponseEntity<?> handleGlobalServiceException(final GlobalServiceException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getBody());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(final ConstraintViolationException e) {
        final HttpStatus status = BAD_REQUEST;
        final Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        final ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, "validation error");
        final List<?> additionalMessages = violations.stream()
                .map(exception -> Map.of(
                        "error", exception.getMessage(),
                        "invalid value", exception.getInvalidValue())
                ).toList();

        detail.setProperties(Map.of("details", additionalMessages));

        return ResponseEntity.status(status).body(detail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        final HttpStatus status = BAD_REQUEST;
        final ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, e.getLocalizedMessage());

        detail.setProperties(Map.of("errors", e.getAllErrors()));

        return ResponseEntity.status(status).body(detail);
    }

}