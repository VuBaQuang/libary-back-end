package com.vbqkma.libarybackend.response;

import lombok.Data;
import org.springframework.web.bind.annotation.ResponseBody;

@Data
@ResponseBody
public class SimpleResponse {
    private String status;
    private String message;
    private Object data;

    public SimpleResponse(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public SimpleResponse() {
    }
}
