package com.vbqkma.libarybackend.controller;

import com.vbqkma.libarybackend.dto.UserDTO;
import com.vbqkma.libarybackend.model.User;
import com.vbqkma.libarybackend.service.FeatureService;
import com.vbqkma.libarybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/rest/features")
public class FeatureController {
    @Autowired
    FeatureService featureService;

    @PostMapping(path = "/get-all")
    public ResponseEntity getAll() {
        return featureService.getAll();
    }


}
