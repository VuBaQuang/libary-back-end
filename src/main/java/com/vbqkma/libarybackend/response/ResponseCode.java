package com.vbqkma.libarybackend.response;

public class ResponseCode {
  public interface ErrorCode {
    String ERROR_NOT_FOUND = "NOT_FOUND";
    String NOT_EXIST = "NOT_EXIST";
    String IS_USED = "IS_USED";
    String NOT_USED = "NOT_USED";
    String FAILED = "FAILED";
    String BAD_REQUEST = "BAD_REQUEST";
    String FILE_NOT_FOUND = "FILE_NOT_FOUND";
    String VALID_USERNAME = "VALID_USERNAME";
    String VALID_PASSWORD = "VALID_PASSWORD";
    String VALID_LOGIN = "VALID_LOGIN";
    String IS_LOCKED = "IS_LOCKED";
  }

  public interface SuccessCode {
    String SUCCESS = "SUCCESS";
    String IS_EMPTY = "IS_EMPTY";
    String LOGIN_SUCCESS = "LOGIN_SUCCESS";
  }
}
