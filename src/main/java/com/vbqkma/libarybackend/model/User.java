package com.vbqkma.libarybackend.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
    private Integer isLock;

    @CreationTimestamp
    private Date createdAt;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL, CascadeType.REMOVE})
    @Fetch(value = FetchMode.SELECT)
    @JoinTable(
            name = "51_users_groups",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "group_id")}
    )
    Set<Group> groups = new HashSet<>();


//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private Set<BorrowBook> borrowBooks;


    public User() {
    }

    public User(Long id, String username, String password, String name, String email, String phone, String address) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}
