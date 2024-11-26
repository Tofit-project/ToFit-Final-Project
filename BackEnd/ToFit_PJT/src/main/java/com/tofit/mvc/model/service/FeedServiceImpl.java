package com.tofit.mvc.model.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.tofit.mvc.model.dao.FeedDao;
import com.tofit.mvc.model.dao.FeedImageDao;
import com.tofit.mvc.model.dao.UserDao;
import com.tofit.mvc.model.dto.Feed;
import com.tofit.mvc.model.dto.FeedImage;

@RestController
public class FeedServiceImpl implements FeedService {

    private final FeedDao feedDao;
    private final UserDao userDao;
    private final FeedImageDao feedImageDao;
    private final AmazonS3Client amazonS3Client;
    private final String bucket;

    public FeedServiceImpl(FeedDao feedDao, UserDao userDao, FeedImageDao feedImageDao, AmazonS3Client amazonS3Client,
            @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.feedDao = feedDao;
        this.userDao = userDao;
        this.feedImageDao = feedImageDao;
        this.amazonS3Client = amazonS3Client;
        this.bucket = bucket;
    }

/////////////// 피드 작성 /////////////////////////////////////////////////////////////////////////////    
    @Transactional
    @Override
    public boolean registerFeed(Feed feed, List<MultipartFile> files) {

        // 피드부터 저장
        boolean insertSuccess = feedDao.insertFeedOne(feed) == 1;
        if (!insertSuccess) {
            throw new RuntimeException("Feed 저장 실패");
        }

        // 사진 업로드 안했을 경우 처리
        if (files == null && files.size() <= 0) {
            return true;
        }

        List<FeedImage> feedImages = files.parallelStream().map(file -> {
            try {
                // s3에 업로드
                String imageUrl = uploadFileToS3(file);

                // 피드 이미지 데이터 담기
                FeedImage feedImage = new FeedImage();
                feedImage.setFeedId(feed.getFeedId());
                feedImage.setImg(imageUrl);
                return feedImage;
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 실패", e);
            }
        }).toList();

        // 피드 이미지 db에 저장
        if (!feedImages.isEmpty()) {
            int res = feedImageDao.insertFeedImages(feedImages);
            if (res != feedImages.size()) {
                throw new RuntimeException("FeedImage 저장 실패");
            }
        }
        return true;
    }

    private String uploadFileToS3(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);

        // 업로드되면 파일url 반환
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

////////////////////////////////////////////////////////////////////////////////////////////////

    // 전체 피드와 피드별 이미지들 조회
    @Override
    public List<Map<String, Object>> getFeedList() {

        List<Feed> feedList = feedDao.selectFeedList();
        List<FeedImage> feedImageList = feedImageDao.selectAllFeedImages();

        List<Map<String, Object>> res = new ArrayList<>();

        // Feed에 해당하는 FeedImage 리스트를 매핑
        for (Feed feed : feedList) {
            List<FeedImage> imagesFromFeed = new ArrayList<>();

            // FeedImage 리스트에서 해당 Feed의 id에 맞는 이미지들 필터링
            for (FeedImage image : feedImageList) {
                if (image.getFeedId() == feed.getFeedId()) {
                    imagesFromFeed.add(image);
                }
            }

            Map<String, Object> userInfo = userDao.selectProfileInfo(feed.getUserId());

            Map<String, Object> feedWithImages = new HashMap<>();
            feedWithImages.put("feed", feed);
            feedWithImages.put("images", imagesFromFeed);
            feedWithImages.put("profileName", userInfo.get("profile_name"));
            feedWithImages.put("profileImg", userInfo.get("profile_img"));

            res.add(feedWithImages);
        }
        return res;
    }

    // 특정 사용자의 모든 피드와 피드별 이미지들 조회
    @Override
    public List<Map<String, Object>> getFeedListByUserId(String userId) {

        List<Feed> feedList = feedDao.selectFeedListByUserId(userId);

        List<Map<String, Object>> res = new ArrayList<>();

        for (Feed feed : feedList) {

            List<FeedImage> feedImageList = feedImageDao.selectFeedImagesByFeedId(feed.getFeedId());
            Map<String, Object> userInfo = userDao.selectProfileInfo(userId);

            Map<String, Object> feedWithImages = new HashMap<>();
            feedWithImages.put("feed", feed);
            feedWithImages.put("images", feedImageList);
            feedWithImages.put("profileName", userInfo.get("profile_name"));
            feedWithImages.put("profileImg", userInfo.get("profile_img"));

            res.add(feedWithImages);
        }
        return res;
    }

