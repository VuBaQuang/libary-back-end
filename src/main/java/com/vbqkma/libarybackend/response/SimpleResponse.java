package com.vbqkma.libarybackend.response;

import lombok.Data;
import org.springframework.web.bind.annotation.ResponseBody;

@Data
@ResponseBody
public class SimpleResponse {
    private String error="error";
    private String message;

    public SimpleResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public SimpleResponse() {
    }
}
