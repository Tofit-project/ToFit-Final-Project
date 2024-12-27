package com.tofit.mvc.model.dto;

public class EmailInfo {
	private String email;
	private String code;
	
	public EmailInfo(String email, String code) {
		super();
		this.email = email;
		this.code = code;
	}
	
	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	@Override
	public String toString() {
		return "EmailInfo [email=" + email + ", code=" + code + "]";
	}
	
}
