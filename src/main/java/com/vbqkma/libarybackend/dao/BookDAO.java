package com.vbqkma.libarybackend.dao;

import com.vbqkma.libarybackend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookDAO extends JpaRepository<Book, Long> {
    public Book findBookByCode(String code);

    public void deleteByIdIn(List<Long> ids);

    public List<Book> findBooksByIdIn(List<Long> ids);

    @Query(value = "select bor.book_id from 51_borrow_book as bor where bor.book_id in :ids and (bor.status <> 4 or bor.status <>5) ", nativeQuery = true)
    public List<Long> findIdBookInBorrowed(List<Long> ids);
}
