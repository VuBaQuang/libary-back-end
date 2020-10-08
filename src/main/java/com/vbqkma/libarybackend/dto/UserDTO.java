package com.vbqkma.libarybackend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data

public class UserDTO extends SimpleDTO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String token;
    private String name;
    private String avatar;
    private String roles;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, String phone, String address, String token, String name, String avatar, String roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.token = token;
        this.name = name;
        this.avatar = avatar;
        this.roles = roles;
    }
}
