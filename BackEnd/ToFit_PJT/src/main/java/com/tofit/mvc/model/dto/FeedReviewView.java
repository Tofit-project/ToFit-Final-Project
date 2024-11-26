package com.tofit.mvc.model.dto;

import java.sql.Date;

public class FeedReviewView {
	private int reviewId;
	private String userId;
	private int feedId;
	private String content;
	private String profileName;
	private String profileImg;
	private Date regDate;
	
	public FeedReviewView() {
	}
	
	public FeedReviewView(int reviewId, String userId, int feedId, String content, String profileName,
			String profileImg, Date regDate) {
		this.reviewId = reviewId;
		this.userId = userId;
		this.feedId = feedId;
		this.content = content;
		this.profileName = profileName;
		this.profileImg = profileImg;
		this.regDate = regDate;
	}

	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getFeedId() {
		return feedId;
	}

	public void setFeedId(int feedId) {
		this.feedId = feedId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getProfileImg() {
		return profileImg;
	}

	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	@Override
	public String toString() {
		return "FeedReviewView [reviewId=" + reviewId + ", userId=" + userId + ", feedId=" + feedId + ", content="
				+ content + ", profileName=" + profileName + ", profileImg=" + profileImg + ", regDate=" + regDate
				+ "]";
	}
	
	

}
