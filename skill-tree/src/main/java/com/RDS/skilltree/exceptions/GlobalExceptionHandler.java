package com.RDS.skilltree.exceptions;

import com.RDS.skilltree.utils.GenericResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({NoEntityException.class})
    public ResponseEntity<GenericResponse<Object>> handleNoEntityException(NoEntityException ex) {
        log.error("NoEntityException - Error : {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new GenericResponse<>(null, ex.getMessage()));
    }

    @ExceptionHandler({AuthenticationException.class, InsufficientAuthenticationException.class})
    public ResponseEntity<GenericResponse<Object>> handleInvalidBearerTokenException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        new GenericResponse<>(
                                null,
                                "The access token provided is expired, revoked, malformed, or invalid for other reasons."));
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<GenericResponse<Object>> handleAccessDeniedException(
            AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new GenericResponse<>(null, ex.getMessage()));
    }

    @ExceptionHandler({EntityAlreadyExistsException.class})
    public ResponseEntity<GenericResponse<Object>> handleEntityAlreadyExistsException(
            EntityAlreadyExistsException ex) {
        log.error("EntityAlreadyExistsException - Error : {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new GenericResponse<>(null, ex.getMessage()));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<GenericResponse<Object>> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime Exception - Error : {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new GenericResponse<>(
                                null, "Runtime Exception - Something went wrong, please try again."));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<GenericResponse<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        StringBuilder errorString = new StringBuilder();
        List<FieldError> fieldErrors =
                ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errorString.append(fieldError.getDefaultMessage());
            errorString.append(" ");
        }
        log.error("MethodArgumentNotValidException Exception - Error : {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new GenericResponse<>(null, errorString.toString().trim()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<GenericResponse<Object>> handleException(Exception ex) {
        log.error("Exception - Error : {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new GenericResponse<>(null, "Something unexpected happened, please try again."));
    }

    @ExceptionHandler({InvalidParameterException.class})
    public ResponseEntity<GenericResponse<Object>> handleException(InvalidParameterException ex) {
        log.error("Exception - Error : {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new GenericResponse<>(null, ex.getMessage()));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<GenericResponse<Object>> handleException(ConstraintViolationException ex) {
        log.error("Exception - Error : {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new GenericResponse<>(null, ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {
        log.error("Exception - Error : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new GenericResponse<>(null, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SkillAlreadyExistsException.class)
    public ResponseEntity<?> handleSkillAlreadyExistsException(
            SkillAlreadyExistsException ex, WebRequest request) {
        log.error("Exception - Error : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new GenericResponse<>(null, ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SelfEndorsementNotAllowedException.class)
    public ResponseEntity<?> handleSelfEndorsementNotAllowedException(
            SelfEndorsementNotAllowedException ex, WebRequest request) {
        log.error("Exception - Error : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(
                new GenericResponse<>(null, ex.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(SkillNotFoundException.class)
    public ResponseEntity<?> handleSkillNotFoundException(
            SkillNotFoundException ex, WebRequest request) {
        log.error("Exception - Error : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new GenericResponse<>(null, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EndorsementNotFoundException.class)
    public ResponseEntity<?> handleEndorsementNotException(
            EndorsementNotFoundException ex, WebRequest request) {
        log.error("Exception - Error : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new GenericResponse<>(null, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenException(ForbiddenException ex, WebRequest request) {
        log.error("Exception - Error : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new GenericResponse<>(null, ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<?> handleInternalServerErrorException(
            InternalServerErrorException ex, WebRequest request) {
        log.error("Exception - Error : {}", ex.getMessage(), ex);
        return new ResponseEntity<>(
                new GenericResponse<>(null, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
