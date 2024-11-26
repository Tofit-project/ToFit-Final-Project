import { ref, computed } from "vue";
import { defineStore } from "pinia";
import axios from "axios";
import { useUserStore } from "./user";

const REST_API_URL = `http://localhost:8080/tofit/record`;

export const useRecordStore = defineStore("record", () => {
  const userStore = useUserStore();

  // ChatGPT
  const gptMessage = ref('');
  const getGptMessage = async function(){
    try{
      const response = await axios({
        url: `${REST_API_URL}/chatGPT`,
        method: 'GET',
        headers: {
          Authorization: `Bearer ${userStore.token}`,
        },
      });
      console.log(response.data);
      gptMessage.value = response.data;
    } catch (error){
      console.error("Failed to get GPT response", error);
      gptMessage.value = "AI 추천을 불러오는 데 실패했습니다";
    }
  };

  const recordList = ref([]);
  // 달력 기록 목록 가져오기
  const getRecordList = async () => {
    try {
      const response = await axios({
        url: `${REST_API_URL}`,
        method: "GET",
        headers: {
          Authorization: `Bearer ${userStore.token}`,
        },
      });
      recordList.value = response.data;
    } catch (error) {
      console.error("Failed to fetch record list:", error);
    }
  };

  // 달력 기록내용 등록
  const addRecord = async function (fav) {
    try {
      const response = await axios({
        url: REST_API_URL,
        method: "POST",
        headers: {
          Authorization: `Bearer ${userStore.token}`,
        },
        data: fav,
      });

      if (response.status === 201) {
        return true; // 성공
      } else if (response.status === 208) {
        return 1; // 이미 등록된 운동
      } else {
        return false; // 실패
      }
    } catch (error) {
      return false; // 예외 처리
    }
  };
  

 // 기록 삭제 예시
 const removeRecord = async (event) => {
  try {
    await axios({
      url: REST_API_URL,
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${userStore.token}`,
      },
      data: {
        recordId : 0,
        userId : null,
        videoId :  event.videoId,
        regDate : event.date,
      },
    });
    // console.log(recordList);
    // recordList.value = recordList.value.filter((rec) => rec.recordId !== recordId);
  } catch (error) {
    console.error("Failed to remove record:", error);
  }
};

  return {gptMessage, getGptMessage, recordList, getRecordList, addRecord, removeRecord};
});
