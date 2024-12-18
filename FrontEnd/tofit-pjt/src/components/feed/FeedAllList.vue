<template>
  <div class="container">
    <div v-if="feedStore.feedAllList.length > 0" class="feed-list">
      <div
        v-for="(feed, index) in feedStore.feedAllList"
        :key="index"
        class="feed-card"
        @click="openModal(feed)"
      >
        <div class="feed-header">
          <!-- 프로필 이미지 클릭 시 해당 사용자 ID로 이동 -->
          <router-link :to="'/feed/' + feed.feed.userId">
            <div v-if="feed.profileImg">
              <img
                :src="feed.profileImg"
                alt="Profile Image"
                class="profile-img"
              />
            </div>
            <div v-else>
              <img
                src="/images/default_profile.png"
                alt="Profile Image"
                class="profile-img"
              />
            </div>
          </router-link>
          <p
            class="profile-name"
            style="font-weight: bold; margin-top: 15px; font-size: large"
          >
            {{ feed.profileName }}
          </p>
        </div>
        <div class="feed-content">
          <p>{{ feed.feed.content }}</p>
        </div>
        <div
          v-if="feed.images && feed.images.length > 0"
          class="feed-image-container"
        >
          <img :src="feed.images[0].img" alt="피드 이미지" />
        </div>
        <div class="feed-footer">
          <p class="feed-date">{{ formatTimestamp(feed.feed.regDate) }}</p>
        </div>
      </div>
    </div>
    <p v-else>작성된 피드가 없습니다.</p>

    <!-- 모달 시작부분 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1-->
    <div v-if="selectedFeed" class="modal-background">
      <div class="modal-content">
        <button class="close-modal" @click="closeModal">x</button>
        <div class="profile-info">
          <div v-if="selectedFeed.profileImg != null">
            <img
              :src="selectedFeed.profileImg"
              alt="Profile Image"
              class="profile-img"
            />
          </div>
          <div v-else>
            <img
              src="/images/default_profile.png"
              alt="Profile Image"
              class="profile-img"
            />
          </div>
          <p class="profile-name">{{ selectedFeed.profileName }}</p>
        </div>
        <div v-if="selectedFeed.images.length > 0" class="modal-images">
          <button class="arrow left" @click="prevImage"><</button>
          <div class="modal-image">
            <img
              :src="selectedFeed.images[currentImageIndex].img"
              alt="피드 이미지!"
              style="border-radius: 5px"
            />
          </div>
          <button class="arrow right" @click="nextImage">></button>
        </div>

        <!-- 피드 내용 -->
        <p class="modal-feed-content">{{ selectedFeed.feed.content }}</p>
        <div class="modal-actions">
          <div v-if="selectedFeed.feed.userId === userStore.loginUserId">
            <button class="modal-button edit-button">수정</button>
            <button
              class="modal-button delete-button"
              @click="deleteFeed(selectedFeed.feed.feedId)"
            >
              삭제
            </button>
          </div>
        </div>
        <!-- 피드 댓글 -->
        <!-- 댓글 작성 폼 -->
        <div class="comment-form">
          <div class="textarea-wrapper">
            <textarea
              v-model="newComment.content"
              placeholder="댓글을 작성하세요..."
              rows="1"
              @keyup.enter="submitComment(selectedFeed.feed.feedId)"
            ></textarea>
            <button
              @click="submitComment(selectedFeed.feed.feedId)"
              class="submit-btn"
            >
              등록
            </button>
          </div>
        </div>
        <!-- 댓글 리스트 출력 -->
        <!-- 댓글 리스트 출력 -->
        <div
          class="comment-list"
          v-if="feedReviewStore.feedReviewList.length !== 0"
        >
          <div
            v-for="(review, index) in feedReviewStore.feedReviewList"
            :key="review.reviewId"
            class="review-card"
          >
            <div class="review-header">
              <div v-if="review.profileImg !== null">
                <img
                  :src="review.profileImg"
                  alt="Author Image"
                  class="profile-img"
                />
              </div>
              <div v-else>
                <img src="/images/default_profile.png" class="profile-img" />
              </div>
              <div class="review-author">
                <strong>{{ review.profileName }}</strong>
                <span class="review-date">
                  {{ formatDate(review.regDate) }}</span
                >
              </div>
            </div>

            <!-- 수정 모드 -->
            <div v-if="editingId === review.reviewId">
              <textarea
                v-model="editingContent"
                rows="1"
                class="edit-textarea"
              ></textarea>
              <div class="edit-actions">
                <button class="edit-submit" @click="confirmEdit(review)">
                  완료
                </button>
                <button class="edit-cancel" @click="cancelEdit">취소</button>
              </div>
            </div>

            <!-- 기본 상태 -->
            <div v-else>
              <p class="review-content">{{ review.content }}</p>
              <div
                v-if="review.userId === userStore.loginUserId"
                class="comment-actions"
              >
                <button @click="startEditing(review.reviewId, review.content)">
                  수정
                </button>
                <button class="delete" @click="deleteComment(review)">
                  삭제
                </button>
              </div>
            </div>
          </div>
        </div>
        <div v-else>
          <p>아직 댓글이 없습니다</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useFeedStore } from "@/stores/feed";
