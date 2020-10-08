package com.vbqkma.libarybackend.dao;
import com.vbqkma.libarybackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
    public User findUserByUsernameAndEmail(String name, String email);
    public User findUserByUsername(String name);
    public User findUserByEmail(String email);
    public User findUserById(Long id);
}
