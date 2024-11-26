<template>
  <div class="container">
    <div class="calendar-container">
      <h2 class="calendar-title">찜한 동영상 캘린더</h2>
      <FullCalendar :options="calendarOptions" />
    </div>

    <!-- 찜한 동영상 목록 -->
    <div class="favorites-list-container">
      <h3>찜한 동영상</h3>
      <div v-if="favoriteList.length === 0">찜한 동영상이 없습니다.</div>
      <div v-else v-for="fav in favoriteList" :key="fav.videoId" class="favorite-item">
        <button class="favorite-button" @click="addVideoToToday(fav)">
          {{ decode(fav.title) }}
        </button>
        <button class="delete-button" @click="removeFavorite(fav)" aria-label="삭제">
          삭제
        </button>
      </div>
    </div>
  </div>

  <!-- GPT에게 요약 및 추천 받기 -->
  <div>
    <h3 class="calendar-title">AI PT쌤에게 추천 받기</h3>
    <button class="gpt-message" @click="loadGPT">클릭</button>

    <!-- 로딩 중 메시지 -->
    <div v-if="isLoading" class="loading-indicator">
      <div class="spinner"></div>
      <p>로딩 중입니다...</p>
    </div>
    <!-- GPT 응답 표시 -->
    <div v-else>
      <div class="gpt-response" v-html="formattedGptResponse"></div>
    </div>
  </div>

  <!-- 날짜 클릭 시 표시되는 모달 -->
  <div v-if="showModal" class="modal">
    <div class="modal-content">
      <div v-if="selectedEvents.length === 0">
        <h3>오늘 한 운동이 없습니다!</h3>
        <RouterLink :to="{name : 'videoList'}">바로 운동하러 가기</RouterLink>
      </div>
      <div v-else>
        <h3>{{ selectedDate }} 운동 완료!</h3>
        <ol>
          <li
            v-for="event in selectedEvents"
            :key="event.title"
            @click="deleteEvent(event)"
            class="event-item"
          >
            {{ decode(event.title) }}
          </li>
        </ol>
      </div>
      <button class="close-button" @click="closeModal">닫기</button>
    </div>
  </div>
</template>

<script>
import FullCalendar from "@fullcalendar/vue3";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import { useFavoriteStore } from "@/stores/favorite";
import { useRecordStore } from "@/stores/record";

export default {
  components: {
    FullCalendar,
  },
  data() {
    return {
      calendarOptions: {
        plugins: [dayGridPlugin, interactionPlugin],
        initialView: "dayGridMonth",
        dateClick: this.handleDateClick,
        events: [],
        eventBackgroundColor: "#ff7676", // 이벤트 배경색
        eventBorderColor: "#ff4c4c", // 이벤트 테두리 색
        eventTextColor: "#ffffff", // 이벤트 텍스트 색
      },
      showModal: false, // 모달 표시 여부
      selectedDate: "", // 클릭한 날짜
      selectedEvents: [], // 해당 날짜의 이벤트 목록
      isLoading: false,
    };
  },
  computed: {
    favoriteList() {
      const favStore = useFavoriteStore();
      return favStore.favoriteList || [];
    },
    recordList() {
      const recordStore = useRecordStore();
      return recordStore.recordList || [];
    },
    gptResponse(){
      const recordStore = useRecordStore();
      return recordStore.gptMessage;
    },
    // GPT 응답을 HTML로 포맷하기 위한 computed 속성
    formattedGptResponse() {
      const rawResponse = this.gptResponse || '';

      // 마크다운 스타일을 HTML로 변환
      let formattedResponse = rawResponse
        .replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>")  // **굵은 글씨**
        .replace(/\n/g, "<br>") // 줄바꿈 처리
        .replace(/- (.*?)(?=\n|$)/g, "<br>- $1"); // 목록 처리

      return formattedResponse;
    }
  },
  methods: {
    formatDate(timestamp) {
      const date = new Date(timestamp);
      const year = date.getUTCFullYear();
      const month = String(date.getUTCMonth() + 1).padStart(2, "0");
      const day = String(date.getUTCDate()).padStart(2, "0");
      return `${year}-${month}-${day}`;
    },
    async loadFavorites() {
      const favStore = useFavoriteStore();
      await favStore.getFavoriteList();
    },
    async loadRecords() {
      const recordStore = useRecordStore();
      await recordStore.getRecordList();
      this.calendarOptions.events = this.recordList.map((rec) => ({
        title: this.decode(rec.title),
        videoId: (rec.videoId),
        date: this.formatDate(rec.regDate),
      }));
    },
    async loadGPT(){
      this.isLoading = true;
      const recordStore = useRecordStore();
      try{
        await recordStore.getGptMessage();
      } catch(error){
      console.log(error)
        recordStore.gptMessage = "AI 추천을 불러오는 데 실패했습니다"
      } finally {
        this.isLoading = false;
      }
    },
    handleDateClick(arg) {
      const clickedDate = arg.dateStr;

      // 해당 날짜의 이벤트 필터링
      this.selectedDate = clickedDate;
      this.selectedEvents = this.calendarOptions.events.filter(
        (event) => event.date === clickedDate
      );

      this.showModal = true; // 모달 열기
    },
    closeModal() {
      this.showModal = false; // 모달 닫기
    },
    async addVideoToToday(fav) {
      const today = new Date();
      const formattedToday = `${today.getFullYear()}-${String(
        today.getMonth() + 1
      ).padStart(2, "0")}-${String(today.getDate()).padStart(2, "0")}`;
      const recordStore = useRecordStore();

      // db 저장
      const result = await recordStore.addRecord(fav);
      if(result === true){
        this.calendarOptions.events.push({
          title: this.decode(fav.title),
          date: formattedToday,
          videoId: fav.videoId,
        });
  
        this.calendarOptions = { ...this.calendarOptions }; // Reactivity 보장
      } else if(result === 1){
        alert("이미 등록된 운동입니다!")
      } else{
        alert("다시 시도해주세요!")
      };
    },
    deleteEvent(event) {
      const recordStore = useRecordStore();

      // db에서 삭제 요청
      recordStore.removeRecord(event);

      // 로컬 캘린더에서도 삭제
      this.calendarOptions.events = this.calendarOptions.events.filter(
        (e) => e.title !== event.title || e.date !== this.selectedDate
      );

      this.closeModal(); // 모달 닫기
    },
    async removeFavorite(fav) {
      const favStore = useFavoriteStore();
      await favStore.removeFavorite(fav.videoId); // 찜한 동영상 삭제
    
    },
    decode(encodedStr) {
      const doc = new DOMParser().parseFromString(encodedStr, "text/html");
      return doc.documentElement.textContent;
    },
  },
  mounted() {
    this.loadFavorites();
    this.loadRecords();
  },
};
</script>

