package com.vbqkma.libarybackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String roles;
    private List<Long> groups;
}
