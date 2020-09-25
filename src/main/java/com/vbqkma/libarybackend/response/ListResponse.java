package com.vbqkma.libarybackend.response;


import com.vbqkma.libarybackend.response.data.ResponseData;

public class ListResponse<T> {
	
	private int code;
	private ResponseData<T> data;
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public ResponseData<T> getData() {
		return data;
	}
	
	public void setData(ResponseData<T> data) {
		this.data = data;
	}
}