import { useUserStore } from "@/stores/user";
import { useFeedReviewStore } from "@/stores/feedReview";
import { RouterView } from "vue-router";
import { useRoute } from "vue-router";

const feedReviewStore = useFeedReviewStore();
const userStore = useUserStore();
const feedStore = useFeedStore();
const route = useRoute();
const selectedFeed = ref(null);
const currentImageIndex = ref(0);

onMounted(() => {
  feedStore.getFeedList();
  userStore.checkLoginStatus();
});

const openModal = (feed) => {
  selectedFeed.value = feed;

  feedReviewStore.getFeedReviewList(selectedFeed.value.feed.feedId);
};

const closeModal = () => {
  selectedFeed.value = null;
  currentImageIndex.value = 0; // 모달을 닫을 때 이미지 인덱스 초기화
};

const prevImage = () => {
  currentImageIndex.value =
    (currentImageIndex.value - 1 + selectedFeed.value.images.length) %
    selectedFeed.value.images.length;
};

const nextImage = () => {
  currentImageIndex.value =
    (currentImageIndex.value + 1) % selectedFeed.value.images.length;
};

const deleteFeed = (feedId) => {
  feedStore.feedDelete(feedId);
  closeModal();
};

const formatTimestamp = (timestamp) => {
  const date = new Date(timestamp);
  return date.toLocaleString("ko-KR", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
  });
};
const newComment = ref({
  feedId: 0,
  content: "",
}); // 새 댓글 내용

// 댓글 수정 상태 관리
const editingId = ref(null); // 현재 수정 중인 댓글 ID
const editingContent = ref(""); // 수정 중인 댓글 내용

// 댓글 작성
const submitComment = (feedId) => {
  if (newComment.value.content.trim() !== "") {
    newComment.value.feedId = feedId;
    feedReviewStore.addReview(newComment.value);
    newComment.value.content = "";
  }
};

// 댓글 삭제
const deleteComment = function (review) {
  feedReviewStore.removeReview(review);
};

// 댓글 수정 모드 시작
const startEditing = (reviewId, content) => {
  editingId.value = reviewId;
  editingContent.value = content;
};

// 댓글 수정 등록
const confirmEdit = (review) => {
  feedReviewStore.updateReview({
    reviewId: review.reviewId,
    feedId: review.feedId,
    content: editingContent.value,
  });

  editingId.value = null; // 수정 모드 종료
  editingContent.value = "";
};

// 댓글 수정 취소
const cancelEdit = () => {
  editingId.value = null; // 수정 모드 종료
  editingContent.value = "";
};

// 날짜 형식 변환 함수
const formatDate = (date) => {
  const formattedDate = new Date(date);
  const year = formattedDate.getFullYear();
  const month = String(formattedDate.getMonth() + 1).padStart(2, "0");
  const day = String(formattedDate.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
};
</script>

<style scoped>
/* 전체 페이지 스타일 */
.container {
  padding: 20px;
  display: flex;
  width: 47%;
  flex-direction: column;
  align-items: center;
  background-color: rgb(163, 173, 173);
}

.feed-list {
  display: flex;
  flex-direction: column;
  gap: 25px;
  align-items: center;
}

.feed-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 100%;
  padding: 10px;
  cursor: pointer;
}

.feed-header {
  display: flex;
  align-items: center;
}

/* 프로필 이미지와 이름을 수평으로 나란히 배치 */
.profile-info {
  display: flex;
  align-items: center;
}

/* 프로필 이미지 크기 설정 */
.profile-img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

/* 프로필 이름 스타일 */
.profile-name {
  font-size: 1.2rem;
  font-weight: bold;
  color: #333;
  margin-top: 10px;
}

.feed-image-container {
  width: 100%; /* 카드의 너비를 따라감 */
  overflow: hidden; /* 이미지를 자름 */
  display: flex;
  justify-content: center;
  align-items: center;
}

.feed-image-container img {
  max-width: 100%; /* 컨테이너 너비에 맞게 이미지 크기 조정 */
  max-height: 100%; /* 컨테이너 높이에 맞게 이미지 크기 조정 */
  object-fit: cover; /* 비율 유지하며 채움 */
  border-radius: 5px; /* 이미지 모서리 둥글게 */
}

.feed-content {
  margin-top: 10px;
  margin-left: 10px;
}

.feed-footer {
  margin-top: 50px;
  text-align: right;
}

.feed-date {
  color: #777;
  font-size: 12px;
  margin-right: 10px;
}

