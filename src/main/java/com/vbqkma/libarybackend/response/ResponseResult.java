package com.vbqkma.libarybackend.response;

import lombok.Data;

@Data
public class ResponseResult {

  private String message;
  private Object data;

  public ResponseResult() {

  }

  public ResponseResult(String message, Object data) {
    this.message = message;
    this.data = data;
  }

  public static ResponseResult isSuccess(String message, Object data) {
    return new ResponseResult(message, data);
  }

  public static ResponseResult isSuccessSimple() {
    return new ResponseResult(ResponseCode.SuccessCode.SUCCESS, "");
  }
  public static ResponseResult isFailSimple(String message) {
    return new ResponseResult(ResponseCode.ErrorCode.FAILED, message);
  }
  public static ResponseResult failed(String message) {
    return new ResponseResult(message, null);
  }
  public static ResponseResult failed(String message, Object data) {
    return new ResponseResult(message, data);
  }
}
