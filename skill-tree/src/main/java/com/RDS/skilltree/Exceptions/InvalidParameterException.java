package com.RDS.skilltree.Exceptions;

// TODO : This is to be removed from this PR (as it is going as part of the test PR)
public class InvalidParameterException extends IllegalArgumentException {
    public InvalidParameterException(String parameterName, String message) {
        super("Invalid parameter " + parameterName + ": " + message);
    }
}