/* 모달 배경 */
.modal-background {
  position: fixed; /* 스크롤 시 배경 고정 */
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  overflow: hidden; /* 모달 배경에서 스크롤 막기 */
}

/* 모달 콘텐츠 */
.modal-content {
  position: relative;
  background: white;
  padding: 20px;
  width: 600px;
  max-height: 100vh; /* 뷰포트의 90% 높이까지 확장 가능 */
  overflow-y: auto; /* 내부에서 스크롤 가능하게 설정 */
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

/* 닫기 버튼 */
.close-modal {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: #f26465;
  color: white;
  border: none;
  width: 40px;
  height: 40px;
  font-size: 20px;
  font-weight: bold;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
  display: flex;
  justify-content: center;
  align-items: center;
}

.close-modal:hover {
  background-color: #d45051;
}

/* 이미지 슬라이드 */
.modal-images {
  position: relative;
  text-align: center;
  margin-bottom: 20px;
  height: 300px; /* 이미지 영역 크기 축소 */
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-image img {
  max-width: 80%;
  max-height: 80%;
  object-fit: contain;
  border-radius: 5px;
}

.arrow {
  position: absolute;
  top: 50%;
  /* transform: translateY(-50%); */
  /* background-color: rgba(0, 0, 0, 0.5); */
  border: none;
  padding: 10px;
  font-size: 24px;
  color: rgb(113, 104, 104);
  border-radius: 20px;
  cursor: pointer;
  z-index: 1;
}

.arrow.left {
  left: 10px;
}

.arrow.right {
  right: 10px;
}

/* 피드 내용 */
.modal-feed-content {
  font-size: 16px;
  line-height: 1.5;
  font-weight: bold;
  max-height: 300px;
  overflow-y: auto;
  margin-top: 5px;
}

/* 수정/삭제 버튼 */
.modal-actions {
  display: flex;
  justify-content: flex-end;
  /* margin-top: 5px; */
  margin-bottom: 15px;
}

.modal-button {
  padding: 10px 20px;
  margin-left: 10px;
  font-size: 14px;
  cursor: pointer;
  border: none;
  border-radius: 5px;
}

.edit-button {
  background-color: #bfbfbf;
  color: white;
}

.delete-button {
  background-color: #f44336;
  color: white;
}

/* 공통 */
.review-title {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
  text-align: center;
}

textarea {
  width: 100%;
  font-size: 1rem;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  outline: none;
  resize: none;
  transition: border-color 0.3s ease;
}

textarea:focus {
  border-color: #ff848f;
  box-shadow: 0 0 4px rgba(255, 132, 143, 0.4);
}

/* 댓글 작성 폼 */
.comment-form + div {
  /* 댓글 리스트 컨테이너 */
  max-height: 300px; /* 댓글 리스트의 최대 높이 제한 */
  overflow-y: auto; /* 스크롤바 활성화 */
  margin-top: 1rem;
  padding-right: 10px; /* 스크롤바 여유 공간 */
}

.textarea-wrapper {
  position: relative;
  width: 100%;
}

.submit-btn {
  position: absolute;
  bottom: 10px;
  right: 10px;
  padding: 0.5rem 1rem;
  font-size: 0.9rem;
  color: white;
  background-color: #ff848f;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.submit-btn:hover {
  background-color: #e63946;
}

.comment-list {
  max-height: 800px; /* 스크롤 영역 최대 높이 설정 */
  overflow-y: auto; /* 세로 스크롤 활성화 */
  padding: 10px;
  border: 1px solid #ddd; /* 스크롤 영역 테두리 추가 (선택 사항) */
  border-radius: 8px;
  background-color: #f9f9f9; /* 배경색 추가 (선택 사항) */
}
/* 댓글 카드 */
.review-card {
  background: #ffffff;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: relative;
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
}

.review-header {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
}

.profile-img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;
}

.review-author {
  font-weight: bold;
  font-size: 1.1rem;
  color: #555;
}

.review-author .review-date {
  margin-left: 10px;
  font-size: 0.9rem;
  color: #aaa;
}

/* 수정 중일 때 */
.edit-textarea {
  padding: 0.8rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  margin-bottom: 1rem;
}

.edit-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}

/* 수정 버튼 색상 */
.edit-submit,
.edit-cancel {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 8px;
  color: white;
  font-size: 0.9rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.edit-submit {
  background-color: #ff848f;
}

.edit-submit:hover {
  background-color: #e63946;
}

.edit-cancel {
  background-color: #f9ccd4;
}

.edit-cancel:hover {
  background-color: #f5a9b8;
}

/* 댓글 액션 버튼 */
.comment-actions {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
}

.comment-actions button {
  padding: 0.4rem 1rem;
  background-color: #ff848f;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.comment-actions button:hover {
  background-color: #e63946;
}

.comment-actions .delete {
  background-color: #f9ccd4;
}

.comment-actions .delete:hover {
  background-color: #f5a9b8;
}
</style>
