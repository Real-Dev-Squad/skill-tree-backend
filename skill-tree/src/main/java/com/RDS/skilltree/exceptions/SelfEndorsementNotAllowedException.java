package com.RDS.skilltree.exceptions;

public class SelfEndorsementNotAllowedException extends RuntimeException {
    public SelfEndorsementNotAllowedException(String message) {
        super(message);
    }
}
