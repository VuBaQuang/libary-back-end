package com.vbqkma.libarybackend.dao;

import com.vbqkma.libarybackend.model.Book;
import com.vbqkma.libarybackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BookDAO extends JpaRepository<Book, Long> {
    public Book findBookByCode(String code);

    public void deleteByIdIn(List<Long> ids);

    public Page<Book>findBooksByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(String name, String code, Pageable pageable);

    public List<Book> findBooksByIdIn(List<Long> ids);

    @Query(value = "select bor.book_id from 51_borrow_book as bor where bor.book_id in :ids and (bor.status <> 4 or bor.status <>5) ", nativeQuery = true)
    public List<Long> findIdBookInBorrowed(List<Long> ids);
}
