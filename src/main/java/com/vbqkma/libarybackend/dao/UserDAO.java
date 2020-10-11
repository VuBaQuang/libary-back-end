package com.vbqkma.libarybackend.dao;
import com.vbqkma.libarybackend.model.Group;
import com.vbqkma.libarybackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
    public User findUserByUsernameAndEmail(String name, String email);
    public User findUserByUsername(String name);
    public User findUserByEmail(String email);
    public User findUserById(Long id);
    public Page<User> findDistinctUserByGroupsIn(Set<Group> groups, Pageable pageable);
    public Page<User> findAllByNameContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(String name, String username, String email, String phone, Pageable pageable);
}
