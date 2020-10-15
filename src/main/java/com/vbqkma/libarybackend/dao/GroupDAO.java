package com.vbqkma.libarybackend.dao;

import com.vbqkma.libarybackend.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupDAO extends JpaRepository<Group, Long> {
    public Group findGroupByName(String name);
    public Page<Group> findGroupsByName(String name, Pageable pageable);
    public void deleteByIdIn(List<Long> ids);
}
