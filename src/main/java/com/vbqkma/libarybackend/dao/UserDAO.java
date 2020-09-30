package com.vbqkma.libarybackend.dao;

import com.vbqkma.libarybackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
    public User findUserByUsername(String name);

    public User findUserById(Long id);
}
