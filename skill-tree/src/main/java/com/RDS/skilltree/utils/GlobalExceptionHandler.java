package com.RDS.skilltree.utils;

import com.RDS.skilltree.Common.Response.GenericResponse;
import com.RDS.skilltree.Exceptions.EntityAlreadyExistsException;
import com.RDS.skilltree.Exceptions.NoEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({NoEntityException.class})
    public ResponseEntity<GenericResponse<Object>> handleNoEntityException(NoEntityException ex){
        log.error("NoEntityException - Error : {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new GenericResponse<>(null, ex.getMessage()));
    }

    @ExceptionHandler({EntityAlreadyExistsException.class})
    public ResponseEntity<GenericResponse<Object>> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex){
        log.error("EntityAlreadyExistsException - Error : {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new GenericResponse<>(null, ex.getMessage()));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<GenericResponse<Object>> handleRuntimException(RuntimeException ex){
        log.error("Runtime Exception - Error : {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new GenericResponse<>(null, "Something went wrong, please try communicating on `#wg-skill-tree discord` channel" ));
    }

}
