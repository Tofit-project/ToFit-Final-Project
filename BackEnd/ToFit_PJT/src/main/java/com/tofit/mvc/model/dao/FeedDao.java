package com.tofit.mvc.model.dao;

import java.util.List;

import com.tofit.mvc.model.dto.Feed;

public interface FeedDao {
    
    // 피드 작성
    public int insertFeedOne(Feed feed);
    
    // 전체 피드 조회
    public List<Feed> selectFeedList();
    
    // 사용자의 모든 피드 조회
    public List<Feed> selectFeedListByUserId(String userId);
    
    // 피드 상세 조회
    public Feed selectFeedOne(int feedId);
    
    // 사용자의 특정 피드 수정
    public int updateFeedOne(Feed feed);
    
    // 사용자의 특정 피드 삭제
    public int deleteFeedOne(int feedId);

}
