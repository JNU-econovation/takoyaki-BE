package com.bestbenefits.takoyaki.exception;

public class NotLoginException extends IllegalStateException{
    public NotLoginException() {
        super("you need login.");
    }
}
