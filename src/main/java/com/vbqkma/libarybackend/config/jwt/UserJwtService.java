package com.vbqkma.libarybackend.config.jwt;


import com.vbqkma.libarybackend.model.User;
import com.vbqkma.libarybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserJwtService implements UserDetailsService {

  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) {
    // Kiểm tra xem user có tồn tại trong database không?
    User user = userService.findUserByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    return new UserJwtDetails(user);
  }

  // doFilter sẽ sử dụng hàm này
  @Transactional
  public UserDetails loadUserById(Long id) {
    User user = userService.findUserById(id);
    if (user != null)
      return new UserJwtDetails(user);
    else
      return null;
  }

}
