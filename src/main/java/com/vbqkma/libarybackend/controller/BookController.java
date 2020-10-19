package com.vbqkma.libarybackend.controller;

import com.vbqkma.libarybackend.dto.BookDTO;
import com.vbqkma.libarybackend.dto.UserDTO;
import com.vbqkma.libarybackend.model.Book;
import com.vbqkma.libarybackend.model.User;
import com.vbqkma.libarybackend.service.BookService;
import com.vbqkma.libarybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rest/books")
public class BookController {
    @Autowired
    BookService bookService;

    @PostMapping(path = "/get-all")
    public ResponseEntity getAll(@RequestBody BookDTO bookDTO) {
        return bookService.getAll(bookDTO);
    }

    @PostMapping
    public ResponseEntity saveOrUpdate(@RequestBody BookDTO bookDTO) {
        return bookService.saveOrUpdate(bookDTO);
    }
//
    @PostMapping(path = "/deletes")
    public ResponseEntity deletes(@RequestBody List<Long> ids) {
        return bookService.deletes(ids);
    }

}
