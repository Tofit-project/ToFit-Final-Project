<template>
  <div class="main-page">
    <section
      class="recommended-section"
      v-if="userStore.loginUserProfileName !== null"
    >
      <h4 style="font-weight: bold">
        {{ userStore.loginUserProfileName }}님을 위한 추천 운동
      </h4>
      <div class="card-container">
        <div
          class="card"
          v-for="(video, index) in paginatedRecomVideos"
          :key="index"
        >
          <RouterLink :to="`/${video.videoId}`" class="card-link">
            <img :src="video.thumbnail" alt="thumbnail" class="thumbnail" />
            <div class="card-content">
              <div v-if="video.instructorImage">
                <img
                  :src="video.instructorImage"
                  alt="profile"
                  class="instructor-img"
                />
              </div>
              <div v-else>
                <img
                  src="/images/default_profile.png"
                  alt="profile"
                  class="instructor-img"
                />
              </div>
              <div class="info">
                <h5 class="title">{{ decode(video.title) }}</h5>
                <p class="instructor">{{ decode(video.channelName) }}</p>
              </div>
            </div>
          </RouterLink>
        </div>
      </div>
      <div v-if="totalPages > 1" class="pagination">
        <button
          class="page-btn"
          :disabled="currentPage === 1"
          @click="prevPage"
        >
          이전
        </button>
        <span>{{ currentPage }} / {{ totalPages }}</span>
        <button
          class="page-btn"
          :disabled="currentPage === totalPages"
          @click="nextPage"
        >
          다음
        </button>
      </div>
    </section>

    <section class="all-section">
      <h4 style="font-weight: bold">내 목표에 맞는 운동을 찾아보세요!</h4>
      <div class="search-filter">
        <input
          type="text"
          v-model="searchInfo.keyWord"
          placeholder="영상제목 또는 강사명을 입력해주세요"
          @keyup.enter="searchVideoList"
          class="search-bar"
        />
        <button class="search-btn" @click="searchVideoList">검색</button>
      </div>
      <div class="filter-buttons">
        <button
          v-for="(filter, index) in filters"
          :key="index"
          class="filter-btn"
          :class="{ active: activeFilter === filter }"
          @click="applyFilter(filter)"
        >
          {{ filter }}
        </button>
      </div>
      <div class="card-container">
        <div class="card" v-for="(video, index) in filteredVideos" :key="index">
          <RouterLink :to="`/${video.videoId}`" class="card-link">
            <img :src="video.thumbnail" alt="thumbnail" class="thumbnail" />
            <div class="card-content">
              <div v-if="video.instructorImage">
                <img
                  :src="video.instructorImage"
                  alt="profile"
                  class="instructor-img"
                />
              </div>
              <div v-else>
                <img
                  src="/images/default_profile.png"
                  alt="profile"
                  class="instructor-img"
                />
              </div>
              <div class="info">
                <h5 class="title">{{ decode(video.title) }}</h5>
                <p class="instructor">{{ decode(video.channelName) }}</p>
              </div>
            </div>
          </RouterLink>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { useVideoStore } from "@/stores/video";
import { useUserStore } from "@/stores/user";

const store = useVideoStore();
const userStore = useUserStore();

const searchInfo = ref({
  keyWord: "",
});

const activeFilter = ref("");
const filters = [
  "다이어트",
  "근력강화",
  "체형교정",
  "스트레칭",
  "명상",
  "식단",
];

const currentPage = ref(1); // 현재 페이지
const itemsPerPage = 3; // 페이지당 항목 수

// 추천 영상 목록의 페이지 계산
const paginatedRecomVideos = computed(() => {
  const startIndex = (currentPage.value - 1) * itemsPerPage;
  const endIndex = startIndex + itemsPerPage;
  return store.recomVideoList.slice(startIndex, endIndex);
});

// 전체 페이지 수
const totalPages = computed(() =>
  Math.ceil(store.recomVideoList.length / itemsPerPage)
);

// 페이지 이동 핸들러
const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
  }
};

const searchVideoList = function () {
  store.searchVideoList(searchInfo.value);
};

const filteredVideos = computed(() => {
  let videos = store.videoList;

  if (activeFilter.value) {
    videos = videos.filter((video) => video.goal === activeFilter.value);
  }

  return videos;
});

// 필터 버튼 클릭 핸들러
const applyFilter = (filter) => {
  activeFilter.value = activeFilter.value === filter ? "" : filter; // 동일 필터 클릭 시 해제
};

// HTML 엔티티 변환 출력
const decode = function (encodedStr) {
  const doc = new DOMParser().parseFromString(encodedStr, "text/html");
  return doc.documentElement.textContent;
};

onMounted(async () => {
  userStore.checkLoginStatus(); // 로그인 상태 갱신
  await store.getRecomVideoList(); // 토큰이 있을 때만 추천 영상 가져오기
  searchVideoList();
});
</script>

<style scoped>
.main-page {
  padding: 1rem;
}

.recommended-section,
.all-section {
  margin-bottom: 5rem;
}

.card-container {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
}

.card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: calc(33% - 1rem);
  overflow: hidden;
}

/* 카드 호버 효과 */
.card:hover {
  transform: scale(1.05); /* 확대 효과 */
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2); /* 더 강한 그림자 */
}

/* RouterLink 기본 스타일 제거 */
.card-link {
  text-decoration: none; /* 하이퍼링크 밑줄 제거 */
  color: inherit; /* 텍스트 색상 유지 */
}

.thumbnail {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

.card-content {
  display: flex;
  padding: 1rem;
  align-items: center;
}

.instructor-img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 1rem;
}

.info {
  display: flex;
  flex-direction: column;
}

.title {
  font-size: 1rem;
  font-weight: bold;
}

.instructor {
  font-size: 0.9rem;
  color: #555;
}

.search-filter {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.search-bar {
  flex: 1;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.search-btn {
  background-color: #f17979;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.search-btn:hover {
  background-color: #f26465;
}

.filter-buttons {
  display: flex;
  justify-content: center; /* 가운데 정렬 */
  flex-wrap: wrap;
  gap: 0.8rem; /* 버튼 간격 */
  margin-bottom: 1.5rem;
}

.filter-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 20px;
  background-color: #ffe4e0;
  color: #8e4e4b;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.filter-btn.active {
  background-color: #fabcb0;
  color: #8e4e4b;
  font-weight: bold;
}

.filter-btn:hover {
  transform: scale(1.05);
  background-color: #fabcb0;
  font-weight: bold;
}
.pagination {
  display: flex;
  justify-content: center; /* 가운데 정렬 */
  align-items: center;
  gap: 1rem; /* 버튼 간격 */
  margin-top: 1rem;
}

.page-btn {
  padding: 0.5rem 1rem;
  background-color: #f17979; /* 대표 색상 */
  color: white;
  border: 1px solid #f17979; /* 테두리 색상 */
  border-radius: 5px;
  font-weight: bold;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s ease;
}

.page-btn:hover {
  background-color: #f26465; /* 호버시 색상 */
  transform: scale(1.05); /* 버튼 확대 효과 */
}

.page-btn:disabled {
  background-color: #ddd; /* 비활성화된 버튼 색상 */
  color: #999;
  cursor: not-allowed;
  border: 1px solid #ddd;
}
</style>
