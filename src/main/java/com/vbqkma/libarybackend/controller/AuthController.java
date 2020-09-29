package com.vbqkma.libarybackend.controller;

import com.vbqkma.libarybackend.dto.ChangePasswordDTO;
import com.vbqkma.libarybackend.dto.LoginDTO;
import com.vbqkma.libarybackend.dto.RegisterDTO;
import com.vbqkma.libarybackend.model.User;
import com.vbqkma.libarybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rest/auth")
public class AuthController {

    @Autowired
    UserService userService;
    @PostMapping(path = "/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @PostMapping(path = "/change-password")
    public ResponseEntity changePassword(@RequestBody ChangePasswordDTO dto) {
        return userService.changePassword(dto);
    }

    @PostMapping(path = "/register")
    public ResponseEntity register(@RequestBody RegisterDTO body) {
        return userService.register(body);
    }

    @PostMapping(path = "/info")
    public ResponseEntity getInfo(@RequestBody User user) {
        ResponseEntity re = userService.getInfo(user.getId());
        return re;
    }
}
