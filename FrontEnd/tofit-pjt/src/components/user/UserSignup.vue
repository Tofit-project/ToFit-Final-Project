<template>
  <div class="container">
    <div class="form-wrapper">
      <h2 class="form-title">회원가입</h2>
      <!-- <hr class="guide-line-divider" /> -->
      <!-- 프로필 이미지 -->
      <div class="form-group profile-section">
        <div class="profile-img">
          <img
            :src="imageUrl || defaultProfileImage"
            alt="프로필 이미지 미리보기"
            class="img-thumbnail"
          />
          <div class="button-group">
            <button
              type="button"
              class="btn btn-primary profile-btn"
              @click="triggerFileInput"
            >
              등록하기
            </button>
            <button
              type="button"
              class="btn btn-danger profile-btn"
              @click="clearImage"
            >
              삭제
            </button>
          </div>
          <input
            type="file"
            id="profileImg"
            class="form-control d-none"
            accept="image/*"
            @change="onFileChange"
            ref="fileInput"
          />
        </div>
      </div>

      <!-- 아이디 -->
      <div class="form-group">
        <label for="userId">아이디</label>
        <div class="input-group">
          <input
            type="text"
            id="userId"
            v-model="user.id"
            class="form-control short-input"
            placeholder="아이디를 입력하세요"
            required
          />
          <button
            type="button"
            class="btn btn-secondary check-btn"
            @click="idCheck(user.id)"
          >
            중복확인
          </button>
        </div>
        <p v-if="idCheckMessage" :class="idCheckMessageClass">
          {{ idCheckMessage }}
        </p>
      </div>

      <!-- 비밀번호 -->
      <div class="form-group">
        <label for="password">비밀번호</label>
        <input
          type="password"
          id="password"
          v-model="user.password"
          class="form-control"
          placeholder="비밀번호를 입력하세요"
          required
        />
      </div>

      <!-- 이메일 -->
      <div class="form-group">
        <label for="email">이메일</label>
        <div class="input-group">
          <input
            type="email"
            id="email"
            v-model="user.mail"
            class="form-control short-input"
            placeholder="example@domain.com"
            required
          />
          <button type="button" class="btn btn-secondary check-btn" @click="">
            인증하기
          </button>
        </div>
      </div>

      <!-- 성별 -->
      <div class="form-group gender-group">
        <label>성별</label>
        <div class="gender-options">
          <label class="gender-label">
            <input type="radio" value="M" v-model="user.gender" required />
            남성
          </label>
          <label class="gender-label">
            <input type="radio" value="F" v-model="user.gender" required />
            여성
          </label>
        </div>
      </div>

      <!-- 생년월일 -->
      <div class="form-group">
        <label for="birth">생년월일</label>
        <input
          type="date"
          id="birth"
          v-model="user.birth"
          class="form-control"
          required
        />
      </div>

      <!-- 프로필명 -->
      <div class="form-group">
        <label for="profileName">프로필명</label>
        <div class="input-group">
          <input
            type="text"
            id="profileName"
            v-model="user.profileName"
            class="form-control short-input"
            placeholder="프로필명을 입력하세요"
            required
          />
          <button
            type="button"
            class="btn btn-secondary check-btn"
            @click="profileNameCheck(user.profileName)"
          >
            중복확인
          </button>
        </div>
        <p v-if="pCheckMessage" :class="pCheckMessageClass">
          {{ pCheckMessage }}
        </p>
      </div>

      <!-- 회원가입 버튼 -->
      <button @click="signUp" class="btn sign-up-btn">가입하기</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";
import { useUserStore } from "@/stores/user";
import router from "@/router";

const userStore = useUserStore();

const defaultProfileImage = "/images/default_profile.png";

const user = ref({
  id: "",
  password: "",
  mail: "",
  gender: "",
  birth: "",
  profileName: "",
  profileImage: null,
});

const idCheckMessage = ref("");
const idCheckMessageClass = ref("");

// 아이디 중복확인
const idCheck = (id) => {
  if (!id) {
    idCheckMessage.value = "아이디를 입력하세요.";
    idCheckMessageClass.value = "text-danger";
    return;
  }

  axios
    .get(`http://localhost:8080/tofit/users/id-check`, {
      params: { userId: id },
    })
    .then((response) => {
      if (response.data === false) {
        idCheckMessage.value = "사용 가능한 아이디입니다.";
        idCheckMessageClass.value = "text-primary";
      } else {
        idCheckMessage.value = "이미 사용 중인 아이디입니다.";
        idCheckMessageClass.value = "text-danger";
        user.value.id = ""; // 아이디 리셋
      }
    })
    .catch(() => {
      idCheckMessage.value = "아이디 중복 확인 실패";
      idCheckMessageClass.value = "text-danger";
    });
};

