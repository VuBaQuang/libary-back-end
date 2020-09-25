package com.vbqkma.libarybackend.response;

import com.vbqkma.libarybackend.response.data.ResponseData;
import org.springframework.data.domain.Page;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GenerateResponse {

	private static int SUCCESS_CODE = 20000;
	private static int ERROR_CODE = 10000;
	public static int INVALID_TOKEN_ERROR_CODE = 400000;
	public static int PERMISSION_DENIED_ERROR_CODE = 50001;

	public static <T> ListResponse<T> generateSuccessListResponse(Page<T> items) {
		ListResponse<T> res = new ListResponse<T>();
		res.setCode(SUCCESS_CODE);
		ResponseData<T> data = new ResponseData<T>();
		data.setItems(items.getContent());
		data.setTotal(items.getTotalElements());
		res.setData(data);
		return res;
	}

	public static <T> ListResponse<T> generateErrorListResponse() {
		ListResponse<T> res = new ListResponse<T>();
		res.setCode(ERROR_CODE);
		res.setData(null);
		return res;
	}

	public static <T> ListResponse<T> generateSuccessListResponse(List<T> items, long total) {
		ListResponse<T> res = new ListResponse<T>();
		res.setCode(SUCCESS_CODE);
		ResponseData<T> data = new ResponseData<T>();
		data.setItems(items);
		data.setTotal(total);
		res.setData(data);
		return res;
	}
	
	public static <T> GetDetailResponse<T> generateSuccessGetDetailResponse(T item) {
		GetDetailResponse<T> res = new GetDetailResponse<T>();
		res.setCode(SUCCESS_CODE);
		res.setData(item);
		return res;
	}
	
	public static <T> GetDetailResponse<T> generateErrorGetDetailResponse(T item) {
		GetDetailResponse<T> res = new GetDetailResponse<T>();
		res.setCode(SUCCESS_CODE);
		res.setData(item);
		return res;
	}

	public static <T> GetDetailResponse<T> generateErrorGetDetailResponse() {
		GetDetailResponse<T> res = new GetDetailResponse<T>();
		res.setCode(ERROR_CODE);
		return res;
	}
	
	public static <T> GetDetailResponse<T> generateErrorGetDetailResponse(String errors) {
		GetDetailResponse<T> res = new GetDetailResponse<T>();
		res.setCode(ERROR_CODE);
		res.setErrors(errors);
		return res;
	}

	public static SimpleResponse generateSuccessSimpleResponse() {
		SimpleResponse res = new SimpleResponse();
		res.setCode(SUCCESS_CODE);
		res.setData("success");
		return res;
	}

	public static SimpleResponse generateErrorSimpleResponse(String message) {
		SimpleResponse res = new SimpleResponse();
		res.setCode(ERROR_CODE);
		res.setData(message);
		return res;
	}
	
	public static <T> ListResponse<T> generateInvalidTokenErrorListResponse() {
		ListResponse<T> res = new ListResponse<>();
		res.setCode(INVALID_TOKEN_ERROR_CODE);
		res.setData(null);
		return res;
	}
	
	public static <T> GetDetailResponse<T> generateInvalidTokenErrorGetDetailResponse(String errors) {
		GetDetailResponse<T> res = new GetDetailResponse<>();
		res.setCode(INVALID_TOKEN_ERROR_CODE);
		if (errors.equals("")) {
			res.setErrors("Invalid Token Error.");
		} else {
			res.setErrors(errors);
		}
		return res;
	}

	public static SimpleResponse generateInvalidTokenErrorSimpleResponse(String message) {
		SimpleResponse res = new SimpleResponse();
		res.setCode(INVALID_TOKEN_ERROR_CODE);
		if (message.equals("")) {
			res.setData("Invalid Token Error.");
		} else {
			res.setData(message);
		}
		return res;
	}
	
	public static <T> ListResponse<T> generatePermissionDeniedErrorListResponse() {
		ListResponse<T> res = new ListResponse<>();
		res.setCode(PERMISSION_DENIED_ERROR_CODE);
		res.setData(null);
		return res;
	}
	
	public static <T> GetDetailResponse<T> generatePermissionDeniedErrorGetDetailResponse(String errors) {
		GetDetailResponse<T> res = new GetDetailResponse<>();
		res.setCode(PERMISSION_DENIED_ERROR_CODE);
		if (errors.equals("")) {
			res.setErrors("Permission Denied Error.");
		} else {
			res.setErrors(errors);
		}
		return res;
	}

	public static SimpleResponse generatePermissionDeniedErrorSimpleResponse(String message) {
		SimpleResponse res = new SimpleResponse();
		res.setCode(PERMISSION_DENIED_ERROR_CODE);
		if (message.equals("")) {
			res.setData("Permission Denied Error.");
		} else {
			res.setData(message);
		}
		return res;
	}

	public static <T> List<T> generateSuccessListResponseCampaignWithPagging(List<T> items, int pageNumber, int size) {
		System.out.println("-- Start pagging " + System.currentTimeMillis());
		if (items.size() < size) {
			return items;
		} else {
			int tmp = items.size() % size;
			int totalPage = 0;
			if (tmp != 0) {
				totalPage = items.size() / size + 1;
			} else {
				totalPage = items.size() / size;
			}
			if (pageNumber == totalPage) {
				return items.subList(size * (pageNumber - 1), items.size());
			} else if (pageNumber == 1) {
				return items.subList(0, size * pageNumber);
			} else {
				return items.subList(size * (pageNumber - 1), size * pageNumber);
			}
		}
	}

	public static String getInfoCampaignLabel(Date dateMin, Date dateMax, int count) {
		String label = "";
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(dateMax);
		cal2.setTime(dateMin);
		boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
				&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		if (sameDay) {
			label = "#campaign.label.today# (" + count + ")";
		} else {
			long day = getDifferenceDays(dateMin, dateMax);
			if (day <= 6) {
				label = "#campaign.label.this.week# (" + count + ")";
			} else if (day <= 29 && day >= 7) {
				label = "#campaign.label.this.month#  (" + count + ")";
			} else if (day <= 365 && day >= 30) {
				label = "#campaign.label.this.year# (" + count + ")";
			} else {
				label = "#campaign.label.all# (" + count + ")";
			}
		}
		return label;
	}

	public static long getDifferenceDays(Date d1, Date d2) {
		long diff = d2.getTime() - d1.getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}
}
