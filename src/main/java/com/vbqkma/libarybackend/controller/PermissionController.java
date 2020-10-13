package com.vbqkma.libarybackend.controller;


import com.vbqkma.libarybackend.service.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/rest/permissions")
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    @PostMapping(path = "/get-all")
    public ResponseEntity getAll() {
        return permissionService.getAll();
    }


}
