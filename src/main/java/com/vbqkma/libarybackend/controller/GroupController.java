package com.vbqkma.libarybackend.controller;

import com.vbqkma.libarybackend.dto.GroupDTO;
import com.vbqkma.libarybackend.dto.UserDTO;
import com.vbqkma.libarybackend.service.GroupService;
import com.vbqkma.libarybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rest/groups")
public class GroupController {
    @Autowired
    GroupService groupService;

    @PostMapping(path = "/get-all")
    public ResponseEntity getAll(@RequestBody GroupDTO groupDTO) {
        return groupService.getAll(groupDTO);
    }

}
