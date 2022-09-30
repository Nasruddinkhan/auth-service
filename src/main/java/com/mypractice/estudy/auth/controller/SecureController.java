package com.mypractice.estudy.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class SecureController {

    @GetMapping
    public String sayHello(){
        return "Hello";
    }

}
