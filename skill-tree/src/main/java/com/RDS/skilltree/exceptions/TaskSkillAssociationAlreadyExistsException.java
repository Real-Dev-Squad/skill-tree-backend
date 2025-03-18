package com.RDS.skilltree.exceptions;

public class TaskSkillAssociationAlreadyExistsException extends RuntimeException {
    public TaskSkillAssociationAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskSkillAssociationAlreadyExistsException(String message) {
        super(message);
    }
}
