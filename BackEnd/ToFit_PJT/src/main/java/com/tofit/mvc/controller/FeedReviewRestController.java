package com.tofit.mvc.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tofit.mvc.jwt.JwtUtil;
import com.tofit.mvc.model.dto.FeedReview;
import com.tofit.mvc.model.dto.FeedReviewView;
import com.tofit.mvc.model.service.FeedReviewService;

@RestController
@RequestMapping("/tofit/feeds/{feedId}/review")
@CrossOrigin("*")
public class FeedReviewRestController {
	
	private final FeedReviewService feedReviewService;
	private final JwtUtil jwtUtil;
	
	public FeedReviewRestController(FeedReviewService feedReviewService, JwtUtil jwtUtil) {
		super();
		this.feedReviewService = feedReviewService;
		this.jwtUtil = jwtUtil;
	}

	// 피드 댓글 전체 조회
	@GetMapping()
	public ResponseEntity<?> list(@PathVariable("feedId") int feedId){
		List<FeedReviewView> list = feedReviewService.getReviewList(feedId);
		
		if(list == null || list.size() == 0)
			return new ResponseEntity<String>("등록된 댓글이 없습니다", HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<List<FeedReviewView>>(list, HttpStatus.OK);
	}
	
	// 피드 댓글 등록
	@PostMapping()
	public ResponseEntity<?> write(@PathVariable("feedId") int feedId, @RequestHeader(value = "Authorization") String token, @RequestBody FeedReview review){
		token = token.replace("Bearer ", "");
	    String userId = jwtUtil.getUserIdFromToken(token);
	    
	    review.setFeedId(feedId);
	    review.setUserId(userId);
	    
		boolean isOk = feedReviewService.writeReview(review);
		if(isOk)
			return new ResponseEntity<String>("등록 완료", HttpStatus.CREATED);
		
		return new ResponseEntity<String>("등록 실패", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	// 피드 댓글 수정
	@PutMapping("/{reviewId}")
	public ResponseEntity<?> update(@PathVariable("reviewId") int reviewId, @RequestBody FeedReview review){
		review.setReviewId(reviewId);
		
		boolean isOk = feedReviewService.modifyReview(review);
		
		if(isOk)
			return new ResponseEntity<String>(review.getContent(), HttpStatus.ACCEPTED);
		
		return new ResponseEntity<String>("수정 불가", HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	// 피드 댓글 삭제
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<?> delete(@PathVariable("reviewId") int reviewId){
		boolean isOk = feedReviewService.removeReview(reviewId);
		
		if(isOk)
			return new ResponseEntity<String>("삭제 완료", HttpStatus.ACCEPTED);

		return new ResponseEntity<String>("삭제 불가", HttpStatus.SERVICE_UNAVAILABLE);
	}
}
