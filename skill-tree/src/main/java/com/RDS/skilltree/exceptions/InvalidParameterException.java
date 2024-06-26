package com.RDS.skilltree.exceptions;

public class InvalidParameterException extends IllegalArgumentException {
    public InvalidParameterException(String parameterName, String message) {
        super("Invalid parameter " + parameterName + ": " + message);
    }
}
