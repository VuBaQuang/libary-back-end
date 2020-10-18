package com.vbqkma.libarybackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "51_books")
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
//    @JsonIgnore
//    private String password;
//    private String name;
//    private String avatar;
//    private String email;
//    private String phone;
//    private String address;
//    private Integer isLock;

    @CreationTimestamp
    private Date createdAt;

}
