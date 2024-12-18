package com.tofit.mvc.model.dao;

import java.util.List;

import com.tofit.mvc.model.dto.FeedReview;
import com.tofit.mvc.model.dto.FeedReviewView;

public interface FeedReviewDao {

	// 피드 댓글 전체 조회
	List<FeedReviewView> selectAll(int feedId);

	// 피드 댓글 등록
	int insertReview(FeedReview review);

	// 피드 댓글 수정
	int updateReview(FeedReview review);

	// 피드 댓글 삭제
	int deleteReview(int reviewId);

}
