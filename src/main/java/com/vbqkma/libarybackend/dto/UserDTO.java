package com.vbqkma.libarybackend.dto;

import com.vbqkma.libarybackend.model.Group;
import com.vbqkma.libarybackend.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Long groupId;
    private Group group;
    private List<Long> userIds;
    private List<Long> groupIds;
    private List<User> users;
    private Boolean isJoinGroup;
    private Boolean isLeaveGroup;
    private Boolean isLockUsers;
    private Boolean isUnlockUsers;
    private Integer isLock;
    private Set<Group> groups = new HashSet<>();
    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, String phone, String address, String token, String name, String avatar, Integer isLock) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.token = token;
        this.name = name;
        this.avatar = avatar;
        this.isLock = isLock;
    }
}
