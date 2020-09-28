package com.vbqkma.libarybackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://localhost:8051")
@Controller
@RequestMapping("/rest/auth")
public class AuthController {
    @PostMapping(path = "/login")
    public ResponseEntity<String> login() {
        System.out.println("POST");
        return new ResponseEntity<>("success", null, HttpStatus.OK);
    }
    @GetMapping(path = "/login")
    public ResponseEntity<String> log() {
        System.out.println("GET");
        return new ResponseEntity<>("<h1>Vũ Bá Quang - AT130444 - Get cookie success !</h1>", null, HttpStatus.OK);
    }
}
