package com.RDS.skilltree.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SkillAlreadyExistsException extends RuntimeException {
    public SkillAlreadyExistsException(String message) {
        super(message);
    }
}
