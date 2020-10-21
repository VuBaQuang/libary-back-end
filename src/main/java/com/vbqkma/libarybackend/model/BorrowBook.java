package com.vbqkma.libarybackend.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "51_borrow_book")
public class BorrowBook {
    @Id
    @Column(name = "borrow_book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long status;

    @CreationTimestamp
    private Date createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
