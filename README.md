# TOFIT 💪
# :clipboard: 목차

- :books: <a href="#outline">개요</a>
- :wrench: <a href="#tech">기술 스택</a>
- :scroll: <a href="#erd">ERD(Entity-Relation Diagram)</a>
- :family: <a href="#team">팀원 역할 소개</a>
- :bookmark_tabs: <a href="#function">기능 구현 시연 영상</a>
  - <a href="#fun1">1.&nbsp;로그인</a>
     - 1-1. 회원가입
     - 1-2. 로그인
  - <a href="#fun2">2.&nbsp;홈 화면</a>
     - 2-1. 운동 영상 추천 조회
     - 2-2. 전체 영상 검색
  - <a href="#fun3">3.&nbsp;강사 조회</a>
     - 3-1. 강사 프로필 조회
     - 3-2. 강사명 검색
  - <a href="#fun4">4.&nbsp;운동 영상 상세 조회</a>
     - 4-1. 찜 관리
     - 4-2. 댓글 관리
  - <a href="#fun5">5.&nbsp;운동 캘린더</a>
     - 5-1. 월별 운동 리포트 생성
     - 5-2. 운동 기록 관리
  - <a href="#fun6">6.&nbsp;마이 피드 관리</a>
  - <a href="#fun7">7.&nbsp;커뮤니티</a>
     - 7-1. 전체 유저 피드 조회
     - 7-2. 피드 댓글 관리
     - 7-3. 리뷰 추천 
  - <a href="#fun8">8.&nbsp;마이페이지</a>
     - 8-1. 정보 관리


# :books: <a name="outline">개요</a>
<br/>
<img src="https://github.com/user-attachments/assets/bd4e8cfe-1e02-4c16-ac35-9dcdf5b5753a"/>

> - **프로젝트** : 홈트 입문자를 위한 전문가 운동 영상 커뮤니티
>
> - **기획 및 제작**
>
>   `ToFit`은 운동 초보자들이 <strong>자신의 목표</strong>에 적합한 운동 영상을 손쉽게 선택할 수 있도록 하여 운동에 대한 접근성을 높이고, 커뮤니티와 캘린더 기능을 활성화하여 <strong>지속적인 동기부여</strong>와 <strong>운동에 대한 열정</strong>을 유지할 수 있도록 지원하는 웹 서비스입니다.
>    ```
>    To fit 건강하게 되어가다
>    Together Fitness 함께 운동하다
>   ```
>    
> - **분류** : 팀 프로젝트
>
> - **제작 기간** : 2024.11.11 ~ 2024.11.26
>
> - **프로젝트 주제** : 운동 초보자들이 꾸준히 운동할 수 있는 웹 사이트 제작

<br/>

# :wrench: <a name="tech">기술 스택</a>
<h4>데이터베이스</h4>
<div align="left">
   <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
   <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white" />
</div> 
<h4>백엔드</h4>
<div align="left">
    <img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=Java&logoColor=white"/>
     <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
    <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" />
    <img src="https://img.shields.io/badge/MyBatis-232F3E?style=for-the-badge&logo=mybatis&logoColor=white" />
</div>
</div> 
<h4>프론트엔드</h4>
<div align="left">
  <img src="https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vuedotjs&logoColor=white">
   <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=HTML5&logoColor=white"/>
   <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=CSS3&logoColor=white"/>
     <img src="https://img.shields.io/badge/JAVASCRIPT-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white"/>
   <img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white" />
     <img src="https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white">
</div>
<h4>API / Library</h4>
<div align="left">
   <img src="https://img.shields.io/badge/openai-412991?style=for-the-badge&logo=openai&logoColor=white">
  <img src="https://img.shields.io/badge/fullcalendar-4285F4?style=for-the-badge&logo=fullcalendar&logoColor=white">
</div>
<h4>클라우드 스토리지</h4>
<div align="left">
   <img src="https://img.shields.io/badge/AWS S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
</div>
<h4>협업도구</h4>
<div align="left">
   <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white" />
   <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white" />
</div><br/>

# :scroll: <a name="erd">ERD</a>

## 전체 ERD
<img src="https://github.com/user-attachments/assets/6a20064f-994a-46ea-9194-7c9f454459c3" width="100%"/>
<br/>

# :family: <a name="team">팀원 역할 소개</a>
<div><br/>
   
<h3>● 김윤지</h3>

##### &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Back-End : 회원가입(이메일 인증), 영상 조회(영상 추천, 전체 조회, 상세 조회), 찜(등록, 삭제), 영상 댓글 관리(등록, 조회, 수정, 삭제), 강사 전체 조회, 피드 댓글 관리(등록, 조회, 수정, 삭제), 운동 기록(등록, 조회, 삭제), ChatGPT API
##### &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Front-End : 홈 화면, 영상 상세 페이지, 전문가 페이지, 운동 기록 페이지

