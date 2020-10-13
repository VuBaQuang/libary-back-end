package com.vbqkma.libarybackend.controller;

import com.vbqkma.libarybackend.dto.GroupDTO;
import com.vbqkma.libarybackend.dto.UserDTO;
import com.vbqkma.libarybackend.model.Group;
import com.vbqkma.libarybackend.service.GroupService;
import com.vbqkma.libarybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rest/groups")
public class GroupController {
    @Autowired
    GroupService groupService;

    @PostMapping(path = "/get-all")
    public ResponseEntity getAll(@RequestBody GroupDTO groupDTO) {
        return groupService.getAll(groupDTO);
    }

    @PostMapping
    public ResponseEntity<GroupDTO> saveOrUpdate(@RequestBody Group group) {
        return groupService.saveOrUpdate(group);
    }
//
//    @PostMapping("/get")
//    public ResponseEntity<GroupDTO> findById(@RequestBody GroupForm groupForm) {
//        return groupService.findById(groupForm);
//    }
//
//    @PostMapping("/search")
//    public ResponseEntity searchByName(@RequestBody GroupForm groupForm) {
//        return groupService.searchByName(groupForm);
//    }
//
    @PostMapping("/deletes")
    public ResponseEntity deletes(@RequestBody List<Long> ids){
        return  groupService.deletes(ids);
    }
}
