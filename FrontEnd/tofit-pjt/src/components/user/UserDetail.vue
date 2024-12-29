<template>
  <div class="profile-container">
    <h2 class="page-title">나의 정보</h2>
    <div class="profile-img-container">
      <img
        :src="userStore.userInfo.profileImg"
        alt="Profile Image"
        class="profile-img"
        v-if="userStore.userInfo.profileImg"
      />
      <div v-else class="no-profile-img">No Image</div>
    </div>
    <p class="guide-line">
      가입하신 회원정보입니다. 개인정보 수정 시 하단의 '수정하기' 버튼을 눌러
      변경할 수 있습니다.
    </p>
    <hr class="guide-line-divider" />

    <div v-if="userStore.loginUserId" class="profile-content">
      <!-- 프로필 이미지 및 정보 -->
      <div class="profile-header">
        <div class="profile-info">
          <p>
            <strong>아이디</strong>
            <input
              type="text"
              :value="userStore.userInfo.userId"
              readonly
              class="profile-input"
            />
          </p>
          <p>
            <strong>프로필명</strong>
            <input
              type="text"
              :value="userStore.userInfo.profileName"
              readonly
              class="profile-input"
            />
          </p>
          <p>
            <strong>이메일</strong>
            <input
              type="email"
              :value="userStore.userInfo.email"
              readonly
              class="profile-input"
            />
          </p>
          <p>
            <strong>성별</strong>
            <input
              type="text"
              :value="genderText"
              readonly
              class="profile-input"
            />
          </p>
          <p>
            <strong>생년월일</strong>
            <input
              type="text"
              :value="formattedBirth"
              readonly
              class="profile-input"
            />
          </p>
        </div>
      </div>

      <!-- 탈퇴하기 및 수정하기 버튼 -->
      <div class="actions">
        <button @click="handleEditAccount" class="edit-button">수정하기</button>
        <button @click="handleDeleteAccount" class="delete-button">
          탈퇴하기
        </button>
      </div>
    </div>

    <div v-else class="no-user-info">
      <p>사용자 정보가 없습니다. 로그인 후 다시 시도해주세요.</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted, computed } from "vue";
import { useUserStore } from "@/stores/user";
import router from "@/router";

const userStore = useUserStore();

onMounted(() => {
  userStore.checkLoginStatus();
  userStore.getUserInfo();
});

// 성별 처리
const genderText = computed(() => {
  return userStore.userInfo.gender === "M" ? "남자" : "여자";
});

// 생년월일 파싱
const formattedBirth = computed(() => {
  const birthDate = userStore.userInfo.birth;
  return birthDate ? birthDate.substring(0, 10) : "";
});

const handleDeleteAccount = () => {
  if (confirm("정말로 탈퇴하시겠습니까?")) {
    userStore.deleteAccount();
    alert("탈퇴가 완료되었습니다.");
    router.push({ name: "videoList" });
    sessionStorage.removeItem("access-token");
    sessionStorage.removeItem("profile-img");
    userStore.loginUserId.value = null;
    userStore.loginUserProfileName.value = null;
    userStore.loginUserProfileImage.value = null;
  }
};

const handleEditAccount = () => {
  alert("계정 수정 페이지로 이동합니다.");
  // 수정 페이지로 이동하는 로직 추가 (예: router.push({ name: 'editAccount' }))
};
</script>

<style scoped>
.profile-container {
  width: 90%;
  max-width: 500px; /* 너비 제한 */
  margin: 50px auto;
  font-family: "Arial", sans-serif;
  text-align: center;
  border: 2px solid #ddd;
  padding: 50px;
}

/* 페이지 제목 */
.page-title {
  font-size: 2rem;
  font-weight: 700;
  color: #9c9b9b;
  margin-bottom: 20px; /* 제목과 프로필 이미지 간격 조정 */
  text-transform: uppercase;
  letter-spacing: 1px;
  font-family: "Poppins", sans-serif;
}

/* 가이드라인 스타일 */
.guide-line {
  font-size: 14px;
  color: #7d7d7d;
  margin-bottom: 20px;
  font-family: "Arial", sans-serif;
}

/* 구분선 스타일 */
.guide-line-divider {
  border: 0;
  border-top: 1px solid #939191;
  margin-bottom: 30px;
}

/* 프로필 내용 스타일 */
.profile-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center;
}

.profile-header {
  display: flex;
  flex-direction: column; /* 세로로 배치 */
  align-items: center; /* 중앙 정렬 */
  gap: 20px; /* 이미지와 텍스트 간격 */
}

.profile-img-container {
  position: relative;
  width: 120px; /* 이미지 크기 */
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  margin-bottom: 30px;
  margin-left: auto; /* 중앙 정렬을 위해 왼쪽 여백 자동으로 설정 */
  margin-right: auto; /* 중앙 정렬을 위해 오른쪽 여백 자동으로 설정 */
}

.profile-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.no-profile-img {
  width: 100%;
  height: 100%;
  background-color: #e0e0e0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: #666;
}

.profile-info {
  text-align: left;
  width: 100%;
}

.profile-info p {
  font-size: 16px;
  color: #555;
  margin: 10px 0; /* 항목 간격 */
  display: flex;
  align-items: center;
}

.profile-info strong {
  color: #46484b;
  font-weight: bold;
  min-width: 80px; /* strong의 고정 너비 설정 */
}

.profile-info input {
  flex: 1; /* 나머지 공간을 차지하도록 설정 */
  text-align: left; /* input 태그의 텍스트 왼쪽 정렬 */
  margin-left: 60px;
  border: none;
  background-color: #faf9f9;
  padding: 7px;
  font-size: 16px;
  border-radius: 5px;
  width: 250px;
}

.profile-info input[readonly] {
  cursor: not-allowed;
  background-color: #efeded;
}

/* 탈퇴하기 및 수정하기 버튼 */
.actions {
  display: flex;
  gap: 10px;
  justify-content: center;
}

.edit-button,
.delete-button {
  padding: 10px 20px;
  font-size: 14px;
  border-radius: 5px; /* 덜 둥글게 */
  cursor: pointer;
  transition: background-color 0.3s, transform 0.3s;
  margin-top: 30px;
}

.edit-button {
  background-color: #d65142;
  color: white;
  border: none;
}

.delete-button {
  background-color: #f77364;
  color: white;
  border: none;
}

.edit-button:hover,
.delete-button:hover {
  transform: scale(1.05);
}

.edit-button:focus,
.delete-button:focus {
  outline: none;
}

/* 로그인 정보가 없을 때 */
.no-user-info {
  font-size: 18px;
  color: #f44336;
  font-weight: bold;
  margin-top: 30px;
}
</style>
