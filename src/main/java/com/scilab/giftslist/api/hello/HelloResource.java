package com.scilab.giftslist.api.hello;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scilab.giftslist.api.ApiConstants;
import com.scilab.giftslist.api.GiftListRootController;

@RestController
@RequestMapping(ApiConstants.API_V1_PREFIX+"/hello")
public class HelloResource extends GiftListRootController{
    
    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok().body("Hello !! USER OR ADMIN ");
    }

    @PutMapping("/{someChain}")
    public ResponseEntity<String> sayHelloPut(@PathVariable("someChain") String someChain){
        return ResponseEntity.ok().body("Hello !! USER OR ADMIN On PUT "+someChain);
    }

    @GetMapping("adm")
    @Secured({"ADMIN"})
    public ResponseEntity<String> sayHelloAdmin(){
        return ResponseEntity.ok().body("Hello !! ADMIN ");
    }

}
