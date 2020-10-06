package com.vbqkma.libarybackend.controller;

import com.vbqkma.libarybackend.dto.ChangePasswordDTO;
import com.vbqkma.libarybackend.dto.ConfirmMailResetPasswordDTO;
import com.vbqkma.libarybackend.dto.LoginDTO;
import com.vbqkma.libarybackend.dto.RegisterDTO;
import com.vbqkma.libarybackend.model.User;
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

    @PostMapping(path = "/confirm-mail-reset-password")
    public ResponseEntity confirmMailResetPassword(@RequestBody ConfirmMailResetPasswordDTO confirmMailResetPasswordDTO) {
        return userService.confirmMailResetPassword(confirmMailResetPasswordDTO);
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

    @PostMapping(path = "/info")
    public ResponseEntity getInfo(HttpServletRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().build();
        }
       return  userService.getInfo(request);

    }
}
