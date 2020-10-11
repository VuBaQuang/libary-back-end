package com.vbqkma.libarybackend.utils;

import com.vbqkma.libarybackend.LibaryBackEndApplication;
import com.vbqkma.libarybackend.config.jwt.JwtTokenProvider;
import com.vbqkma.libarybackend.model.User;
import com.vbqkma.libarybackend.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class AppUtils {
    private static final Logger logger = LogManager.getLogger(AppUtils.class);
    private static final List<String> ALLOW_LIST = Arrays.asList(
            "/rest/auth/login",
            "/favicon.ico"
    );

    public static String getToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null) {
            return "";
        }
        String sessionToken = "";
        if (auth.split(" ").length > 1) {
            sessionToken = auth.split(" ")[1];
        }
        return sessionToken;
    }

    public static Boolean requestValidation(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        String uri = request.getRequestURI();
        if (!StringUtils.isEmpty(uri)) {
            boolean match = ALLOW_LIST.stream().anyMatch(s -> uri.contains(s));
            if (match) {
                return true;
            }
            String token = request.getHeader("Authorization");

            if (StringUtils.isEmpty(token)) {
                logger.info("requestValidation: Authorization not defined (" + uri + ")");
                return false;
            }
            Cookie[] cookies = request.getCookies();
            String auth = "";
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase("Kma-Auth")) {
                    auth = cookie.getValue();
                }
            }
            String sessionToken = "";
            if (token.split(" ").length > 1) {
                sessionToken = token.replace("Basic ", "");
            }
            if (StringUtils.isEmpty(sessionToken) || StringUtils.isEmpty(auth)) {
                logger.info("Validate token --> token empty");
                return false;
            }
            ValueOperations opsVal = LibaryBackEndApplication.ctx.getBean(RedisTemplate.class).opsForValue();
            String jwtTokenByAuth = (String) opsVal.get(auth);
            UserService userService = LibaryBackEndApplication.ctx.getBean(UserService.class);
            Long idUser = Long.parseLong(opsVal.get(sessionToken).toString());
            User user = userService.findUserById(idUser);
            if(!uri.equalsIgnoreCase("/rest/auth/logout")){
                if (user.getIsLock() != 1) {
                    return false;
                }
            }
            return opsVal.get(sessionToken) != null && jwtTokenByAuth != null && jwtTokenByAuth.equals(sessionToken);

        }
        return false;
    }
}
