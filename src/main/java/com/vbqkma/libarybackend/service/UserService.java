package com.vbqkma.libarybackend.service;

import com.vbqkma.libarybackend.config.jwt.JwtTokenProvider;
import com.vbqkma.libarybackend.config.jwt.UserJwtDetails;
import com.vbqkma.libarybackend.dao.BookDAO;
import com.vbqkma.libarybackend.dao.BorrowBookDAO;
import com.vbqkma.libarybackend.dao.GroupDAO;
import com.vbqkma.libarybackend.dao.UserDAO;
import com.vbqkma.libarybackend.dto.*;
import com.vbqkma.libarybackend.model.Book;
import com.vbqkma.libarybackend.model.BorrowBook;
import com.vbqkma.libarybackend.model.Group;
import com.vbqkma.libarybackend.model.User;
import com.vbqkma.libarybackend.response.SimpleResponse;
import com.vbqkma.libarybackend.utils.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private GroupDAO groupDAO;

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private BorrowBookDAO borrowBookDAO;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity returnBook(BorrowBook borrowBook, HttpServletRequest request) {
        String token = getToken(request);
        User user = getUserByToken(token);
        borrowBook.setStatus(5L);
        Book s = borrowBook.getBook();
        s.setCount(s.getCount() + 1);
        bookDAO.save(s);
        borrowBookDAO.save(borrowBook);
        mailService.sendMail(
                user.getEmail(),
                "Trả sách",
                "Trả sách thành công",
                user.getUsername() + " - " + user.getName(),
                "",
                "Liên hệ quản trị viên",
                "https://www.facebook.com/profile.php?id=100013548901162"

        );
        return ResponseEntity.ok().body(new SimpleResponse("U_SUCCESS", "", ""));
    }

    public ResponseEntity createNewPassword(UserDTO userDTO) {
        try {
            String token = (String) redisTemplate.opsForValue().get("createNewPassword" + userDTO.getUsername());
            if (token.equals(userDTO.getToken())) {
//                redisTemplate.opsForValue().set("createNewPassword"+ userDTO.getUsername(),userDTO.getToken());
                User user = userDAO.findUserByUsername(userDTO.getUsername());
                user.setPassword(encoder.encode(userDTO.getPassword()));
                userDAO.save(user);
                redisTemplate.delete("createNewPassword" + userDTO.getUsername());
                return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Tạo mật khẩu mới thành công, Đăng nhập lại", null));
            } else {
                return ResponseEntity.ok().body(new SimpleResponse("ERROR", "Xác nhận thất bại, token không chính xác", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));

        }
    }

    public ResponseEntity confirmTokenViaEmail(UserDTO userDTO) {
        try {
            String token = (String) redisTemplate.opsForValue().get("confirmViaMail" + userDTO.getUsername());
            if (token.equals(userDTO.getToken())) {
                redisTemplate.opsForValue().set("createNewPassword" + userDTO.getUsername(), userDTO.getToken());
                redisTemplate.delete("confirmViaMail" + userDTO.getUsername());
                return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Xác nhận thành công", null));
            } else {
                return ResponseEntity.ok().body(new SimpleResponse("ERROR", "Xác nhận thất bại, token không chính xác", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));

        }
    }

    @Transactional
    public ResponseEntity saveOrUpdate(UserDTO userDTO) {
        try {
            if (userDTO.getIsUpdateInfo()) {
                User user = userDAO.findUserById(userDTO.getId());
                if (userDTO.getIsUpdateInfo()) {
                    user.setName(userDTO.getName());
                    user.setEmail(userDTO.getEmail());
                    userDAO.save(user);
                    return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Cập nhật người dùng thành công", null));
                }
            }
            if (userDTO.getUserIds() != null && userDTO.getUserIds().size() > 0) {
                for (int i = 0; i < userDTO.getUserIds().size(); i++) {
                    User user = userDAO.findUserById(userDTO.getUserIds().get(i));
                    Set<Group> groups = user.getGroups();
                    if (userDTO.getIsJoinGroup() != null && userDTO.getIsJoinGroup()) {
                        groups.add(userDTO.getGroup());
                    }
                    if (userDTO.getIsLeaveGroup() != null && userDTO.getIsLeaveGroup()) {
                        groups.remove(userDTO.getGroup());
                    }
                    user.setGroups(groups);

                    Integer lock = user.getIsLock();
                    if (userDTO.getIsLockUsers() != null && userDTO.getIsLockUsers()) {
                        lock = 0;
                    }
                    if (userDTO.getIsUnlockUsers() != null && userDTO.getIsUnlockUsers()) {
                        lock = 1;
                    }
                    user.setIsLock(lock);
                    userDAO.save(user);
                }
                return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Cập nhật người dùng thành công", null));
            }
            User model = new User(null, userDTO.getUsername(), null, userDTO.getName(), userDTO.getEmail(), userDTO.getPhone(), userDTO.getAddress());

            if (userDTO.getGroups() != null && userDTO.getGroups().size() > 0) {
                Set<Group> groups = new HashSet<>();
                for (Long id : userDTO.getGroupIds()) {
                    Group group = groupDAO.findById(id).get();
                    groups.add(group);
                }
                model.setGroups(groups);
            }
            model.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif?imageView2/1/w/80/h/80");
            model.setIsLock(1);
            userDAO.save(model);
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Thêm người dùng thành công", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "Cập nhật người dùng thất bại", null));
        }
    }

    @Transactional
    public ResponseEntity deletes(List<User> users) {
        try {
            users.forEach(user -> {
                user.setGroups(new HashSet<>());
            });
            userDAO.saveAll(users);
            userDAO.deleteAll(users);
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Xóa người dùng thành công", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "Xóa người dùng thất bại", null));
        }
    }

    public User findUserByUsername(String name) {
        return userDAO.findUserByUsername(name);
    }

    public ResponseEntity getAll(UserDTO userDTO) {
        try {
            Pageable pageable = PageRequest.of(userDTO.getPage() > 0 ? userDTO.getPage() - 1 : 0, userDTO.getPageSize() > 0 ? userDTO.getPageSize() : 10);
            if (userDTO.getGroups() != null && userDTO.getGroups().size() > 0) {
                return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Lấy thông tin người dùng thành công", userDAO.findDistinctUserByGroupsIn(userDTO.getGroups(), pageable)));
            }
            if (!StringUtils.isEmpty(userDTO.getName()) || !StringUtils.isEmpty(userDTO.getUsername()) || !StringUtils.isEmpty(userDTO.getEmail()) || !StringUtils.isEmpty(userDTO.getPhone())) {
                return ResponseEntity.ok().body(
                        new SimpleResponse("SUCCESS", "Lấy thông tin người dùng thành công",
                                userDAO.
                                        findAllByNameContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                                                userDTO.getName(),
                                                userDTO.getUsername(),
                                                userDTO.getEmail(),
                                                userDTO.getPhone(),
                                                pageable)));
            }
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Lấy thông tin người dùng thành công", userDAO.findAll(pageable)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));
        }

    }

    @Transactional
    public ResponseEntity borrowBook(UserDTO userDTO) {
        try {

            User user = userDAO.findUserByUsername(userDTO.getUsername());
            Book book = bookDAO.getOne(userDTO.getBook().getId());
            if (book.getCount() <= 0) {
                return ResponseEntity.ok().body(new SimpleResponse("ERROR", "Số lượng sách đã hết", ""));
            }
            if (user != null) {
                List<BorrowBook> borrowBooks = borrowBookDAO.findBorrowBooksByUser(user);
                for (BorrowBook borrowBook : borrowBooks) {
                    if (borrowBook.getBook().getId() == userDTO.getBook().getId() && (borrowBook.getStatus() == 1 || borrowBook.getStatus() == 2 || borrowBook.getStatus() == 3)) {
                        return ResponseEntity.ok().body(new SimpleResponse("ERROR", "Tài khoản của bạn đã mượn sách này !", ""));
                    }
                }

                BorrowBook borrowBook = new BorrowBook();
                borrowBook.setBook(userDTO.getBook());
                borrowBook.setUser(user);
                borrowBook.setStatus(2L);
                borrowBookDAO.save(borrowBook);
//
//                Set<Book> books = new HashSet<>();
//                books = user.getBooks();
//                books.add(userDTO.getBook());
//                user.setBooks(books);
//                userDAO.save(user);


                book.setCount(book.getCount() - 1);
                book.setBorrowed(book.getBorrowed() + 1);
                bookDAO.save(book);

                return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Mượn sách thành công", null));
            } else {
                return ResponseEntity.ok().body(new SimpleResponse("ERROR", "User is invalid", ""));
            }

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

    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = getToken(request);
            User user = getUserByToken(token);
            String jwtTokenOld = (String) redisTemplate.opsForValue().get(user.getId().toString());
            String jwtAuthOld = (String) redisTemplate.opsForValue().get(user.getUsername());

            if (jwtAuthOld != null) {
                redisTemplate.delete(user.getId().toString());
                redisTemplate.delete(jwtAuthOld);
                logger.info("Clear jwt auth old success !");
            }

            if (jwtTokenOld != null) {
                redisTemplate.delete(user.getUsername());
                redisTemplate.delete(jwtTokenOld);
                logger.info("Clear jwt token old success !");
            }
            Cookie cookie_token = new Cookie("Kma-Token", null);
            cookie_token.setMaxAge(0);
            cookie_token.setHttpOnly(true);
            cookie_token.setPath("/");

            Cookie cookie_auth = new Cookie("Kma-Auth", null);
            cookie_auth.setMaxAge(0);
            cookie_auth.setHttpOnly(true);
            cookie_auth.setPath("/");
            response.addCookie(cookie_token);
            response.addCookie(cookie_auth);
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "logout_success", ""));
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
            User user = getUserByToken(token);
            List<BorrowBook> borrowBooks = borrowBookDAO.findBorrowBooksByUser(user);
            UserDTO result = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(), user.getAddress(), null, user.getName(), user.getAvatar(), user.getIsLock(), user.getCreatedAt());
            result.setBorrowBooks(borrowBooks);
            result.setGroups(user.getGroups());
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "detail_success", result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse("error", "server_error", null));
        }

    }

    public ResponseEntity register(RegisterDTO user) {
        try {
            User model = new User(null, user.getUsername(), user.getPassword(), user.getName(), user.getEmail(), user.getPhone(), user.getAddress());
            model.setIsLock(1);
            model.setPassword(encoder.encode(user.getPassword()));
            model.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif?imageView2/1/w/80/h/80");
            model.setName(user.getUsername());
            Group group = groupDAO.findById(5L).get();
            Set<Group> groups = new HashSet<>();
            groups.add(group);
            model.setGroups(groups);
            userDAO.save(model);
            mailService.sendMail(
                    user.getEmail(),
                    "Đổi mật khẩu",
                    "Đăng ký thành công",
                    user.getUsername() + " - " + user.getName(),
                    "",
                    "Đăng nhập",
                    "https://localhost:8051/login"

            );
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "register_success", model));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SimpleResponse("error", "server_error", null));
        }
    }

    public ResponseEntity login(LoginDTO loginDTO, HttpServletResponse response) {
        try {
            User user = userDAO.findUserByUsername(loginDTO.getUsername());
            if (user != null) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                if (user.getIsLock() != 1) {
                    return ResponseEntity.ok().body(new SimpleResponse("ERROR", "Tài khoản bị khóa", null));
                }
                UserJwtDetails userDetails = (UserJwtDetails) authentication.getPrincipal();

                String jwtToken = tokenProvider.generateTokenByid(userDetails);
                String jwtAuth = tokenProvider.generateTokenByUsername(userDetails);
                logger.info("Generate Token success !");

                String jwtTokenOld = (String) redisTemplate.opsForValue().get(user.getId().toString());
                String jwtAuthOld = (String) redisTemplate.opsForValue().get(user.getUsername());
                if (jwtAuthOld != null) {
                    redisTemplate.delete(user.getId().toString());
                    redisTemplate.delete(jwtAuthOld);
                    logger.info("Clear jwt auth old success !");
                }
                if (jwtTokenOld != null) {
                    redisTemplate.delete(user.getUsername());
                    redisTemplate.delete(jwtTokenOld);
                    logger.info("Clear jwt token old success !");
                }
                redisTemplate.opsForValue().set(jwtToken, user.getId(), Duration.ofHours(1));
                redisTemplate.opsForValue().set(jwtAuth, jwtToken, Duration.ofHours(1));

                redisTemplate.opsForValue().set(user.getId().toString(), jwtToken, Duration.ofHours(1));
                redisTemplate.opsForValue().set(user.getUsername(), jwtAuth, Duration.ofHours(1));

                UserDTO result = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(), user.getAddress(), jwtToken, user.getName(), user.getAvatar(), user.getIsLock(), user.getCreatedAt());

                Cookie cookie_token = new Cookie("Kma-Token", jwtToken);
                cookie_token.setMaxAge(3600);
                cookie_token.setSecure(false);
                cookie_token.setHttpOnly(false);
                cookie_token.setPath("/");

                Cookie cookie_auth = new Cookie("Kma-Auth", jwtAuth);
                cookie_auth.setMaxAge(3600);
                cookie_auth.setSecure(true);
                cookie_auth.setHttpOnly(true);
                cookie_auth.setPath("/");

                response.addCookie(cookie_token);
                response.addCookie(cookie_auth);

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
        try {
            String jwt = tokenProvider.generateTokenFromString(RandomStringUtils.random(8, true, true));
            redisTemplate.delete("confirmViaMail" + userDTO.getUsername());
            redisTemplate.opsForValue().set("confirmViaMail" + userDTO.getUsername(), jwt, Duration.ofMinutes(15));
            User user = userDAO.findUserByUsername(userDTO.getUsername());
            mailService.sendMail(
                    userDTO.getEmail(),
                    "Reset mật khẩu",
                    "Xác nhận reset mật khẩu",
                    userDTO.getUsername() + " - " + user.getName(),
                    "Mã xác nhận của bạn là: " + jwt + ". Nếu bạn không thực hiện hành động này vui lòng liên hệ với quản trị viên ngay bây giờ.",
                    "Liên hệ quản trị viên",
                    "https://www.facebook.com/profile.php?id=100013548901162"
            );
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Gửi lại email thành công", null));
        } catch (Exception e) {
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
                redisTemplate.opsForValue().set("confirmViaMail" + user.getUsername(), jwt, Duration.ofMinutes(15));
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
