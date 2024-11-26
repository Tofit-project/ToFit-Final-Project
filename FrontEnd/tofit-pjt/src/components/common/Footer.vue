<template>
  <div class="footer">
    <div class="footer-content">
      <div class="footer-logo">
        <h2>tofit</h2>
        <p>운동 초보자를 위한 전문가 운동 영상 커뮤니티</p>
      </div>
      <div class="footer-links">
        <ul>
          <li><a href="/about">About</a></li>
          <li><a href="/contact">Contact</a></li>
          <li><a href="/privacy-policy">Privacy</a></li>
        </ul>
      </div>
      <div class="footer-social">
        <ul>
          <li>
            <a href="https://facebook.com/tofit" target="_blank">Facebook</a>
          </li>
          <li>
            <a href="https://twitter.com/tofit" target="_blank">Twitter</a>
          </li>
          <li>
            <a href="https://instagram.com/tofit" target="_blank">Instagram</a>
          </li>
        </ul>
      </div>
    </div>
    <div class="footer-bottom">
      <p>&copy; 2024 tofit.com | All rights reserved.</p>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
/* Footer 전체 스타일 */
.footer {
  background-color: #333333bf; /* 다크한 배경 */
  color: #fff; /* 밝은 텍스트 */
  padding: 10px 0; /* 간격 줄이기 */
  font-family: "Arial", sans-serif;
}

/* Footer 내용 컨테이너 */
.footer-content {
  display: flex;
  flex-wrap: wrap; /* 반응형으로 줄바꿈 가능 */
  justify-content: space-between; /* 양끝 정렬 */
  align-items: flex-start;
  max-width: 1000px; /* 중앙 정렬을 위한 고정 폭 */
  margin: 0 auto;
  padding: 10px 20px; /* 내부 여백 */
  text-align: center;
}

/* 로고 섹션 */
.footer-logo h2 {
  font-size: 24px; /* 더 작고 깔끔한 폰트 */
  margin: 0;
  color: #f26465; /* 브랜드 컬러 */
}

.footer-logo p {
  font-style: italic;
  font-size: 14px;
  color: #aaa;
  margin: 5px 0 0;
}

/* 링크 섹션 */
.footer-links ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-links li {
  margin-bottom: 5px; /* 간격 줄이기 */
}

.footer-links a {
  text-decoration: none;
  color: #fff;
  font-size: 14px;
  transition: color 0.3s ease;
}

.footer-links a:hover {
  color: #ff7676; /* 브랜드 컬러 강조 */
}

/* 소셜 섹션 */
.footer-social ul {
  list-style: none;
  display: flex;
  gap: 10px; /* 버튼 간격 */
  padding: 0;
  margin: 0;
}

.footer-social a {
  text-decoration: none;
  color: #fff;
  font-size: 14px;
  transition: color 0.3s ease;
}

.footer-social a:hover {
  color: #ff7676;
}

/* 하단 섹션 */
.footer-bottom {
  text-align: center;
  margin-top: 15px;
  font-size: 12px;
  color: #aaa;
}

.footer-bottom p {
  margin: 0;
}
</style>
