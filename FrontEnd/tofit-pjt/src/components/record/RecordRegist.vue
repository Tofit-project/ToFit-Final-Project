<template>
  <!-- GPT에게 요약 및 추천 받기 -->
  <div>
    <h3 class="calendar-title">이번달 리포트</h3>
    <p class="calander-content">열정 만!땅! 💪</p>
    <p class="calander-content">AI 트레이너쌤에게 분석을 받아보세요! 🙌</p>
    <!-- gpt-message 버튼을 감싸는 div 추가 -->
<div class="gpt-message-container">
  <button class="gpt-message" @click="loadGPT">분석 받기</button>
</div>


    <div v-if="showBox">
      <!-- 로딩 중 메시지 -->
      <div v-if="isLoading" class="loading-indicator">
        <div class="spinner"></div>
        <p>회원님의 운동을 분석 중입니다...</p>
      </div>
      <!-- GPT 응답 표시 -->
      <div v-else>
        <div class="gpt-response" v-html="formattedGptResponse"></div>
      </div>
    </div>
  </div>

  <div class="container">
    <div class="calendar-container">
      <FullCalendar :options="calendarOptions" />
    </div>

    <!-- 찜한 동영상 목록 -->
    <div class="favorites-list-container">
      <h3 class="favorites-title">찜한 동영상</h3>
      <div v-if="favoriteList.length === 0">찜한 동영상이 없습니다.</div>
      <div
        v-else
        v-for="fav in favoriteList"
        :key="fav.videoId"
        class="favorite-item"
      >
        <button class="favorite-button" @click="showFavoriteModal(fav)">
          {{ decode(truncateTitle(fav.title)) }}
        </button>
      </div>
    </div>
  </div>

  <!-- 날짜 클릭 시 표시되는 모달 -->
  <div v-if="showModal" class="modal">
    <div class="modal-content">
      <div v-if="selectedEvents.length === 0">
        <h3>오늘 한 운동이 없습니다!</h3>
        <RouterLink :to="{ name: 'videoList' }"
          >바로 운동하러 가볼까요?</RouterLink
        >
      </div>
      <div v-else>
        <h3>{{ selectedDate }} 운동 완료!</h3>
        <ol>
          <div
          v-for="event in selectedEvents"
          :key="event.title"
          class="event-container"
          >
          <li
          class="event-item"
          >
          <RouterLink :to="`/${event.videoId}`" class="card-link">
            {{ decode(event.title) }}
          </RouterLink>
          </li>
          <button
            class="delete-button"
            @click="deleteEvent(event)"
            aria-label="삭제"
          >
            X
          </button>
        </div>
        </ol>
      </div>
      <button class="close-button" @click="closeModal">닫기</button>
    </div>
  </div>


