package com.vbqkma.libarybackend.model;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "51_groups")
public class Group {
    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String code;

    @ManyToMany(mappedBy = "groups")
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "groups")
    private Set<User> users = new HashSet<>();
}
