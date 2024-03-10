package com.scilab.giftslist.infra.errors;

public class DefaultException extends RuntimeException{
    public DefaultException(String message){
        super(message);
    }
}
