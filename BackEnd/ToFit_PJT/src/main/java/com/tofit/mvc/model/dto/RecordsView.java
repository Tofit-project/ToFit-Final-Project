package com.tofit.mvc.model.dto;

import java.util.Date;

public class RecordsView {
	private int recordId;
	private String userId;
	private String videoId;
	private String title;
	private String channelName;
	private String goal;
	private Date regDate;
	
	public RecordsView(int recordId, String userId, String videoId, String title, String channelName, String goal,
			Date regDate) {
		this.recordId = recordId;
		this.userId = userId;
		this.videoId = videoId;
		this.title = title;
		this.channelName = channelName;
		this.goal = goal;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	@Override
	public String toString() {
		return "RecordsView [recordId=" + recordId + ", userId=" + userId + ", videoId=" + videoId + ", title=" + title
				+ ", channelName=" + channelName + ", goal=" + goal + ", regDate=" + regDate + "]";
	}
	
}
