package com.skaria.aws.data.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/hello")
public class UserController {


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    @PreAuthorize("hasAuthority('MyGreeterLambda')")
    public ResponseEntity sayHello() {

        logger.info("sayHello method called from controller!!");
        return ResponseEntity.ok().body("hello from a secure data service!!");
    }

}