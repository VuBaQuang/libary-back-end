package com.vbqkma.libarybackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
//@RequestMapping("/rest/v1/schedules")
public class TestController {

    @GetMapping(path = "/")
    public @ResponseBody
    ResponseEntity<byte[]> login() {
        return new ResponseEntity<>(null, null, HttpStatus.OK);
    }
}