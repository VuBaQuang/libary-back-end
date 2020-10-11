package com.vbqkma.libarybackend.dao;

import com.vbqkma.libarybackend.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupDAO extends JpaRepository<Group, Long> {
}
