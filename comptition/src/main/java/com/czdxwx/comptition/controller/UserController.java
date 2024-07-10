package com.czdxwx.comptition.controller;

import com.czdxwx.comptition.entity.Result;
import com.czdxwx.comptition.model.User;
import com.czdxwx.comptition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/validate")
    public ResponseEntity<?> login(@RequestBody User user) {
        if(userService.validateUser(user)){
            return ResponseEntity.ok().body("{\"message\":\"Login successful\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"Login failed\"}");
        }
    }
}
