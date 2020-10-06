package com.vbqkma.libarybackend.controller;

import com.vbqkma.libarybackend.dto.*;
import com.vbqkma.libarybackend.service.MailService;
import com.vbqkma.libarybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/rest/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    MailService mailService;

    @PostMapping(path = "/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @PostMapping(path = "/confirm-via-mail")
    public ResponseEntity confirmViaMail(@RequestBody ConfirmMailResetPasswordDTO confirmMailResetPasswordDTO) {
        return userService.confirmViaMail(confirmMailResetPasswordDTO);
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
    public ResponseEntity logout(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        return userService.logout(userDTO, request);
    }

    @PostMapping(path = "/info")
    public ResponseEntity getInfo(HttpServletRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().build();
        }
        return userService.getInfo(request);

    }
}
