package com.vbqkma.libarybackend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;


import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookDTO extends SimpleDTO {

    private Long id;
    private String username;
    private String name;
    private String code;
    private Long semester;
    private Long count;
    private Date createdAt;
    private Long isNew;

}
