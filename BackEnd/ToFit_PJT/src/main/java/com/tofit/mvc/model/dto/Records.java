package com.tofit.mvc.model.dto;


// sql 형식 date
import java.sql.Date;

public class Records {
	private int recordId;
	private String userId;
	private String videoId;
	private Date regDate;
	
	public Records() {}
	
	public Records(int recordId, String userId, String videoId, Date regDate) {
		this.recordId = recordId;
		this.userId = userId;
		this.videoId = videoId;
		this.regDate = regDate;
	}

	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	@Override
	public String toString() {
		return "Records [recordId=" + recordId + ", userId=" + userId + ", videoId=" + videoId + ", regDate=" + regDate
				+ "]";
	}
}
