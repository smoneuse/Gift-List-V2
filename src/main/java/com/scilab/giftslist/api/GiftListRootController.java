package com.scilab.giftslist.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.scilab.giftslist.infra.errors.BadRequestException;
import com.scilab.giftslist.infra.errors.DefaultException;
import com.scilab.giftslist.infra.errors.NotFoundException;

public class GiftListRootController {
    @ExceptionHandler(DefaultException.class)
    public ResponseEntity<String> handleDefaultEx(DefaultException def){
        return ResponseEntity.internalServerError().body(def.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestEx(BadRequestException badE){
        return ResponseEntity.badRequest().body(badE.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleByDefault(Throwable thr){
        return ResponseEntity.internalServerError().body(thr.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException nfe){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nfe.getMessage());
    }
}
