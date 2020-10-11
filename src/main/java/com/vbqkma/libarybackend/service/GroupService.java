package com.vbqkma.libarybackend.service;

import com.vbqkma.libarybackend.dao.GroupDAO;
import com.vbqkma.libarybackend.dto.GroupDTO;
import com.vbqkma.libarybackend.response.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
    @Autowired
    GroupDAO groupDAO;

    public ResponseEntity getAll(GroupDTO groupDTO) {
        try {
            Pageable pageable = PageRequest.of(groupDTO.getPage() > 0 ? groupDTO.getPage() - 1 : 0, groupDTO.getPageSize() > 0 ? groupDTO.getPageSize() : 10);
            return ResponseEntity.ok().body(new SimpleResponse("GET_SUCCESS", "", groupDAO.findAll(pageable)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));
        }

    }
}
