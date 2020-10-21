package com.vbqkma.libarybackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "51_books")
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private Long borrowed;
    private Long semester;
    private Long count;
    @CreationTimestamp
    private Date createdAt;


//    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
//    private Set<BorrowBook> persons;


//    @ManyToOne
//    @JoinColumn(name = "borrow_book_id")
//    private BorrowBook borrowBook;
}
