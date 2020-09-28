package com.vbqkma.libarybackend.dto;

import lombok.Data;

@Data

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String token;
    private String name;
    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, String phone, String address, String token, String name) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.token = token;
        this.name=name;
    }
}
