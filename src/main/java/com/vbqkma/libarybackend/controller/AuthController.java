package com.vbqkma.libarybackend.controller;

import com.vbqkma.libarybackend.dto.LoginDTO;
import com.vbqkma.libarybackend.model.User;
import com.vbqkma.libarybackend.response.GenerateResponse;
import com.vbqkma.libarybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rest/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        userService.login(loginDTO);
        return new ResponseEntity<>("success", null, HttpStatus.OK);
    }

    @PostMapping(path = "/register")
    public ResponseEntity register(@RequestBody User body) {
        return userService.register(body);
    }
}
