package com.tree3.exception;

//自定义异常
public class IllegalTokenException extends RuntimeException {

    public IllegalTokenException(String message) {
        super(message);
    }
}