package com.vbqkma.libarybackend.service;

import com.vbqkma.libarybackend.config.jwt.JwtTokenProvider;
import com.vbqkma.libarybackend.config.jwt.UserJwtDetails;
import com.vbqkma.libarybackend.dao.UserDAO;
import com.vbqkma.libarybackend.dto.ChangePasswordDTO;
import com.vbqkma.libarybackend.dto.LoginDTO;
import com.vbqkma.libarybackend.dto.RegisterDTO;
import com.vbqkma.libarybackend.dto.UserDTO;
import com.vbqkma.libarybackend.model.User;
import com.vbqkma.libarybackend.response.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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

    public ResponseEntity getInfo(Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userDAO.findUserById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error Message");
        }

    }

    public ResponseEntity register(RegisterDTO user) {
        try {
//            user.setPassword();
            User model = new User(null, user.getUsername(), user.getPassword(), user.getName(), user.getEmail(), user.getPhone(), user.getAddress(), "['admin]");
            model.setPassword(encoder.encode(user.getPassword()));
            userDAO.save(model);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error Message");

        }
    }

    public ResponseEntity login(LoginDTO loginDTO) {
        try {
            User user = userDAO.findUserByUsername(loginDTO.getUsername());
            if (user != null) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserJwtDetails userDetails = (UserJwtDetails) authentication.getPrincipal();
                String jwt = tokenProvider.generateToken(userDetails);
                UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(), user.getAddress(), jwt, user.getName());
                return ResponseEntity.status(HttpStatus.OK).body(userDTO);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error Message");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error Message");
        }
    }

    public ResponseEntity changePassword(ChangePasswordDTO dto) {
        try {
            User user = userDAO.findUserByUsername(dto.getUsername());
            if (user != null) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
                user.setPassword(encoder.encode(dto.getNewPassword()));
                userDAO.save(user);
                return ResponseEntity.status(HttpStatus.OK).body(user);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error Message");
            }
        } catch (BadCredentialsException b){
            return ResponseEntity.status(200).body(new SimpleResponse("error","Password invalid"));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error Message");
        }
    }
}