<style scoped>
/* 기본 스타일 */
body {
  font-family: Arial, sans-serif;
  background-color: #ffffff; /* 흰색 배경 */
  color: #333333; /* 텍스트 색상 */
  margin: 0;
  padding: 0;
}

/* 전체 컨테이너 */
.container {
  display: flex;
  justify-content: space-between;
  padding: 20px;
}

/* 캘린더 컨테이너 */
.calendar-container {
  flex: 3; /* 캘린더 영역 */
  margin-right: 20px; /* 오른쪽 여백 */
}

/* 캘린더 제목 */
.calendar-title {
  font-size: 1.8rem;
  text-align: center;
  margin: 20px 0;
  color: #ff5a5a; /* 빨간색 */
}

/* 찜한 동영상 목록 스타일 */
.favorites-list-container {
  flex: 1; /* 목록 영역 */
  padding: 15px;
  border: 1px solid #ffdada; /* 연한 빨간 테두리 */
  border-radius: 8px;
  background-color: #fff5f5; /* 연한 빨간 배경 */
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
}

/* 찜한 동영상 버튼 스타일 */
.favorite-button {
  display: block;
  width: 100%;
  background-color: #ff7676;
  color: white;
  border: none;
  padding: 10px;
  margin: 10px 0;
  font-size: 1rem;
  border-radius: 5px;
  cursor: pointer;
  text-align: left;
  position: relative;
}

.favorite-button:hover {
  background-color: #ff4c4c;
}

/* 삭제 버튼 스타일 */
.delete-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: #ffffff;
  color: #ff7676;
  border: none;
  padding: 5px 10px;
  font-size: 0.8rem;
  border-radius: 5px;
  cursor: pointer;
}

.delete-button:hover {
  background-color: #ffdddd;
}

/* favorite-item의 상대적 위치 설정 */
.favorite-item {
  position: relative;
  margin-bottom: 15px;
}

/* GPT 메시지 로딩 중 */
.loading-indicator {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100px;
}

.spinner {
  border: 4px solid rgba(255, 255, 255, 0.3);
  border-top: 4px solid #ff5a5a;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.gpt-response {
  margin-top: 20px;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 5px;
  font-size: 1.1rem;
  color: #333;
}

.gpt-response strong {
  font-weight: bold;
  color: #ff5a5a;
}

/* 모달 스타일 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  max-width: 80%;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
.event-item {
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #ffdada; /* 연한 빨간 테두리 */
  border-radius: 8px;
  background-color: #fff5f5; /* 연한 빨간 배경 */
  color: #333333; /* 검은색 텍스트 */
  cursor: pointer;
  transition: 0.3s ease-in-out;
}

.event-item:hover {
  background-color: #ffe8e8; /* 더 진한 연한 빨간색 */
}

.close-button {
  background-color: #ff5a5a;
  color: white;
  border: none;
  padding: 8px 15px;
  font-size: 1rem;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 20px;
}

.close-button:hover {
  background-color: #ff4c4c;
}
</style>