    // 피드 상세 조회
    @Override
    public Map<String, Object> getFeedInfo(int feedId) {
        
        Map<String, Object> feedDetail = new HashMap<>();
    
        Feed feed = feedDao.selectFeedOne(feedId);
        List<FeedImage> images = feedImageDao.selectFeedImagesByFeedId(feedId);
        
        feedDetail.put("feed", feed);
        feedDetail.put("images", images);
        
        return feedDetail;    
    }

    // 피드 삭제
    @Transactional
    @Override
    public boolean deleteFeedInfo(int feedId, String userId) {

        // id에 해당하는 feed 있는지 확인하고
        Feed exist = feedDao.selectFeedOne(feedId);

        // 있으면 해당 feed 데이터 삭제(삭제 권한 -> 로그인한 사용자 확인)
        if (exist != null && exist.getUserId().equals(userId)) {

            // 그리고 해당하는 feedid에 대한 feedImage들이 있는지 확인하고
            List<FeedImage> images = feedImageDao.selectFeedImagesByFeedId(feedId);
            // 이미지들이 있으면 삭제
            if (!images.isEmpty()) {
                // db에서 삭제
                boolean deleteSuccess = feedImageDao.deleteFeedImages(feedId) > 0;

                // s3에서 삭제
                if (deleteSuccess) {
                    for (FeedImage image : images) {
                        try {
                            String imgUrl = image.getImg();
                            String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);

                            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));

                        } catch (Exception e) {
                            throw new RuntimeException("S3에서 피드 이미지 삭제 실패", e);
                        }
                    }
                } else {
                    throw new RuntimeException("피드 이미지 삭제 실패");
                }
            }

            // 피드 삭제
            int res = feedDao.deleteFeedOne(feedId);
            if (res == 1) {
                return true;
            } else {
                throw new RuntimeException("피드 삭제 실패");
            }
        }
        return false;
    }

    // 피드 수정
    @Transactional
    @Override
    public boolean updateFeedInfo(Feed newFeed, List<MultipartFile> files) {

        // 피드 등록한 userId와 로그인된 userId가 동일한지 확인
        Feed exist = feedDao.selectFeedOne(newFeed.getFeedId());
        if (exist == null) {
            throw new RuntimeException("해당하는 피드가 없음");
        }

        // 새로운 내용으로 수정
        boolean updateSuccess = feedDao.updateFeedOne(newFeed) == 1;
        if (!updateSuccess) {
            throw new RuntimeException("피드 업데이트 실패");
        }

        // 파일은 새로 업로드 되지 않을때는 여기서 종료
        if (files == null || files.size() <= 0) {
            return true;
        }

        // 피드 이미지를 업데이트 처리하기 (s3랑 db에 기존꺼 삭제하고, 새거 업로드)
        List<FeedImage> images = feedImageDao.selectFeedImagesByFeedId(newFeed.getFeedId());
        if (!images.isEmpty()) {
            for (FeedImage image : images) {
                try {
                    // s3에서 먼저 삭제
                    String imgUrl = image.getImg();
                    String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
                    amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
                } catch (Exception e) {
                    throw new RuntimeException("S3에서 기존 피드 이미지 삭제 실패", e);
                }
            }
            
            // db에서 삭제
            boolean deleteSuccess = feedImageDao.deleteFeedImages(newFeed.getFeedId()) > 0;
            if(!deleteSuccess) {
                throw new RuntimeException("피드 이미지 db 삭제 실패");
            }
        }
        
        // 새 이미지 업로드
        List<FeedImage> feedImages = files.parallelStream().map(file -> {
            try {
                // s3에 업로드
                String imageUrl = uploadFileToS3(file);

                // 피드 이미지 데이터 담기
                FeedImage feedImage = new FeedImage();
                feedImage.setFeedId(newFeed.getFeedId());
                feedImage.setImg(imageUrl);
                return feedImage;
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 실패", e);
            }
        }).toList();

        // 피드 이미지 db에 저장
        if (!feedImages.isEmpty()) {
            int res = feedImageDao.insertFeedImages(feedImages);
            if (res != feedImages.size()) {
                throw new RuntimeException("FeedImage 저장 실패");
            }
        }
        return true;
    }

}
