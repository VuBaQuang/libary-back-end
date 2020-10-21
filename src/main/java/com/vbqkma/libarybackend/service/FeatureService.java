package com.vbqkma.libarybackend.service;

import com.vbqkma.libarybackend.config.jwt.JwtTokenProvider;
import com.vbqkma.libarybackend.config.jwt.UserJwtDetails;
import com.vbqkma.libarybackend.dao.FeatureDAO;
import com.vbqkma.libarybackend.dao.UserDAO;
import com.vbqkma.libarybackend.dto.ChangePasswordDTO;
import com.vbqkma.libarybackend.dto.LoginDTO;
import com.vbqkma.libarybackend.dto.RegisterDTO;
import com.vbqkma.libarybackend.dto.UserDTO;
import com.vbqkma.libarybackend.model.Feature;
import com.vbqkma.libarybackend.model.Group;
import com.vbqkma.libarybackend.model.Permission;
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
public class FeatureService {
    private static final Logger logger = LogManager.getLogger(FeatureService.class);
    @Autowired
    FeatureDAO featureDAO;

    public ResponseEntity getAll() {
        try {
            return ResponseEntity.ok().body(new SimpleResponse("GET_SUCCESS", "", featureDAO.findAll()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));
        }
    }
    public ResponseEntity saveOrUpdate(Feature feature) {
        try {
            Feature bo = featureDAO.findFeatureByNameIgnoreCase(feature.getName());
            if (bo != null) {
                return ResponseEntity.ok().body(new SimpleResponse("ERROR", "Tên quyền đã tồn tại", null));
            }
            bo = featureDAO.findFeatureByCodeIgnoreCase(feature.getCode());
            if (bo != null) {
                return ResponseEntity.ok().body(new SimpleResponse("ERROR", "Mã quyền đã tồn tại", null));
            }
            featureDAO.save(feature);
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Thêm quyền thành công", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));
        }
    }
}
