package com.vbqkma.libarybackend.service;

import com.vbqkma.libarybackend.config.jwt.JwtTokenProvider;
import com.vbqkma.libarybackend.config.jwt.UserJwtDetails;
import com.vbqkma.libarybackend.dao.UserDAO;
import com.vbqkma.libarybackend.dto.*;
import com.vbqkma.libarybackend.model.User;
import com.vbqkma.libarybackend.response.SimpleResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    @Autowired
    UserDAO userDAO;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    MailService mailService;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

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

    public ResponseEntity confirmUserEmail(UserDTO userDTO) {
        try {
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "confirm_success", ""));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "confirm_fail", ""));
        }
    }

    public ResponseEntity getInfo(HttpServletRequest request) {

        String auth = request.getHeader("Authorization");
        String accessToken = "";
        if (auth != null && auth.split(" ").length > 1) {
            accessToken = auth.split(" ")[1];
        }
        logger.info("ACCESS TOKEN: " + accessToken);
        try {
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "detail_success", getUserByToken(accessToken)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse("error", "server_error", null));
        }

    }

    public ResponseEntity register(RegisterDTO user) {
        try {
            User model = new User(null, user.getUsername(), user.getPassword(), user.getName(), user.getEmail(), user.getPhone(), user.getAddress(), "[\"admin\"]");
            model.setPassword(encoder.encode(user.getPassword()));
            userDAO.save(model);
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "register_success", model));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse("error", "server_error", null));
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
                user.setToken(jwt);
                userDAO.save(user);
                System.out.println(redisTemplate.opsForValue().get(user.getUsername()));
                redisTemplate.opsForValue().set(user.getUsername(), jwt);
                System.out.println(redisTemplate.opsForValue().get(user.getUsername()));
//                UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(), user.getAddress(), jwt, user.getName());
                return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "login_success", user));
            } else {
                return ResponseEntity.ok().body(new SimpleResponse("error", "login_fail", null));
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.ok().body(new SimpleResponse("error", "login_fail", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse("error", "server_error", null));
        }
    }

    public ResponseEntity changePassword(ChangePasswordDTO dto) {
        try {
            User user = userDAO.findUserByUsername(dto.getUsername());
            if (user != null) {
                if (!encoder.matches(dto.getPassword(), user.getPassword())) {
                    return ResponseEntity.ok().body(new SimpleResponse("error", "password_is_incorrect", null));
                }
                user.setPassword(encoder.encode(dto.getNewPassword()));
                userDAO.save(user);
                mailService.sendMail(
                        user.getEmail(),
                        "Đổi mật khẩu",
                        "Đổi mật khẩu thành công",
                        user.getUsername() + " - " + user.getName(),
                        "Nếu bạn không thực hiện hành động này vui lòng liên hệ với quản trị viên ngay bây giờ.",
                        "Liên hệ quản trị viên",
                        "https://www.facebook.com/profile.php?id=100013548901162"

                );
                return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "change_password_success", user));
//                return ResponseEntity.status(HttpStatus.OK).body(user);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse("error", "server_error", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse("error", "server_error", null));
        }
    }

    public User getUserByToken(String token) {
        User user = null;
        Long userId = tokenProvider.getUserIdFromJWT(token);
        if (userId != null) {
            user = userDAO.findUserById(userId);
            if (user != null) {
                user.setToken(token);
            }
            return user;
        }
        return null;
    }

    public ResponseEntity confirmMailResetPassword(ConfirmMailResetPasswordDTO confirmMailResetPasswordDTO) {
        User user = userDAO.findUserByUsername(confirmMailResetPasswordDTO.getUsername());
        if (user != null) {
            String code = RandomStringUtils.random(8, true, true);
            if (user.getEmail().equalsIgnoreCase(confirmMailResetPasswordDTO.getMail())) {
                mailService.sendMail(
                        user.getEmail(),
                        "Reset mật khẩu",
                        "Xác nhận reset mật khẩu",
                        user.getUsername() + " - " + user.getName(),
                        "Nếu bạn không thực hiện hành động này vui lòng liên hệ với quản trị viên ngay bây giờ.",
                        "Liên hệ quản trị viên",
                        "https://www.facebook.com/profile.php?id=100013548901162"

                );
            }
        } else {

        }
        return null;
    }
}
