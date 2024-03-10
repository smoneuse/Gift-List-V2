package com.scilab.giftslist.infra.errors;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }

}
