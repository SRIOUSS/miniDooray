# miniDooray-FE

Spring Boot 기반의 MVC 웹 애플리케이션으로, API Gateway를 통해 Account API 및 Task API 백엔드와 통신합니다.

---

## 시스템 아키텍처

```
[Browser]
    │  HTTP (쿠키: SESSION, XSRF-TOKEN)
    ▼
[miniDooray-FE]  :8080
    │  JSON REST (X-Account-Id 헤더)
    ▼
[API Gateway]    :8000
    ├── Account API (계정 관리)
    └── Task API    (프로젝트/태스크 관리)
```

세션은 Redis에 저장되며, 모든 인증 상태는 서버 측에서 관리됩니다.

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 4.0.6 |
| Web | Spring MVC, Thymeleaf |
| Security | Spring Security 6 |
| Session | Spring Session Data Redis |
| HTTP Client | Spring `RestClient` |
| Build | Maven |
| Test | JUnit 5, JaCoCo |

---

## 프로젝트 구조

```
src/main/java/com/nhnacademy/minidoorayfe/
├── MiniDoorayFeApplication.java      # 진입점
│
├── api/                              # 백엔드 API 클라이언트
│   ├── AccountApiClient.java         # Account API 통신
│   └── TaskApiClient.java            # Task API 통신
│
├── auth/                             # Spring Security 커스텀 컴포넌트
│   ├── CustomAuthenticationProvider.java
│   ├── CustomAuthenticationSuccessHandler.java
│   ├── CustomAuthenticationFailHandler.java
│   ├── LoginFailureCounter.java      # Redis 기반 로그인 실패 카운터
│   └── BlackList.java                # Redis 기반 IP 블랙리스트
│
├── config/                           # 설정 클래스
│   ├── RestClientConfig.java         # RestClient 빈 + 에러 핸들러
│   ├── SecurityConfig.java           # Spring Security 설정
│   ├── WebConfig.java                # ArgumentResolver 등록
│   ├── RedisConfig.java              # Redis + Spring Session 설정
│   ├── PasswordEncodeConfig.java     # BCryptPasswordEncoder 빈
│   └── (properties/)
│       └── ApiProperties.java        # api.gateway-url 바인딩
│
├── controller/
│   ├── auth/
│   │   ├── AuthController.java       # GET /login
│   │   ├── SignUpController.java     # GET/POST /signup
│   │   └── CheckUserIdController.java # GET /check-userId
│   └── task/
│       ├── ProjectController.java    # /projects/**
│       ├── TaskController.java       # /projects/{id}/tasks/**
│       ├── CommentController.java    # .../comments/**
│       ├── MilestoneController.java  # .../milestones/**
│       └── MyPageController.java    # /mypage/**
│
├── dto/                              # 데이터 전송 객체
│   ├── auth/    (SignFormDto, AccountResponseDto, SessionAccountDto)
│   ├── project/ (ProjectRequestDto, ProjectViewDto, ProjectInfoDto, ProjectStatus)
│   ├── task/    (TaskRequestDto, TaskResponseDto, TaskViewDto, TaskInfoDto, TaskInfoListDto)
│   ├── member/  (MemberRequestDto, MemberInfoDto, MemberInfoListDto, MembersAuth)
│   ├── comment/ (CommentRequestDto, CommentResponseDto, CommentListDto)
│   ├── milestone/ (MilestoneRequestDto, MilestoneResponseDto, MilestoneStatus)
│   └── tag/     (TagResponseDto)
│
├── exception/
│   └── ApiServerException.java       # 5xx 에러 전용 예외
│
├── filter/
│   ├── SessionAuthFilter.java        # 세션 기반 인증 필터
│   └── IpBlackListFilter.java        # IP 차단 필터
│
├── handler/
│   └── GlobalExceptionHandler.java   # @ControllerAdvice 전역 예외 처리
│
└── resolver/
    ├── SessionIdentity.java          # 컨트롤러 파라미터 주입 어노테이션
    ├── SessionArgumentResolver.java  # @SessionIdentity 처리
    └── SessionConstants.java         # 세션 키 상수 (SESSION_ACCOUNT)
```

---

## 주요 기능

### 인증 (Authentication)
- **폼 로그인**: ID/Password 기반 로그인, Account API로 계정 검증
- **비밀번호 검증**: BCryptPasswordEncoder로 해시 비교
- **계정 상태 확인**: `ACTIVE` 상태만 로그인 허용
- **로그인 실패 제한**: 3회 연속 실패 시 IP 블랙리스트 등록 (Redis TTL 1분)
- **IP 차단**: `IpBlackListFilter`에서 차단된 IP 요청을 조기 차단

### 세션 관리 (Session)
- Redis 기반 세션 저장 (TTL: 3600초)
- 로그인 성공 시 세션 고정 공격 방지를 위해 기존 세션 무효화 후 신규 발급
- `SessionAccountDto` (accountId, userId) 를 세션에 저장
- 컨트롤러에서 `@SessionIdentity` 어노테이션으로 세션 정보 주입

### 보안 (Security)
- CSRF 보호: `CookieCsrfTokenRepository` (httpOnly=false, Thymeleaf 폼 자동 삽입)
- 세션 쿠키: `HttpOnly=true`, `Secure=true`, `SameSite=Strict`
- 공개 URL: `/login`, `/signup`, `/error`, 정적 리소스 (`/css/**`, `/js/**`, `/images/**`)
- 나머지 모든 URL은 인증 필요

### 프로젝트 관리
- 프로젝트 CRUD (소프트 삭제)
- 멤버 초대/권한 변경/제거 (`MEMBER` / `ADMIN`)

### 태스크 관리
- 태스크 CRUD (소프트 삭제)
- 태그 다중 연결
- 마일스톤 연결 (태스크 당 1개, CRUD)
- 댓글 CRUD

### 마이페이지
- 내가 작성한 태스크 목록
- 내가 작성한 댓글 목록

---

## 설정 (application.yaml)

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379

  session:
    store-type: redis
    timeout: 3600          # 세션 만료: 1시간

api:
  gateway-url: http://localhost:8000   # API Gateway 주소

server:
  servlet:
    session:
      cookie:
        http-only: true
        secure: true
        same-site: strict
```

---

## 실행 방법

### 사전 요구 사항

| 항목 | 버전 |
|------|------|
| Java | 21 이상 |
| Maven | 3.8 이상 |
| Redis | 7.x (localhost:6379) |
| API Gateway | localhost:8000 실행 중 |

서버 시작 후 `http://localhost:8080/login` 으로 접속

---

## 관련 문서

- [API_DOCS.md](./submit/API_DOCS.md) — Account API / Task API 백엔드 통신 명세
- [miniDooray-team11-ERD.png](./submit/miniDooray-team11-ERD.png) - ERD
- [SonarQube.png](./submit/SonarQube.png) — SonarQube 커버리지