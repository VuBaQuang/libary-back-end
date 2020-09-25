package com.vbqkma.libarybackend.controller;

import com.vbqkma.libarybackend.dto.LoginDTO;
import com.vbqkma.libarybackend.model.User;
import com.vbqkma.libarybackend.response.GenerateResponse;
import com.vbqkma.libarybackend.response.GetDetailResponse;
import com.vbqkma.libarybackend.response.SimpleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;


@Controller
@RequestMapping("/api")
public class AuthController {
    @PostMapping(path = "/login")
    public @ResponseBody
    GetDetailResponse<User> login(@RequestBody LoginDTO loginDTO) {


return GenerateResponse.generateSuccessGetDetailResponse(new User());
//        return null;
    }
}