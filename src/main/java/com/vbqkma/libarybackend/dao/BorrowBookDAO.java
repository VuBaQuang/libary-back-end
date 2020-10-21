package com.vbqkma.libarybackend.dao;

import com.vbqkma.libarybackend.model.BorrowBook;
import com.vbqkma.libarybackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowBookDAO extends JpaRepository<BorrowBook,Long> {
    public List<BorrowBook> findBorrowBooksByUser(User user);
}
