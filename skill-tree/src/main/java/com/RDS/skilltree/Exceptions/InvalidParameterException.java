package com.RDS.skilltree.Exceptions;

public class InvalidParameterException extends IllegalArgumentException{
    public InvalidParameterException(String parameterName, String message){
        super("Invalid parameter "+parameterName+": "+message);
    }
}
