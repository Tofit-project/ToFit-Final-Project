package com.tofit.mvc.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.tofit.mvc.model.dto.Feed;
import com.tofit.mvc.model.dto.FeedImage;

public interface FeedService {
    
    // 피드 작성
    public boolean registerFeed(Feed feed, List<MultipartFile> files);
    
    // 전체 피드와 피드별 이미지들 조회
    public List<Map<String, Object>> getFeedList();
    
    // 특정 사용자의 모든 피드와 피드별 이미지들 조회
    public List<Map<String, Object>> getFeedListByUserId(String userId);
    
    // 피드 상세 조회
    public Map<String, Object>  getFeedInfo(int feedId);
    
    // 피드 수정
    public boolean updateFeedInfo(Feed feed, List<MultipartFile> files);
    
    // 피드 삭제
    public boolean deleteFeedInfo(int feedId, String userId);

}
