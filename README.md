## 오이시 테이블

### _Goal_

> 고객과 식당 모두에게 편의성을 제공하여 효율적인 식당 운영과 고객의 만족도를 높이기 위한 프로젝트
---
### _Member_

| **이름**   | **역할**   | **담당 업무**                                                     |
|------------|------------|------------------------------------------------------------------|
| **홍승근** | 팀장       | 인증/인가, 예약 동시성 제어, 개발 환경 구축                      |
| **이우진** | 부팀장     | 게시글/댓글, CI/CD, AWS 인프라 설계 및 구축, 소셜 로그인, 예약 알림, 키워드 검색(ElasticSearch) |
| **김현준** | 팀원       | 팔로잉, 근처 가게 조회                                            |
| **이수호** | 팀원       | 예약, 쿠폰                                                       |
| **최우탁** | 팀원       | 북마크, 가게 웨이팅                                              |

---
### _Skills_
[![Team_Skills](https://skillicons.dev/icons?i=github,githubactions,java,spring,gradle)](https://skillicons.dev)

[![Team_Skills](https://skillicons.dev/icons?i=mysql,redis,rabbitmq,elasticsearch,docker,aws)](https://skillicons.dev)

---
### _Architecture_
![Architecture](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FPUs71%2FbtsMLQwSuMX%2F0PlnTzIC8sWfcwnXxakPw0%2Fimg.png)

---
### _ERD_
![erd](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbbWFVX%2FbtsMNgOzWo5%2FLyuk5qnL3XGK9wTDMKW6Yk%2Fimg.png)

---
### _Wire Frame_
> https://www.figma.com/design/IdgwOglfS5qUzXs19sPDgr/Team11?node-id=0-1&p=f&t=EWhJvXmYadw08A43-0

---
### API Specification

> https://docs.google.com/spreadsheets/d/1eOc9F-IZVpA-UlT_tm1u7qOb17IsiICzk5K5ZEFF7gQ/edit?gid=0#gid=0

---
# 주요 기능

## 1. 가게 필터 검색
- 사용자 위치 기반 주변 가게 검색
- 검색어 기반 가게 이름/메뉴 검색
- 주소로 가게 검색
- 가격 범위 기반 가게 검색
- 좌석 타입 기준 가게 검색
- 정렬 옵션:
  - 인기 순
  - 가까운 순

## 2. 예약 서비스
- 사용자가 예약하고자 하는 날짜와 시간, 인원 수, 좌석 타입을 선택하여 예약을 진행

## 3. 웨이팅 서비스
### 고객
- 웨이팅 등록
- 웨이팅 취소
- 현재 가게 웨이팅 대기 팀 수 확인
- 현재 웨이팅 대기열 순위 조회

### 사장
- 웨이팅 상태 수정
- 웨이팅 대기열 내 유저 삭제
- 웨이팅 대기열 조회 (페이지네이션)

## 4. 유저 커뮤니티
- 지역마다 유저 간 정보를 공유할 수 있는 게시글 / 댓글 커뮤니티

## 5. 쿠폰 관리
- 가게별 일반 쿠폰,이벤트 쿠폰 생성 및 관리
- 유저별 쿠폰 다운로드 및 관리

## 6. 북마크 / 컬렉션 관리
- 유저가 특정 가게들을 북마크로 등록하여 관리하는 기능
- 여러 북마크들을 하나의 컬렉션으로 묶어서 관리할 수 있는 기능

## 7. 알림 서비스
- 예약 및 웨이팅 성공 알림 발송
- 예약 날짜 임박 알림 발송
