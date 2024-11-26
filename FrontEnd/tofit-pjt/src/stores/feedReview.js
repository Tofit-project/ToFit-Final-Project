import { ref, computed } from "vue";
import { defineStore } from "pinia";
import axios from "axios";
import { useUserStore } from "./user";

const REST_API_URL = `http://localhost:8080/tofit/feeds`;

export const useFeedReviewStore = defineStore("feedReview", () => {
  const userStore = useUserStore();

  // 피드에 대한 리뷰 목록
  const feedReviewList = ref([]);
  const getFeedReviewList = function (feedId){
    axios({
        url: `${REST_API_URL}/${feedId}/review`,
        method: "GET",
    }).then((response) => {
        feedReviewList.value = response.data;
    });
  };

  // 피드에 대한 리뷰 등록
  const addReview = function (review) {
    axios({
      url: `${REST_API_URL}/${review.feedId}/review`,
      method: "POST",
      headers: {
        Authorization: `Bearer ${userStore.token}`,
      },
      data: review,
    }).then(() => {
      // 목록 다시 불러오기
      getFeedReviewList(review.feedId);
    });
  };

  // 영상에 대한 리뷰 수정
  const updateReview = function (review) {
    axios({
      url: `${REST_API_URL}/${review.feedId}/review/${review.reviewId}`,
      method: "PUT",
      data: review,
    }).then(() => {
      getFeedReviewList(review.feedId);
    });
  };
  
  // 영상에 대한 리뷰 삭제
  const removeReview = function (review) {
    axios({
      url: `${REST_API_URL}/${review.feedId}/review/${review.reviewId}`,
      method: "DELETE",
    }).then(() => {
      getFeedReviewList(review.feedId);
    });
  };

  return {
    feedReviewList,
    getFeedReviewList,
    addReview,
    updateReview,
    removeReview,
  };
});
