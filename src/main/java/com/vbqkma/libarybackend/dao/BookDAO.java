package com.vbqkma.libarybackend.dao;

import com.vbqkma.libarybackend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookDAO extends JpaRepository<Book, Long> {
 public Book findBookByCode(String code);
 public void deleteByIdIn(List<Long> ids);
}
