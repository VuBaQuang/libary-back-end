package com.vbqkma.libarybackend.service;

import com.vbqkma.libarybackend.config.jwt.JwtTokenProvider;
import com.vbqkma.libarybackend.config.jwt.UserJwtDetails;
import com.vbqkma.libarybackend.dao.UserDAO;
import com.vbqkma.libarybackend.dto.*;
import com.vbqkma.libarybackend.model.User;
import com.vbqkma.libarybackend.response.SimpleResponse;
import com.vbqkma.libarybackend.utils.EmailUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    public User findUserByUsername(String name) {
        return userDAO.findUserByUsername(name);
    }

    public ResponseEntity getAll(UserDTO userDTO) {
        try {
            Pageable pageable = PageRequest.of(userDTO.getPage() > 0 ? userDTO.getPage() - 1 : 0, userDTO.getPageSize() > 0 ? userDTO.getPageSize() : 10);
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Lấy thông tin người dùng thành công", userDAO.findAll(pageable)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));
        }

    }

    public ResponseEntity checkExist(UserDTO userDTO) {
        if (findUserByUsername(userDTO.getUsername()) != null) {
            return ResponseEntity.ok().body(new SimpleResponse("EXIST", "Tên tài khoản đã tồn tại", ""));
        }
        if (userDAO.findUserByEmail(userDTO.getEmail()) != null) {
            return ResponseEntity.ok().body(new SimpleResponse("EXIST", "Email đã có tài khoản sử dụng", ""));
        }
        return ResponseEntity.ok().body(new SimpleResponse("NOT_EXIST", "", ""));
    }

    public User findUserById(Long id) {
        return userDAO.findUserById(id);
    }

    public ResponseEntity logout(UserDTO userDTO, HttpServletRequest request) {
        try {
            String token = getToken(request);
            String tokenInMemory = (String) redisTemplate.opsForValue().get(userDTO.getUsername());
            if (tokenInMemory != null) {
                if (tokenInMemory.equals(token)) {
                    logger.info("TOKEN DELETED: " + redisTemplate.opsForValue().get(userDTO.getUsername()));
                    redisTemplate.delete(token);
                    redisTemplate.delete(userDTO.getUsername());
                    return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "logout_success", ""));
                }
            }
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "logout_fail", ""));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));
        }
    }

    public String getToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String accessToken = "";
        if (auth != null && auth.split(" ").length > 1) {
            accessToken = auth.split(" ")[1];
        }
        return accessToken;
    }

    public ResponseEntity getInfo(HttpServletRequest request) {
        String token = getToken(request);
        logger.info("ACCESS TOKEN: " + token);
        try {
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "detail_success", getUserByToken(token)));
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
                userDAO.save(user);
                String jwtOld = (String) redisTemplate.opsForValue().get(user.getUsername());
                if (jwtOld != null) {
                    logger.info("TOKEN OLD: " + redisTemplate.opsForValue().get(user.getUsername()));
                    System.out.println(redisTemplate.opsForValue().get(jwtOld));
                    redisTemplate.delete(user.getUsername());
                    redisTemplate.delete(jwtOld);
                }
                redisTemplate.opsForValue().set(jwt, tokenProvider.getTokenExpiryFromJWT(jwt));
                redisTemplate.opsForValue().set(user.getUsername(), jwt);
                logger.info("TOKEN NEW: " + redisTemplate.opsForValue().get(user.getUsername()));
                UserDTO result = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(), user.getAddress(), jwt, user.getName(), user.getAvatar(), user.getRoles());
                return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "login_success", result));
            } else {
                return ResponseEntity.ok().body(new SimpleResponse("ERROR", "login_fail", null));
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "login_fail", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse("ERROR", "server_error", null));
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
            return user;
        }
        return null;
    }

    public ResponseEntity sendEmailAgain(UserDTO userDTO) {
        try{
            String jwt = tokenProvider.generateTokenFromString(RandomStringUtils.random(8, true, true));
            redisTemplate.delete("confirmViaMail" + userDTO.getUsername());
            redisTemplate.opsForValue().set("confirmViaMail" + userDTO.getUsername(), jwt);
            mailService.sendMail(
                    userDTO.getEmail(),
                    "Reset mật khẩu",
                    "Xác nhận reset mật khẩu",
                    userDTO.getUsername() + " - " + userDTO.getName(),
                    "Mã xác nhận của bạn là: " + jwt + ". Nếu bạn không thực hiện hành động này vui lòng liên hệ với quản trị viên ngay bây giờ.",
                    "Liên hệ quản trị viên",
                    "https://www.facebook.com/profile.php?id=100013548901162"
            );
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Gửi lại email thành công", null));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "Gửi lại email thất bại", null));
        }

    }

    public ResponseEntity confirmUserEmail(UserDTO userDTO) {
        try {
            User user = userDAO.findUserByUsernameAndEmail(userDTO.getUsername(), userDTO.getEmail());
            if (user != null) {
                String code = RandomStringUtils.random(8, true, true);
                String jwt = tokenProvider.generateTokenFromString(code);
                redisTemplate.opsForValue().set("confirmViaMail" + user.getUsername(), jwt);
                mailService.sendMail(
                        user.getEmail(),
                        "Reset mật khẩu",
                        "Xác nhận reset mật khẩu",
                        user.getUsername() + " - " + user.getName(),
                        "Mã xác nhận của bạn là: " + jwt + ". Nếu bạn không thực hiện hành động này vui lòng liên hệ với quản trị viên ngay bây giờ.",
                        "Liên hệ quản trị viên",
                        "https://www.facebook.com/profile.php?id=100013548901162"
                );
                return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Đã gửi email, vui lòng nhập mã xác nhận từ email", ""));
            }
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "confirm_error", ""));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));
        }
    }
}
