# 🍽️ Byte & Bite

**Byte & Bite**는 사용자가 직접 방문한 맛집 리뷰를 공유하고,
맛집 운영자가 자신의 식당 소식과 이벤트를 등록할 수 있는 커뮤니티 서비스입니다.

일반 사용자와 맛집 운영자를 구분하여,
리뷰 작성과 식당 홍보 기능을 제공하는 것을 목표로 개발했습니다.

---

### 프로젝트 개요

- 개발 기간 : 2026.07.24 ~ 2026.08.06
- 개발 인원 : 5명

---

# 📌 주요 기능

## 👤 회원 관리
- 회원가입 및 로그인/로그아웃
- 일반 사용자 / 맛집 운영자 회원 유형 구분
- 맛집 운영자 회원가입 시 식당 정보 등록
- 회원 탈퇴(Soft Delete)

---

## 🍽 맛집 리뷰
- 맛집 리뷰 작성 / 상세 조회 / 수정 / 삭제(CRUD)
- 리뷰 이미지 업로드
- 최신순 / 조회순 정렬
- 카테고리별 조회
- 식당명 검색
- 리뷰 목록 페이지네이션
- 상세 조회 시 조회수 증가
- 세션 기반 조회수 중복 방지

---

## 📢 맛집 소식
- 맛집 운영자 전용 소식 작성
- 등록 식당 정보와 소식 연동
- 소식 작성 / 상세 조회 / 수정 / 삭제(CRUD)
- 소식 이미지 업로드
- 최신순 / 조회순 정렬
- 카테고리별 조회
- 식당명 검색
- 소식 목록 페이지네이션
- 상세 조회 시 조회수 증가
- 세션 기반 조회수 중복 방지

---

## 💬 댓글
- 리뷰 상세 페이지 댓글 작성
- 댓글 수정 및 삭제
- 댓글 목록 페이지네이션
- 작성자 권한 기반 수정 및 삭제

---

## 👨‍💻 마이페이지

### 일반 사용자
- 회원 정보 조회
- 닉네임 변경
- 작성 리뷰 조회 및 관리

### 맛집 운영자
- 회원 정보 조회
- 등록 식당 정보 조회 및 수정
- 작성 맛집 소식 조회 및 관리

<br>

# 🛠 기술 스택

## Backend
- Java
- Spring Boot
- Spring MVC
- Spring JDBC (JdbcTemplate)
- Thymeleaf

## Database
- MySQL

## Frontend
- HTML
- CSS
- JavaScript

## Collaboration
- Git / GitHub
- GitHub Projects
- Pull Request

<br>

# 🏗 Architecture

```text
Browser Client
      |
      | HTTP Request / Response
      ▼
Spring Boot Application
      |
      ├── Controller
      │     └── HTTP 요청 처리
      │
      ├── Service
      │     └── 비즈니스 로직 처리
      │
      └── Repository
            └── Database 접근
      |
      ▼
MySQL Database
```

- Spring MVC 기반 3-Tier Architecture 적용
- Thymeleaf 기반 Server Side Rendering(SSR) 방식 사용
- Spring JDBC(JdbcTemplate)를 활용한 데이터 접근

<br>

# 📂 Project Structure

```text
src
└── main
    ├── java
    │   └── com.bytebite
    │       ├── controller
    │       ├── service
    │       ├── repository
    │       ├── dto
    │       └── config
    │
    └── resources
        ├── templates
        ├── static
        └── application.properties
```

<br>

# 👥 Team

| 이름 | 담당 |
| --- | --- |
| 이승언 | 회원 관리, 인증, 프로젝트 관리 |
| 김혜란 | 마이페이지 |
| 소지현 | 댓글 |
| 김남규 | 맛집 소식 |
| 윤승영 | 맛집 리뷰 |

<br>

# 📝 Documentation

상세 문서는 아래에서 확인 가능합니다.

### 📌 기획
- [프로젝트 기획서](./docs/01_planning/01_proposal.md)
- [요구사항 기능 명세서](./docs/01_planning/02_prd.md)

### 🏗 설계
- [시스템 아키텍처](./docs/02_design/01_architecture.md)
- [데이터베이스 ERD](./docs/02_design/02_erd.md)
- [화면 설계서](./docs/02_design/03_ui_wireframe.md)

### 🐛 트러블슈팅
- [트러블슈팅 기록](./docs/03_reports/)
