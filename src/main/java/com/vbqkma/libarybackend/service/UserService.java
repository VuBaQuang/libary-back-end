package com.vbqkma.libarybackend.service;

import com.vbqkma.libarybackend.config.jwt.JwtTokenProvider;
import com.vbqkma.libarybackend.config.jwt.UserJwtDetails;
import com.vbqkma.libarybackend.dao.UserDAO;
import com.vbqkma.libarybackend.dto.LoginDTO;
import com.vbqkma.libarybackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    public User findUserByUsername(String name) {
        return userDAO.findUserByUsername(name);
    }

    public User findUserById(Long id) {
        return userDAO.findUserById(id);
    }

    public ResponseEntity register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userDAO.save(user);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserJwtDetails userDetails = (UserJwtDetails) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(userDetails);
        return ResponseEntity.ok("1");
    }
}
