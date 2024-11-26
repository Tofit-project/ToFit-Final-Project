<template>
  <div class="profile-container">
    <h1 class="page-title">마이페이지</h1>

    <div v-if="userStore.loginUserId" class="profile-card">
      <div class="profile-img-container">
        <img
          :src="userStore.userInfo.profileImg"
          alt="Profile Image"
          class="profile-img"
          v-if="userStore.userInfo.profileImg"
        />
        <div v-else class="no-profile-img">No Image</div>
      </div>
      <div class="profile-info">
        <p><strong>아이디:</strong> {{ userStore.userInfo.userId }}</p>
        <p><strong>프로필명:</strong> {{ userStore.userInfo.profileName }}</p>
        <p><strong>이메일:</strong> {{ userStore.userInfo.email }}</p>
        <p><strong>성별:</strong> {{ genderText }}</p>
        <p><strong>생년월일:</strong> {{ formattedBirth }}</p>
      </div>

      <!-- 탈퇴하기 버튼 -->
      <button @click="handleDeleteAccount" class="delete-button">
        탈퇴하기
      </button>
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
</script>

<style scoped>
/* 전체 컨테이너 스타일 */
.profile-container {
  width: 90%;
  max-width: 500px; /* 카드 가로폭을 좁힘 */
  margin: 50px auto;
  text-align: center;
  font-family: "Arial", sans-serif;
}

.page-title {
  font-size: 2.5rem; /* 제목 크기 키움 */
  font-weight: 700;
  color: #2c3e50; /* 어두운 회색으로 변경 */
  margin-bottom: 40px;
  text-align: center;
  text-transform: uppercase; /* 제목 대문자로 변경 */
  letter-spacing: 1px; /* 제목 간격을 조금 넓힘 */
  font-family: "Poppins", sans-serif;
}

/* 프로필 카드 */
.profile-card {
  background-color: #fff;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  padding: 30px;
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: all 0.3s ease;
  margin-bottom: 40px;
}

.profile-card:hover {
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
}

.profile-img-container {
  position: relative;
  margin-bottom: 20px;
}

.profile-img {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #eee;
}

.no-profile-img {
  width: 120px;
  height: 120px;
  border-radius: 50%;
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
  margin-top: 20px;
}

.profile-info p {
  font-size: 18px; /* 글자 크기 키움 */
  margin: 12px 0;
  color: #555;
}

.profile-info strong {
  color: #0056b3;
  font-weight: bold;
}

/* 로그인 정보가 없을 때 */
.no-user-info {
  font-size: 18px; /* 글자 크기 키움 */
  color: #f44336;
  font-weight: bold;
  margin-top: 30px;
}

/* 탈퇴하기 버튼 스타일 */
.delete-button {
  margin-top: 30px;
  padding: 12px 25px;
  background-color: #e74c3c;
  color: white;
  font-size: 18px;
  border: none;
  border-radius: 30px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.3s;
}

.delete-button:hover {
  background-color: #c0392b;
  transform: scale(1.05);
}

.delete-button:focus {
  outline: none;
}
</style>