const pCheckMessage = ref("");
const pCheckMessageClass = ref("");

// 프로필명 중복확인
const profileNameCheck = (profileName) => {
  if (!profileName) {
    pCheckMessage.value = "프로필명을 입력하세요.";
    pCheckMessageClass.value = "text-danger";
    return;
  }

  axios
    .get(`http://localhost:8080/tofit/users/name-check`, {
      params: { profileName: profileName },
    })
    .then((response) => {
      if (response.data === false) {
        pCheckMessage.value = "사용 가능한 프로필명입니다.";
        pCheckMessageClass.value = "text-primary";
      } else {
        pCheckMessage.value = "이미 사용 중인 프로필명입니다.";
        pCheckMessageClass.value = "text-danger";
        user.value.profileName = ""; // 프로필명 리셋
      }
    })
    .catch(() => {
      pCheckMessage.value = "프로필명 중복 확인 실패";
      pCheckMessageClass.value = "text-danger";
    });
};

const imageUrl = ref(null);

// 프로필 이미지 파일 처리
const onFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    user.value.profileImage = file;
    imageUrl.value = URL.createObjectURL(file);
  }
};

const triggerFileInput = () => {
  const fileInput = document.querySelector("#profileImg");
  fileInput.click();
};

const clearImage = () => {
  user.value.profileImage = null;
  imageUrl.value = null;
};

const isFormValid = () => {
  return (
    user.value.id &&
    user.value.password &&
    user.value.mail &&
    user.value.gender &&
    user.value.birth &&
    user.value.profileName
  );
};

const signUp = function () {
  if (isFormValid()) {
    userStore.userSignup(user.value);
  } else {
    alert("모든 필드를 올바르게 작성해주세요.");
    rou;
  }
};
</script>

<style scoped>
.text-primary {
  color: blue;
}

.text-danger {
  color: red;
}

/* 전체 레이아웃 */
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
}

.form-wrapper {
  width: 400px;
  text-align: left;
}

.form-title {
  text-align: center;
  margin-bottom: 20px;
  font-weight: bold;
}

/* form-group */
.form-group {
  margin-bottom: 30px; /* 간격을 늘려줌 */
}

/* label 스타일 */
label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
}

/* input 크기 통일 */
.form-control {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 14px;
}

/* 짧은 input */
.short-input {
  width: 70%;
}

/* input-group */
.input-group {
  display: flex;
  gap: 10px;
}

/* 프로필 이미지 */
.profile-section {
  margin-bottom: 30px; /* 프로필 이미지 아래 간격 늘리기 */
  text-align: center;
}

.profile-img img {
  width: 150px;
  height: 150px;
  object-fit: cover;
  border-radius: 50%;
  margin-top: 20px;
}

.button-group {
  display: flex;
  gap: 10px;
  justify-content: center;
  margin-top: 40px;
}

/* 성별 선택 */
.gender-group {
  display: flex;
  flex-direction: column;
}

.gender-options {
  display: flex;
  gap: 20px;
}

.gender-label {
  display: flex;
  align-items: center;
  gap: 5px;
}

/* 중복 확인 메시지 */
p {
  margin-top: 10px;
  font-size: 12px;
}

/* 회원가입 버튼 */
.sign-up-btn {
  width: 100%;
  padding: 10px 0;
  background-color: #ff848f;
  color: white;
  border: none;
  border-radius: 5px;
  margin-top: 20px;
  font-size: 14px;
  font-weight: bold;
}

.sign-up-btn:hover {
  background-color: #e86b7a;
}

/* 수정하기, 탈퇴하기 버튼 공통 스타일 */
.profile-btn {
  width: 100%;
  padding: 10px 0;
  font-size: 14px;
  font-weight: bold;
  border-radius: 5px;
}

.profile-btn:hover {
  opacity: 0.8;
}

/* 등록하기 버튼 */
.btn-primary.profile-btn {
  background-color: #ef5769; /* 메인 색상에 맞춘 부드러운 톤 */
  color: white;
  border: none;
}

.btn-danger.profile-btn:hover {
  background-color: #e86b7a; /* 메인 색상 */
}

/* 삭제제하기 버튼 */
.btn-danger.profile-btn {
  background-color: #f29b97; /* 메인 색상에 맞춘 밝은 톤 */
  color: white;
  border: none;
}

.btn-danger.profile-btn:hover {
  background-color: #e86b7a; /* 메인 색상 */
}

.guide-line-divider {
  border: 0;
  border-top: 1px solid #939191;
  margin-bottom: 30px;
}
</style>
