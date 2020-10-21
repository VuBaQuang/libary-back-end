package com.vbqkma.libarybackend.service;

import com.vbqkma.libarybackend.dao.BookDAO;
import com.vbqkma.libarybackend.dao.UserDAO;
import com.vbqkma.libarybackend.dto.BookDTO;
import com.vbqkma.libarybackend.dto.GroupDTO;
import com.vbqkma.libarybackend.model.Book;
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
public class BookService {
    @Autowired
    BookDAO bookDAO;

    @Autowired
    UserDAO userDAO;

    public ResponseEntity saveOrUpdate(BookDTO bookDTO) {
        Book book = bookDAO.findBookByCode(bookDTO.getCode());
        if (bookDTO.getIsNew() != null && bookDTO.getIsNew() == 2) {
            if (book == null) {
                return ResponseEntity.ok().body(new SimpleResponse("ERROR", "Mã sách không tồn tại", ""));
            }
            book.setCount(book.getCount() + bookDTO.getCount());
            bookDAO.save(book);
            return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Cập nhật sách thành công", ""));
        }
        if (book != null) {
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "Mã sách đã tồn tại", ""));
        }
        Book bo = new Book();
        bo.setCode(bookDTO.getCode());
        bo.setCount(bookDTO.getCount());
        bo.setName(bookDTO.getName());
        bo.setSemester(bookDTO.getSemester());
        bo.setBorrowed(0L);
        bookDAO.save(bo);
        return ResponseEntity.ok().body(new SimpleResponse("SUCCESS", "Thêm mới sách thành công", ""));
    }

    public ResponseEntity getAll(BookDTO bookDTO) {
        try {
            Pageable pageable = PageRequest.of(bookDTO.getPage() > 0 ? bookDTO.getPage() - 1 : 0, bookDTO.getPageSize() > 0 ? bookDTO.getPageSize() : 10);
            return ResponseEntity.ok().body(new SimpleResponse("GET_SUCCESS", "", bookDAO.findAll(pageable)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));
        }
    }
    @Transactional
    public ResponseEntity deletes(List<Long> ids) {
        try {
            List<Long> longs  = bookDAO.findIdBookInBorrowed(ids);
            if(longs.size()>0){
                return ResponseEntity.ok().body(new SimpleResponse("ERROR", "Có sách đang cho mượn, không thể xóa !", ""));
            }
            bookDAO.deleteByIdIn(ids);
            return ResponseEntity.ok().body(new SimpleResponse("DELETE_SUCCESS", "", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SimpleResponse("ERROR", "server_error", ""));
        }
    }

}
