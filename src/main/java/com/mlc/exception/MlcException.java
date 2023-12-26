package com.mlc.exception;

public class MlcException extends Exception{
    private int statusCode;

    public MlcException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    public int getStausCode(){
        return this.statusCode;
    }
}
