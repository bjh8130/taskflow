# TaskFlow 백엔드 아웃소싱 프로젝트
 본 프로젝트는 기업용 태스크 관리 시스템 TaskFlow의 백엔드 서버를 구축하는 것을 목표로 합니다. 클라이언트는 이미 프론트엔드 개발을 완료한 상태이며, REST API 기반의 안정적이고 확장 가능한 백엔드를 필요로 하고 있습니다. 
 ## 기술스택
 * Language: Java 17
 * Framework: Spring Boot 3.5
 * Database: MySQL
 * Security / Auth: Spring Security, JWT
 * Logging: AOP
 * Runtime: Docker
 * IDE: IntelliJ IDEA
 * API Test: Postman
 * Version Control: Git, GitHub
## 주요 기능
### 👤 사용자 관리 (User)
* 회원가입 및 로그인
* 비밀번호 확인
* 사용자 정보 조회
* 사용자 정보 수정
* 회원 탈퇴
* 사용자 목록 조회
* 추가 가능한 사용자 조회
### 🗂️ 작업 관리 (Task)
* 작업 목록 조회
  * 페이징 지원
  * 상태/조건 기반 필터링
* 작업 상세 조회
* 작업 생성/수정/삭제
* 작업 상태 변경
### 👥 팀 관리 (Team)
* 팀 목록/상세 조회
* 팀 생성/수정/삭제
* 팀 멤버 조회
* 팀 멤버 추가/제거
### 💬 댓글 관리 (Comment)
* 작업 댓글 목록 조회(페이징)
* 댓글 생성/수정/삭제
### 📊 대시보드 & 통계
* 대시보드 통계 조회
* 내 작업 요약
* 주간 작업 추세 분석
### 📝 활동 로그 (Activity Log)
* 전체 활동 로그 조회(조건별 필터링 지원)
* 내 활동 로그 조회
### 🔍 통합 검색
* 사용자 / 작업 / 팀 등 주요 데이터 통합 검색
## ERD
<img width="1279" height="860" alt="image" src="https://github.com/user-attachments/assets/78994767-3d36-49ff-b6cc-de98bbd40883" />

## API 명세서
[API명세서 보러가기](https://teamsparta.notion.site/TaskFlow-API-2c32dc3ef51481139566e0201d71fe44)

## 5조 - 파이브가이즈 🍔
한지영,김재혁,백재현,성주연,이다희
