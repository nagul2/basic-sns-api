# 인스타드인

**sns** api 서비스로 인스타그램과 링크드인의 일부 기능을 참고하여 SNS 서버를 구현하기 위해 최소한의 기초적인 API 제공하는 서버입니다.

<br><br>

## 🔧 **기술 스택**

- **프레임워크:** Spring 6.2.5, Spring Boot 3.4.4
- **ORM:** JPA hibernate:7.0.3, Querydsl 5.1.0
- **언어:** Java 17
- **데이터베이스:** MySQL


<br><br>

## 🛠️ **주요 기능**
### API
- 뉴스피드 CRUD
- 뉴스피드 댓글 CRUD
- 회원 CRUD
- 인증(세션, 필터를 활용한 로그인, 로그아웃)
- 친구 관리 CRUD
- 회원 팔로우 CRUD
- 좋아요 CRUD

### COMMON
- 실행 시간 LOG AOP
- Spring data jPA Auditing: 생성일, 수정일, 생성인, 수정인 자동 기록
- 예외 처리 핸들링
- 공통 응답 객체를 생성하여 응답 통일성 강화
- @EventListener()를 활용한 더미데이터 생성
