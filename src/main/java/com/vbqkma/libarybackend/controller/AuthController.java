package com.vbqkma.libarybackend.controller;

import com.vbqkma.libarybackend.dto.*;
import com.vbqkma.libarybackend.service.MailService;
import com.vbqkma.libarybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/rest/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    MailService mailService;

    @PostMapping(path = "/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        return userService.login(loginDTO,response);
    }

    @PostMapping(path = "/send-email-again")
    public ResponseEntity sendEmailAgain(@RequestBody UserDTO UserDTO) {
        return userService.sendEmailAgain(UserDTO);
    }

    @PostMapping(path = "/check-exist")
    public ResponseEntity checkExist(@RequestBody UserDTO userDTO) {
        return userService.checkExist(userDTO);
    }

    @PostMapping(path = "/confirm-user-email")
    public ResponseEntity confirmUserEmail(@RequestBody UserDTO userDTO) {
        return userService.confirmUserEmail(userDTO);
    }

    @PostMapping(path = "/change-password")
    public ResponseEntity changePassword(@RequestBody ChangePasswordDTO dto) {
        return userService.changePassword(dto);
    }

    @PostMapping(path = "/register")
    public ResponseEntity register(@RequestBody RegisterDTO body) {
        return userService.register(body);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity logout(HttpServletRequest request,HttpServletResponse response) {
        return userService.logout(request,response);
    }

    @PostMapping(path = "/info")
    public ResponseEntity getInfo(HttpServletRequest request) {
        return userService.getInfo(request);

    }
}
