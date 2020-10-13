package com.vbqkma.libarybackend.controller;

import com.vbqkma.libarybackend.dto.UserDTO;
import com.vbqkma.libarybackend.model.User;
import com.vbqkma.libarybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rest/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping(path = "/get-all")
    public ResponseEntity getAll(@RequestBody UserDTO userDTO) {
        return userService.getAll(userDTO);
    }

    @PostMapping(path = "")
    public ResponseEntity saveOrUpdate(@RequestBody UserDTO userDTO) {
        return userService.saveOrUpdate(userDTO);
    }

    @PostMapping(path = "/deletes")
    public ResponseEntity deletes(@RequestBody List<User> users) {
        return userService.deletes(users);
    }

}
