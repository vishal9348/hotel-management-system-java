package com.bigocode.hotel_management.exception;

import org.aspectj.weaver.ast.Not;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){
        super(message);
    }
}
