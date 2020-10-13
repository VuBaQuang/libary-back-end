package com.vbqkma.libarybackend.service;

import com.vbqkma.libarybackend.dao.GroupDAO;
import com.vbqkma.libarybackend.dto.GroupDTO;
import com.vbqkma.libarybackend.model.Group;
import com.vbqkma.libarybackend.response.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    GroupDAO groupDAO;

    public ResponseEntity getAll(GroupDTO dto) {
        try {
            Pageable pageable;
            if (dto.getSortField() == null || (dto.getSortField().length() <= 0)) {
                pageable = PageRequest.of(dto.getPage() > 0 ? dto.getPage() - 1 : 0, dto.getPageSize() > 0 ? dto.getPageSize() : 10);
            } else {
                if (dto.getConditionSort()) {
                    pageable = PageRequest.of(dto.getPage() > 0 ? dto.getPage() - 1 : 0, dto.getPageSize() > 0 ? dto.getPageSize() : 10, Sort.by(dto.getSortField()).ascending());
                } else {
                    pageable = PageRequest.of(dto.getPage() > 0 ? dto.getPage() - 1 : 0, dto.getPageSize() > 0 ? dto.getPageSize() : 10, Sort.by(dto.getSortField()).descending());
                }
            }
            return ResponseEntity.ok().body(new SimpleResponse("GET_SUCCESS", "", groupDAO.findAll(pageable)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));
        }
    }

    public ResponseEntity saveOrUpdate(Group group) {
        try {
            Group bo = groupDAO.findGroupByName(group.getName());
            if (bo != null) {
                return ResponseEntity.ok().body(new SimpleResponse("ERROR", "Tên nhóm đã tồn tại", null));
            }
            groupDAO.save(group);
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Cập nhật thông tin nhóm thành công", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));
        }
    }
    @Transactional
    public ResponseEntity deletes(List<Long> ids) {
        try {
            groupDAO.deleteByIdIn(ids);
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Xóa nhóm thành công", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));
        }
    }
}
