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

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NoEntityException.class})
    public ResponseEntity<GenericResponse<Object>> handleNoEntityException(NoEntityException ex) {
        log.error("NoEntityException - Error : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericResponse<>(ex.getMessage()));
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
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GenericResponse<>(ex.getMessage()));
    }

    @ExceptionHandler({EntityAlreadyExistsException.class})
    public ResponseEntity<GenericResponse<Object>> handleEntityAlreadyExistsException(
            EntityAlreadyExistsException ex) {
        log.error("EntityAlreadyExistsException - Error : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new GenericResponse<>(ex.getMessage()));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<GenericResponse<Object>> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime Exception - Error : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new GenericResponse<>("Runtime Exception - Something went wrong, please try again."));
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
                .body(new GenericResponse<>(errorString.toString().trim()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<GenericResponse<Object>> handleException(Exception ex) {
        log.error("Exception - Error : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new GenericResponse<>("Something unexpected happened, please try again."));
    }

    @ExceptionHandler({InvalidParameterException.class})
    public ResponseEntity<GenericResponse<Object>> handleException(InvalidParameterException ex) {
        log.error("InvalidParameterException - Error : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new GenericResponse<>(ex.getMessage()));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<GenericResponse<Object>> handleException(ConstraintViolationException ex) {
        log.error("ConstraintViolationException - Error : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new GenericResponse<>(ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("UserNotFoundException - Error : {}", ex.getMessage());
        return new ResponseEntity<>(new GenericResponse<>(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SkillAlreadyExistsException.class)
    public ResponseEntity<?> handleSkillAlreadyExistsException(SkillAlreadyExistsException ex) {
        log.error("SkillAlreadyExistsException - Error : {}", ex.getMessage());
        return new ResponseEntity<>(new GenericResponse<>(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SelfEndorsementNotAllowedException.class)
    public ResponseEntity<?> handleSelfEndorsementNotAllowedException(
            SelfEndorsementNotAllowedException ex) {
        log.error("SelfEndorsementNotAllowedException - Error : {}", ex.getMessage());
        return new ResponseEntity<>(
                new GenericResponse<>(ex.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(SkillNotFoundException.class)
    public ResponseEntity<?> handleSkillNotFoundException(SkillNotFoundException ex) {
        log.error("SkillNotFoundException - Error : {}", ex.getMessage());
        return new ResponseEntity<>(new GenericResponse<>(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskSkillAssociationAlreadyExistsException.class)
    public ResponseEntity<GenericResponse<Object>> handleTaskSkillAssociationAlreadyExistsException(
            TaskSkillAssociationAlreadyExistsException ex) {
        log.error("Duplicate task-skill association: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new GenericResponse<>(null, ex.getMessage()));
    }

    @ExceptionHandler(EndorsementNotFoundException.class)
    public ResponseEntity<?> handleEndorsementNotFoundException(EndorsementNotFoundException ex) {
        log.error("EndorsementNotFoundException - Error : {}", ex.getMessage());
        return new ResponseEntity<>(new GenericResponse<>(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenException(ForbiddenException ex) {
        log.error("ForbiddenException - Error : {}", ex.getMessage());
        return new ResponseEntity<>(new GenericResponse<>(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<?> handleInternalServerErrorException(InternalServerErrorException ex) {
        log.error("Internal Server Error", ex);
        String errorMessage = "An unexpected error occurred.";
        return new ResponseEntity<>(
                new GenericResponse<>(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EndorsementAlreadyExistsException.class)
    public ResponseEntity<?> handleEndorsementAlreadyExistsException(
            EndorsementAlreadyExistsException ex) {
        log.error("EndorsementAlreadyExistsException - Error : {}", ex.getMessage());
        return new ResponseEntity<>(
                new GenericResponse<>(ex.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
    }
}
