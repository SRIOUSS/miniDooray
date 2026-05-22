# miniDooray-TaskAPI

miniDooray 프로젝트의 태스크(Task) 관리 REST API 서비스입니다.  
프로젝트·태스크·댓글·마일스톤·멤버·태그를 통합 관리하며, 회원 정보는 Account API를 내부 호출하여 처리합니다.

## 기술 스택

| 항목 | 내용 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 4.0.6 |
| ORM | Spring Data JPA (Hibernate) |
| DB (개발) | H2 (In-Memory) |
| Build | Maven |

## 프로젝트 구조

```
src/main/java/com/nhnacademy/minidooraytask/
├── MiniDoorayTaskApiApplication.java
├── MileStone/                # 마일스톤 도메인
│   ├── domain/               # MileStone, MileStoneStatus, DTO
│   ├── exception/
│   ├── repository/
│   └── service/              # MileStoneService, MileStoneFacade
├── client/                   # Account API RestClient
│   └── AccountApiClient.java
├── comment/                  # 댓글 도메인
│   ├── domain/
│   ├── exception/
│   ├── repository/
│   └── service/              # CommentService, CommentFacade
├── config/                   # 설정 (RestClient, ApiProperties, ErrorResponseDto)
├── controller/               # REST 컨트롤러 6개
├── handler/                  # 전역 예외 핸들러
├── member/                   # 프로젝트 멤버 도메인
│   ├── domain/               # ProjectMember, MembersAuth, DTO
│   ├── exception/
│   ├── repository/
│   └── service/              # ProjectMemberService, ProjectMemberFacade
├── project/                  # 프로젝트 도메인
│   ├── domain/               # Project, ProjectStatus, DTO
│   ├── exception/
│   ├── repository/
│   └── service/              # ProjectService, ProjectFacade
├── tag/                      # 태그 도메인
│   ├── domain/               # Tag, TaskTag, DTO
│   ├── exception/
│   ├── repository/
│   └── service/              # TagService
└── task/                     # 태스크 도메인
    ├── domain/               # Task, DTO
    ├── exception/
    ├── repository/
    └── service/              # TaskService, TaskFacade
```

## 실행 방법

```bash
./mvnw spring-boot:run
```

- 서버 포트: `8082`
- H2 콘솔: `http://localhost:8082/h2-console`
  - JDBC URL: `jdbc:h2:mem:taskdb`
  - Username: `sa`
  - Password: (빈 값)

### 외부 의존 서비스

| 서비스 | URL |
|--------|-----|
| Account API | `http://localhost:8081/account-api/v1/accounts` |

## 공통 요청 헤더

모든 API는 `X-Account-Id` 헤더를 통해 요청자를 식별합니다 (Gateway에서 주입).

| 헤더 | 타입 | 설명 |
|------|------|------|
| `X-Account-Id` | Long | 요청자의 계정 고유 번호 |

## API 명세

전체 API 명세는 [API_SPEC.md](./API_SPEC.md)를 참고하세요.

### 엔드포인트 요약

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/task-api/projects` | 내 프로젝트 목록 조회 |
| POST | `/task-api/projects` | 프로젝트 생성 |
| PUT | `/task-api/projects/{projectId}` | 프로젝트 수정 |
| DELETE | `/task-api/projects/{projectId}` | 프로젝트 삭제 |
| GET | `/task-api/projects/{projectId}/tasks` | 태스크 목록 조회 |
| GET | `/task-api/projects/{projectId}/tasks/{taskId}` | 태스크 상세 조회 |
| POST | `/task-api/projects/{projectId}/tasks` | 태스크 생성 |
| PUT | `/task-api/projects/{projectId}/tasks/{taskId}` | 태스크 수정 |
| DELETE | `/task-api/projects/{projectId}/tasks/{taskId}` | 태스크 삭제 |
| POST | `/task-api/tasks/{taskId}/comments` | 댓글 생성 |
| PUT | `/task-api/tasks/{taskId}/comments/{commentId}` | 댓글 수정 |
| DELETE | `/task-api/tasks/{taskId}/comments/{commentId}` | 댓글 삭제 |
| POST | `/task-api/tasks/{taskId}/milestones` | 마일스톤 생성 |
| PUT | `/task-api/tasks/{taskId}/milestones` | 마일스톤 수정 |
| DELETE | `/task-api/tasks/{taskId}/milestones` | 마일스톤 삭제 |
| GET | `/task-api/projects/{projectId}/members` | 프로젝트 멤버 목록 조회 |
| POST | `/task-api/projects/{projectId}/members` | 멤버 추가 |
| PUT | `/task-api/projects/{projectId}/members/{memberId}` | 멤버 권한 변경 |
| DELETE | `/task-api/projects/{projectId}/members/{memberId}` | 멤버 삭제 |
| GET | `/task-api/mypage/tasks` | 내 태스크 목록 조회 |
| GET | `/task-api/mypage/comments` | 내 댓글 목록 조회 |

## 도메인 모델 요약

### ProjectStatus

| 값 | 설명 |
|----|------|
| `ACTIVE` | 활성 |
| `DORMANT` | 휴면 |
| `TERMINATED` | 종료 |

### MileStoneStatus

| 값 | 설명 |
|----|------|
| `PLANNED` | 예정 |
| `IN_PROGRESS` | 진행 중 |
| `COMPLETED` | 완료 |
| `CANCELLED` | 취소 |

### MembersAuth

| 값 | 설명 |
|----|------|
| `ADMIN` | 관리자 (프로젝트 생성자) |
| `MEMBER` | 일반 멤버 |

## 에러 응답 형식

```json
{
  "message": "에러 메시지",
  "httpStatusCode": 404,
  "errorTime": "2026-05-22T10:00:00"
}
```

## 에러 처리

| 예외 상황 | Status |
|----------|--------|
| 리소스 없음 (태스크·프로젝트·댓글 등) | 404 Not Found |
| 권한 없음 (작성자 불일치, 관리자 아님) | 403 Forbidden |
| 중복 리소스 (멤버·태그·마일스톤) | 409 Conflict |
| 잘못된 입력값 | 400 Bad Request |
| 서버 내부 오류 | 500 Internal Server Error |