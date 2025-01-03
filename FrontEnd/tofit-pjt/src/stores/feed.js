import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "axios";
import router from "@/router";

const REST_API_URL = `http://localhost:8080/tofit/feeds`;

export const useFeedStore = defineStore("feed", () => {
  // 피드 생성
  const writeFeed = function (feed) {
    // 보낼 데이터 세팅
    const token = sessionStorage.getItem("access-token");
    const formData = new FormData();
    formData.append("content", feed.content);
    feed.images.forEach((image) => {
      formData.append("files", image);
    });

    axios
      .post(`${REST_API_URL}/write`, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      })
      .then((response) => {
        if (response.status === 201) {
          alert("게시 완료");
          router.push({ name: "feedList" });
        } else {
          alert("게시 실패, 다시 시도해주세요.");
        }
      })
      .catch((err) => {
        alert("다시 시도해주세요.");
      });
  };

  // 사용자 피드리스트 전체 조회
  const myFeedList = ref([]);
  const getUserFeedList = function (userId = null) {
    // userId값이 함수 호출 시 전달되지 않으면 null 값을 기본값으로 사용한다는 의미
    const token = sessionStorage.getItem("access-token");
    let url = `${REST_API_URL}/user`; // 기본 URL은 로그인한 사용자의 피드
    let params = {};

    if (userId) {
      // userId가 전달되면 해당 사용자의 피드를 가져오기 위한 쿼리 파라미터 설정
      params = { userId: userId };
    }

    axios
      .get(url, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        params: params, // 쿼리 파라미터로 userId 전달
      })
      .then((res) => {
        console.log("체크해야해!!->" + res.data);
        myFeedList.value = res.data; // 응답받은 피드 리스트를 저장
      })
      .catch(() => {
        alert("피드 리스트를 가져오는데 실패했습니다.");
      });
  };

  // 사용자 피드 삭제
  const feedDelete = function (feedId) {
    const token = sessionStorage.getItem("access-token");
    console.log(feedId);

    axios
      .delete(`${REST_API_URL}/user/${feedId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then(() => {
        alert("피드 삭제 완료");
        getUserFeedList();
      })
      .catch(() => {
        alert("피드 삭제 실패, 다시 시도해주세요.");
      });
  };

  // 피드리스트 전체 조회
  const feedAllList = ref([]);
  const getFeedList = function () {
    axios
      .get(REST_API_URL)
      .then((res) => {
        console.log(res);
        feedAllList.value = res.data;
      })
      .catch(() => {
        alert("커뮤니티 데이터를 가져오는데 실패했습니다.");
      });
  };

  // 사용자 피드 수정
  const feedUpdate = function (feed) {
    const token = sessionStorage.getItem("access-token");
    const formData = new FormData();
    formData.append("content", feed.content);
    if (feed.images && feed.images.length > 0) {
      feed.images.forEach((image) => {
        formData.append("files", image);
      });
    }

    axios
      .put(`${REST_API_URL}/user/${feed.feedId}`, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      })
      .then((response) => {
        if (response.status === 200) {
          alert("수정 완료");
          router.push({ name: "feedList" });
        } else {
          alert("수정 실패, 다시 시도해주세요.");
        }
      })
      .catch(() => {
        alert("피드 수정 실패, 다시 시도해주세요.");
      });
  };

  // 피드 상세 조회(수정작업을 위한 기존 데이터 불러오기)
  const feedDetailInfo = ref({});
  const getFeedDetail = function (feedId) {
    const token = sessionStorage.getItem("access-token");
    axios
      .get(`${REST_API_URL}/user/${feedId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        console.log("Response data:", res.data);
        feedDetailInfo.value = res.data;
      })
      .catch(() => {
        alert("피드 상세 정보를 가져오는 데 실패했습니다.");
      });
  };

  return {
    writeFeed,
    getUserFeedList,
    myFeedList,
    feedDelete,
    feedAllList,
    getFeedList,
    feedUpdate,
    feedDetailInfo,
    getFeedDetail,
  };
});