<!-- Favorite Modal -->
<div v-if="favoriteModalVisible" class="modal">
  <div class="modal-content">
    <h4>{{ decode(selectedFavorite.title) }}</h4>
    <hr>
    <h5>{{ decode(selectedFavorite.channelName) }}</h5>
    <div class="modal-actions">
      <button class="regist-button" @click="registerFavorite(selectedFavorite)">
        오운완💪
      </button>
      <button class="del-button" @click="removeFavorite(selectedFavorite)">
        찜 해제❌
      </button>
    </div>
    <button class="close-button" @click="closeFavoriteModal">
      닫기
    </button>
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
        eventBackgroundColor: "#ffe4e0", // 이벤트 배경색
        eventBorderColor: "#ffe4e0", // 이벤트 테두리 색
        eventTextColor: "#8e4e4b", // 이벤트 텍스트 색
      },
      showModal: false, // 모달 표시 여부
      selectedDate: "", // 클릭한 날짜
      selectedEvents: [], // 해당 날짜의 이벤트 목록
      isLoading: false,
      showBox: false,
      favoriteModalVisible: false,
      selectedFavorite: null,
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
    gptResponse() {
      const recordStore = useRecordStore();
      return recordStore.gptMessage;
    },
    // GPT 응답을 HTML로 포맷하기 위한 computed 속성
    formattedGptResponse() {
      const rawResponse = this.gptResponse || "";

      // 마크다운 스타일을 HTML로 변환
      let formattedResponse = rawResponse
        .replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>") // **굵은 글씨**
        .replace(/\n/g, "<br>") // 줄바꿈 처리
        .replace(/- (.*?)(?=\n|$)/g, "<br>- $1"); // 목록 처리

      return formattedResponse;
    },
  },
  methods: {

    formatDate(timestamp) {
      const date = new Date(timestamp);
      const year = date.getUTCFullYear();
      const month = String(date.getUTCMonth() + 1).padStart(2, "0");
      const day = String(date.getUTCDate()).padStart(2, "0");
      return `${year}-${month}-${day}`;
    },
    truncateTitle(title) {
      return title.length > 15 ? `${title.slice(0, 15)}  ...` : title;
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
        videoId: rec.videoId,
        date: this.formatDate(rec.regDate),
      }));
    },
    async loadGPT() {
      this.showBox = true;
      this.isLoading = true;
      const recordStore = useRecordStore();
      try {
        await recordStore.getGptMessage();
      } catch (error) {
        console.log(error);
        recordStore.gptMessage = "AI 추천을 불러오는 데 실패했습니다";
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
      if (result === true) {
        this.calendarOptions.events.push({
          title: this.decode(fav.title),
          date: formattedToday,
          videoId: fav.videoId,
        });

        this.calendarOptions = { ...this.calendarOptions }; // Reactivity 보장
      } else if (result === 1) {
        alert("이미 등록된 운동입니다!");
      } else {
        alert("다시 시도해주세요!");
      }
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
      this.closeFavoriteModal();
    },
    decode(encodedStr) {
      const doc = new DOMParser().parseFromString(encodedStr, "text/html");
      return doc.documentElement.textContent;
    },
    showFavoriteModal(fav) {
    this.selectedFavorite = fav; // 선택한 favorite 정보 저장
    this.favoriteModalVisible = true; // 모달 표시
  },
  closeFavoriteModal() {
    this.favoriteModalVisible = false; // 모달 닫기
    this.selectedFavorite = null; // 선택한 favorite 초기화
  },
  registerFavorite(fav) {
    // 등록 로직 추가
    this.addVideoToToday(fav);
    this.closeFavoriteModal();
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
  /* justify-content: space-between; */
  margin-top: 50px;
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
  color: #574240; /* 빨간색 */
}

.calander-content {
  text-align: center;
  color: #574240; /* 빨간색 */
  font-style: italic;
}

/* 찜한 동영상 목록 스타일 */
.favorites-list-container {
  flex: 1; /* 목록 영역 */
  color: #574240;
  padding: 15px;
  border: 1px solid #ffdada; /* 연한 빨간 테두리 */
  border-radius: 8px;
  background-color: #fff5f5;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
}

/* 찜한 동영상 텍스트 중앙 정렬 */
.favorites-title {
  font-size: 1.5rem;
  text-align: center;
  margin-bottom: 20px;
  color: #574240;
}

/* favorite-item 간격 조정 */
.favorite-item {
  display: flex;
  justify-content: space-between; /* 버튼들을 양 끝으로 배치 */
  align-items: center;
  position: relative;
  margin-bottom: 20px; /* 간격 조정 */
  padding: 10px;
  background-color: #fff5f5; /* 연한 배경색 */
  border-radius: 5px;
}

/* 찜한 동영상 버튼 스타일 */
.favorite-button {
  display: block;
  width: 100%;
  background-color: white;
  border: 1px solid #ffdada; /* 연한 빨간 테두리 */
  color: #574240;
  border: none;
  padding: 10px;
  margin: 5px 0; /* 세로 간격을 줄이기 위해 기존 10px에서 5px으로 줄임 */
  margin-right: 10px;
  font-size: 1rem;
  border-radius: 5px;
  cursor: pointer;
  text-align: left;
  position: relative;
}

.favorite-button:hover {
  background-color: #ffd2cb;
}

/* favorite-item의 상대적 위치 설정 */
.favorite-item {
  position: relative;
  margin-bottom: 5px; /* 버튼 간격 줄이기 */
}

/* 삭제 버튼 스타일 */
.delete-button {
  background-color: #ff76764e;
  color: white;
  border: none;
  padding: 5px 10px;
  font-size: 1.2em;
  border-radius: 5px;
  cursor: pointer;
  margin-left: 10px;
}

.delete-button:hover {
  background-color: #ff7676;
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
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 5px;
  font-size: 1.1rem;
  color: #333;
  display: flex; /* Flexbox로 자식 정렬 */
  flex-direction: column; /* 자식 요소를 세로로 정렬 */
  justify-content: center; /* 세로 중앙 정렬 */
  align-items: center; /* 가로 중앙 정렬 */
  width: 100%; /* 부모(container)의 너비에 맞춤 */
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
  backdrop-filter: blur(5px); /* 배경 흐림 효과 */
  z-index: 1000; /* 상위 레이어 */

}

.modal-content {
  background-color: #ffffff;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0px 8px 20px rgba(0, 0, 0, 0.2); /* 부드러운 그림자 */
  width: 90%;
  max-width: 500px;
  text-align: center;
  animation: fadeIn 0.3s ease-out; /* 모달 등장 애니메이션 */
}
@keyframes fadeIn {
  from {
    transform: scale(0.9);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}
/* 제목 스타일 */
.modal-content h3 {
  font-size: 1.5rem;
  color: #333;
  margin-bottom: 20px;
  font-weight: bold;
}
.modal-content h5 {
  font-style: oblique;
}
.event-container {
  display: flex;
  align-items: center;
  justify-content: space-between; /* 이벤트 텍스트와 삭제 버튼을 양쪽으로 정렬 */
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 8px;
  transition: background-color 0.3s ease;
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
.card-link {
  text-decoration: none; /* 하이퍼링크 밑줄 제거 */
  color: inherit; /* 텍스트 색상 유지 */
}

/* 닫기 버튼 */
.close-button {
  background-color: #d6d6d6; /* 밝은 회색 */
  color: #333333; /* 진한 텍스트 */
  border: none;
  padding: 10px 20px;
  font-size: 1rem;
  border-radius: 8px;
  cursor: pointer;
  margin-top: 20px;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.close-button:hover {
  background-color: #bfbfbf; /* 더 짙은 회색 */
  transform: translateY(-2px);
}

.close-button:active {
  background-color: #a6a6a6; /* 클릭 시 더 짙은 회색 */
  transform: translateY(0);
}

.gpt-message-container {
  display: flex; /* Flexbox 사용 */
  justify-content: center; /* 가로 중앙 정렬 */
}


/* gpt-message 버튼 스타일 */
.gpt-message {
  background-color: #ff7676;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 1.1rem;
  border-radius: 5px;
  cursor: pointer;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.gpt-message:hover {
  background-color: #ff4c4c;
  transform: translateY(-2px);
}

.gpt-message:active {
  background-color: #e63b3b;
  transform: translateY(0);
}

.modal-actions {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
}
/* regist-button: 초록색 스타일 */
.regist-button {
  background-color: #5cb85c; /* 부드러운 초록색 */
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 1rem;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
}

.regist-button:hover {
  background-color: #4cae4c; /* 더 짙은 초록색 */
  transform: translateY(-2px);
}

.regist-button:active {
  background-color: #449d44; /* 클릭 시 더 짙은 초록색 */
  transform: translateY(0);
}

/* del-button: 회색 스타일 */
.del-button {
  background-color: #ff7676;
  border: none;
  padding: 10px 20px;
  font-size: 1rem;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
}

.del-button:hover {
  background-color: #ff4c4c;
  transform: translateY(-2px);
}

.del-button:active {
  background-color: #e63b3b;
  transform: translateY(0);
}
.modal-button {
  background-color: #ff7676;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 1rem;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.modal-button:hover {
  background-color: #ff4c4c;
}
.modal-button:active {
  background-color: #e63b3b;
}
/* 텍스트 스타일 */
.modal-content p {
  font-size: 1rem;
  color: #555;
  line-height: 1.6;
}
</style>