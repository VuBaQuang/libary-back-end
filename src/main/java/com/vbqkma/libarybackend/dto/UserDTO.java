package com.vbqkma.libarybackend.dto;

import com.vbqkma.libarybackend.model.Book;
import com.vbqkma.libarybackend.model.BorrowBook;
import com.vbqkma.libarybackend.model.Group;
import com.vbqkma.libarybackend.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
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
    private String password;
    private String name;
    private String avatar;
    private Integer isLock;
    private Date createdAt;

    private Long groupId;
    private Group group;
    private Book book;
    private List<Book> books;
    private List<BorrowBook> borrowBooks;
    private List<Long> userIds;
    private List<Long> groupIds;
//    private List<Group> groups;
    private List<User> users;
    private Boolean isJoinGroup;
    private Boolean isLeaveGroup;
    private Boolean isLockUsers;
    private Boolean isUnlockUsers;
    private Set<Group> groups = new HashSet<>();
    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, String phone, String address, String token, String name, String avatar, Integer isLock,Date createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.name = name;
        this.token=token;
        this.avatar = avatar;
        this.isLock = isLock;
        this.createdAt=createdAt;
    }
}
