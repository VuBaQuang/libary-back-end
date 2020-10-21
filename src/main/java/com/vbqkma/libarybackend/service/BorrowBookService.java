package com.vbqkma.libarybackend.service;

import com.vbqkma.libarybackend.dao.BookDAO;
import com.vbqkma.libarybackend.dao.BorrowBookDAO;
import com.vbqkma.libarybackend.dao.UserDAO;
import com.vbqkma.libarybackend.dto.BookDTO;
import com.vbqkma.libarybackend.dto.GroupDTO;
import com.vbqkma.libarybackend.model.Book;
import com.vbqkma.libarybackend.model.BorrowBook;
import com.vbqkma.libarybackend.response.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BorrowBookService {
    @Autowired
    BorrowBookDAO borrowBookDAO;


    public ResponseEntity saveOrUpdate(BorrowBook borrowBook) {
        borrowBookDAO.save(borrowBook);
        return ResponseEntity.ok().body(new SimpleResponse("U_SUCCESS", "", ""));
    }

//    public ResponseEntity getAll(BookDTO bookDTO) {
//        try {
//            Pageable pageable = PageRequest.of(bookDTO.getPage() > 0 ? bookDTO.getPage() - 1 : 0, bookDTO.getPageSize() > 0 ? bookDTO.getPageSize() : 10);
//            return ResponseEntity.ok().body(new SimpleResponse("GET_SUCCESS", "", bookDAO.findAll(pageable)));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));
//        }
//    }


}
