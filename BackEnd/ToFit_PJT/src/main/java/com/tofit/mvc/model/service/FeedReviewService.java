package com.tofit.mvc.model.service;

import java.util.List;

import com.tofit.mvc.model.dto.FeedReview;
import com.tofit.mvc.model.dto.FeedReviewView;

public interface FeedReviewService {

	List<FeedReviewView> getReviewList(int feedId);

	boolean writeReview(FeedReview review);

	boolean modifyReview(FeedReview review);

	boolean removeReview(int reviewId);

}
