package com.vbqkma.libarybackend.config.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vbqkma.libarybackend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
public class UserJwtDetails implements UserDetails {

  User user;
  @JsonIgnore
  public Collection<? extends GrantedAuthority> authorities;
  @Override
  public String getPassword() {
    return user.getPassword();
  }
  @Override
  public String getUsername() {
    return user.getUsername();
  }
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }
  public UserJwtDetails(User user) {
    this.user = user;
  }
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
  @Override
  public boolean isEnabled() {
    return true;
  }
}
