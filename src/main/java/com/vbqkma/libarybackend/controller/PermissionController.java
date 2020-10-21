package com.vbqkma.libarybackend.controller;


import com.vbqkma.libarybackend.model.Permission;
import com.vbqkma.libarybackend.service.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/rest/permissions")
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    @PostMapping(path = "/get-all")
    public ResponseEntity getAll() {
        return permissionService.getAll();
    }

    @PostMapping
    public ResponseEntity saveOrUpdate(@RequestBody Permission permission) {
        return permissionService.saveOrUpdate(permission);
    }

}
