package com.vbqkma.libarybackend.controller;

import com.vbqkma.libarybackend.dto.*;
import com.vbqkma.libarybackend.model.User;
import com.vbqkma.libarybackend.service.MailService;
import com.vbqkma.libarybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/rest/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping(path = "/get-all")
    public ResponseEntity getAll(@RequestBody UserDTO userDTO) {
        return userService.getAll(userDTO);
    }

}
