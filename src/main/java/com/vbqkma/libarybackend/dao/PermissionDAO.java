package com.vbqkma.libarybackend.dao;
import com.vbqkma.libarybackend.model.Group;
import com.vbqkma.libarybackend.model.Permission;
import com.vbqkma.libarybackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PermissionDAO extends JpaRepository<Permission, Long> {
}
