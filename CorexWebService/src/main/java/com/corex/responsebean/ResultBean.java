package com.corex.responsebean;

public class ResultBean {
	private String message;
	private String code;
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setCode(String code, String message) {
		this.setCode(code);
		this.setMessage(message);
	}
}
