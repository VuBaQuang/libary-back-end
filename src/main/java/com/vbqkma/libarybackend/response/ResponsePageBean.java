package com.vbqkma.libarybackend.response;

import lombok.Data;

import java.util.List;

@Data
public class ResponsePageBean<T> {
  private List<T> content;
  private int totalPages;
  private Long totalElements;

  public ResponsePageBean() {
    this.totalPages = 1;
  }
}
