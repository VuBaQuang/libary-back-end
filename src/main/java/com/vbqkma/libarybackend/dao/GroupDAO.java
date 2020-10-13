package com.vbqkma.libarybackend.dao;

import com.vbqkma.libarybackend.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupDAO extends JpaRepository<Group, Long> {
    public Group findGroupByName(String name);
    public void deleteByIdIn(List<Long> ids);
}