<h3>● 이성희</h3>

##### &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Back-End : 회원가입(아이디 중복 확인, 프로필명 중복 확인), 로그인, 회원 정보(조회, 수정, 탈퇴), 피드 관리(등록, 전체 조회, 상세 조회, 수정, 삭제), 팔로우(등록, 삭제)
##### &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Front-End : 로그인 페이지, 마이페이지, 커뮤니티 페이지, 피드 상세 페이지

</div><br/>

# :bookmark_tabs: <a name="function">기능 구현 시연 영상</a>
<br/>

# 1. <a name="fun1">로그인
## &nbsp;&nbsp;1-1. 회원가입 

<img src="https://github.com/user-attachments/assets/2ba1da00-e53e-46b8-a2cc-bf4b8b7adcb4" width="100%"/><br/><br/>

<br/>
  
## &nbsp;&nbsp;1-2. 로그인

<img src="https://github.com/user-attachments/assets/6a80dfd2-f61c-434c-a232-9d38608f536f" width="100%"/><br/><br/>

<br/>

# 2. <a name="fun2">홈 화면
## &nbsp;&nbsp;2-1. 운동 영상 추천 조회

<img src="https://github.com/user-attachments/assets/ade8d6e8-a5d5-4c9e-aca0-540c12a4aa0c" width="100%"/><br/><br/>

<br/>

## &nbsp;&nbsp;2-2. 전체 영상 조회

<img src="https://github.com/user-attachments/assets/3d10d564-1132-4791-b899-07d9a5f0ebb2" width="100%"/><br/><br/>

<br/>

# 3. <a name="fun3">강사 조회
## &nbsp;&nbsp;3-1. 강사 프로필 조회

<img src="https://github.com/user-attachments/assets/1a8d73c4-4e38-4bb9-86a5-161dcbbd431b" width="100%"/><br/><br/>

<br/>

## &nbsp;&nbsp;3-2. 강사명 검색

<img src="https://github.com/user-attachments/assets/07525e87-8e16-4278-aafb-3575c9dd6beb" width="100%"/><br/><br/>

<br/>

# 4. <a name="fun4">운동 영상 상세 조회
## &nbsp;&nbsp;4-1. 찜 관리

<img src="https://github.com/LeeSeongHui/Final-Project/blob/main/src/main/resources/static/portfolio/4-1. 일정관리.gif?raw=true" width="100%"/><br/><br/>

<br/>

## &nbsp;&nbsp;4-2. 댓글 관리

<img src="https://github.com/LeeSeongHui/Final-Project/blob/main/src/main/resources/static/portfolio/4-2. 신청관리.gif?raw=true" width="100%"/><br/><br/>

<br/>

# 5. <a name="fun5">운동 캘린더
## &nbsp;&nbsp;5-1. 월별 운동 리포트 생성

<img src="https://github.com/LeeSeongHui/Final-Project/blob/main/src/main/resources/static/portfolio/5-1. 클래스검색(지역, 카테고리, 날짜, 난이도, 가격).gif?raw=true" width="100%"/><br/><br/>

<br/>
  
## &nbsp;&nbsp;5-2. 운동 기록 관리

<img src="https://github.com/LeeSeongHui/Final-Project/blob/main/src/main/resources/static/portfolio/5-2. 클래스검색(지도).gif?raw=true" width="100%"/><br/><br/>

<br/>

# 6. <a name="fun6">마이 피드 관리
<img src="https://github.com/LeeSeongHui/Final-Project/blob/main/src/main/resources/static/portfolio/5-2. 클래스검색(지도).gif?raw=true" width="100%"/><br/><br/>

<br/>

# 7. <a name="fun7">커뮤니티
## &nbsp;&nbsp;7-1. 전체 유저 피드 조회

<img src="https://github.com/LeeSeongHui/Final-Project/blob/main/src/main/resources/static/portfolio/7-1. 신청 내역 조회(상세보기).gif?raw=true" width="100%"/><br/><br/>

<br/>

## &nbsp;&nbsp;7-2. 피드 댓글 관리

<img src="https://github.com/LeeSeongHui/Final-Project/blob/main/src/main/resources/static/portfolio/7-2. 리뷰 작성.gif?raw=true" width="100%"/><br/><br/>

<br/>

# 8. <a name="fun8">마이페이지
## &nbsp;&nbsp;8-1. 정보 관리

<img src="https://github.com/LeeSeongHui/Final-Project/blob/main/src/main/resources/static/portfolio/8. 커뮤니티(게시글 및 댓글 작성).gif?raw=true" width="100%"/><br/><br/>

<br/>
