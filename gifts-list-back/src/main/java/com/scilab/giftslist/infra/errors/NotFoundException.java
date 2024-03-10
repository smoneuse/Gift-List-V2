package com.scilab.giftslist.infra.errors;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
