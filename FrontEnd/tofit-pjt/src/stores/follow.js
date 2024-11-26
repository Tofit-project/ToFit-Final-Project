import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "axios";
import router from "@/router";

const REST_API_URL = `http://localhost:8080/tofit/follows`;

export const useFollowStore = defineStore("follow", () => {
  // 팔로우 등록
  const registFollow = async (followedId) => {
    const token = sessionStorage.getItem("access-token");
    try {
      await axios.post(`${REST_API_URL}`, null, {
        headers: { Authorization: `Bearer ${token}` },
        params: { followedId },
      });
      console.log("팔로우 성공");
      await getFollowStatus(); // 상태 갱신
    } catch (err) {
      alert("다시 시도해주세요.");
      console.error(err);
    }
  };

  // 팔로우 취소
  const cancelFollow = async (followedId) => {
    const token = sessionStorage.getItem("access-token");
    try {
      await axios.delete(`${REST_API_URL}`, {
        headers: { Authorization: `Bearer ${token}` },
        params: { followedId },
      });
      console.log("언팔로우 성공");
      await getFollowStatus(); // 상태 갱신
    } catch (err) {
      alert("다시 시도해주세요.");
      console.error(err);
    }
  };

  // 사용자의 팔로우 리스트 조회
  const followStatusList = ref([]);

  const getFollowStatus = async () => {
    const token = sessionStorage.getItem("access-token");
    try {
      const response = await axios.get(`${REST_API_URL}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (response.status === 200) {
        console.log(response.data);
        followStatusList.value = response.data;
      } else if (response.status === 204) {
        followStatusList.value = [];
      }
    } catch (error) {
      console.error("팔로우 상태 조회 실패:", error);
      followStatusList.value = [];
    }
  };

  return {
    registFollow,
    cancelFollow,
    followStatusList,
    getFollowStatus,
  };
});
