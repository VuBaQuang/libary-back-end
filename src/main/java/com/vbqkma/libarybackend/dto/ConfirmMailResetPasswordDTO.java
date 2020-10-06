package com.vbqkma.libarybackend.dto;

import lombok.Data;

@Data
public class ConfirmMailResetPasswordDTO {
    private String username;
    private String mail;
}
