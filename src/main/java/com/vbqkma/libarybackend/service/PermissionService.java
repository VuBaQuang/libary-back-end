package com.vbqkma.libarybackend.service;

import com.vbqkma.libarybackend.dao.PermissionDAO;

import com.vbqkma.libarybackend.response.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
    @Autowired
    PermissionDAO permissionDAO;

    public ResponseEntity getAll() {
        try {
            return ResponseEntity.ok().body(new SimpleResponse("GET_SUCCESS", "", permissionDAO.findAll()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));
        }

    }
}
