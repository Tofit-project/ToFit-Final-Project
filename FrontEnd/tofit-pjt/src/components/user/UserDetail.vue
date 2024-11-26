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
        <p><strong>성별:</strong> {{ userStore.userInfo.gender }}</p>
        <p><strong>생년월일:</strong> {{ userStore.userInfo.birth }}</p>
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
import { onMounted } from "vue";
import { useUserStore } from "@/stores/user";
import router from "@/router";

const userStore = useUserStore();

onMounted(() => {
  userStore.checkLoginStatus();
  userStore.getUserInfo();
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
  margin: 50px auto;
  text-align: center;
  font-family: "Arial", sans-serif;
}

.page-title {
  font-size: 2rem;
  font-weight: bold;
  color: #333;
  margin-bottom: 40px;
}

/* 프로필 카드 */
.profile-card {
  background-color: #fff;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  width: 500px;
  margin: 0 auto;
  padding: 30px;
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: all 0.3s ease;
}

.profile-card:hover {
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.1);
}

.profile-img-container {
  position: relative;
  margin-bottom: 20px;
}

.profile-img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #eee;
}

.no-profile-img {
  width: 100px;
  height: 100px;
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
}

.profile-info p {
  font-size: 16px;
  margin: 12px 0;
  color: #555;
}

.profile-info strong {
  color: #0056b3;
  font-weight: bold;
}

/* 로그인 정보가 없을 때 */
.no-user-info {
  font-size: 16px;
  color: #f44336;
  font-weight: bold;
  margin-top: 30px;
}

/* 탈퇴하기 버튼 스타일 */
.delete-button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #e74c3c;
  color: white;
  font-size: 16px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.delete-button:hover {
  background-color: #c0392b;
}

.delete-button:focus {
  outline: none;
}
</style>
