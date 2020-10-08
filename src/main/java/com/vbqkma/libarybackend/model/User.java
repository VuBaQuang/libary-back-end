package com.vbqkma.libarybackend.model;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "51_users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    @JsonIgnore
    private String password;
    private String name;
    private String avatar;
    private String email;
    private String phone;
    private String address;
    private String roles;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "51_users_groups",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "group_id") }
    )
    Set<Group> groups = new HashSet<>();
    public User() {
    }

    public User(Long id, String username, String password, String name, String email, String phone, String address, String roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.roles = roles;
    }
}
