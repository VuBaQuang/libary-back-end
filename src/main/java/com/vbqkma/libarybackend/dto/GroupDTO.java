package com.vbqkma.libarybackend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GroupDTO  extends  SimpleDTO{
    private Long id;
    private String name;
    private String code;
    private String roles;
    private String rolesJson;
    private String searchName;
}
