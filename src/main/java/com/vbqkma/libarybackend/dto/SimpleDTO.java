package com.vbqkma.libarybackend.dto;

import lombok.Data;

@Data
public class SimpleDTO {
    private int page;
    private int pageSize;
    Boolean conditionSort;
    String sortField;
}
