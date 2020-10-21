package com.vbqkma.libarybackend.service;

import com.vbqkma.libarybackend.dao.PermissionDAO;

import com.vbqkma.libarybackend.model.Group;
import com.vbqkma.libarybackend.model.Permission;
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

    public ResponseEntity saveOrUpdate(Permission permission) {
        try {
            Permission bo = permissionDAO.findPermissionByNameIgnoreCase(permission.getName());
            if (bo != null) {
                return ResponseEntity.ok().body(new SimpleResponse("ERROR", "Tên tính năng đã tồn tại", null));
            }
            bo = permissionDAO.findPermissionByCodeIgnoreCase(permission.getName());
            if (bo != null) {
                return ResponseEntity.ok().body(new SimpleResponse("ERROR", "Mã tính năng đã tồn tại", null));
            }
            permissionDAO.save(permission);
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Thêm tính năng thành công", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));
        }
    }
}
