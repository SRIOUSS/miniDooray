# miniDooray FE — Backend REST API 통신 명세서

> FE 서버(miniDooray-FE)가 API Gateway를 통해 **Account API** 및 **Task API** 백엔드 서버와 주고받는 HTTP 통신 명세입니다.  
> 모든 요청은 `api.gateway-url`(기본값: `http://localhost:8000`)을 Base URL로 사용합니다.

---

## 공통 사항

| 항목 | 내용 |
|------|------|
| Base URL | `http://localhost:8000` (application.yaml: `api.gateway-url`) |
| Content-Type | `application/json` |
| 연결/읽기 타임아웃 | 각 5초 |
| 인증 방식 | Task API 전용 헤더 `X-Account-Id: {accountId}` |

### 공통 에러 처리

| HTTP 상태 | 처리 |
|-----------|------|
| 404 | `UsernameNotFoundException` → 404 에러 페이지 |
| 5xx | `ApiServerException` → 500 에러 페이지 |
| 그 외 | `RestClientException` → 400 에러 페이지 |

---

## 1. Account API

Base Path: `/account-api/v1/accounts`

### 1-1. 사용자 조회 (로그인용)

로그인 시 CustomAuthenticationProvider가 호출하여 계정 정보를 검증합니다.

```
GET /account-api/v1/accounts/login?userId={userId}
```

**Request**

| 위치 | 파라미터 | 타입 | 필수 | 설명 |
|------|----------|------|------|------|
| Query | `userId` | String | O | 조회할 사용자 ID |

**Response Body**

```json
{
  "accountId": 1,
  "userId": "testuser",
  "userPassword": "$2a$10$...",
  "status": "ACTIVE"
}
```

| 필드 | 타입 | 설명 |
|------|------|------|
| `accountId` | Long | 계정 고유 ID |
| `userId` | String | 사용자 ID |
| `userPassword` | String | BCrypt 해시된 비밀번호 |
| `status` | String | 계정 상태 (`ACTIVE`, `DORMANT` 등) |

**처리 흐름**

- `status != "ACTIVE"` → `DisabledException` 발생 → 로그인 실패
- 비밀번호 불일치 → `BadCredentialsException` 발생 → 로그인 실패
- 3회 실패 시 IP 블랙리스트 등록 (1분간 차단)

---

### 1-2. 회원가입

```
POST /account-api/v1/accounts/register
```

**Request Body**

```json
{
  "userId": "newuser",
  "userPassword": "password123",
  "userName": "홍길동",
  "userEmail": "hong@example.com"
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `userId` | String | O | 사용자 ID |
| `userPassword` | String | O | 비밀번호 (평문, 백엔드에서 암호화) |
| `userName` | String | O | 사용자 이름 |
| `userEmail` | String | O | 이메일 주소 |

**Response**

- 성공: `201 Created` (body 없음)

---

## 2. Task API

Base Path: `/task-api`

모든 Task API 요청에는 인증 헤더가 필요합니다.

```
X-Account-Id: {accountId}
```

`accountId`는 로그인 시 Redis 세션에 저장된 `SessionAccountDto.accountId` 값입니다.

---

### 2-1. 프로젝트 (Project)

#### 프로젝트 목록 조회

```
GET /task-api/projects
X-Account-Id: {accountId}
```

**Response Body**

```json
{
  "projectInfoDtoList": [
    {
      "id": 1,
      "title": "프로젝트명",
      "status": "ACTIVE",
      "taskStatusList": ["PLANNED", "IN_PROGRESS"]
    }
  ],
  "taskInfoDtoList": [
    {
      "id": 1,
      "title": "태스크명",
      "status": "ACTIVE"
    }
  ]
}
```

| 필드 | 타입 | 설명 |
|------|------|------|
| `projectInfoDtoList` | List | 참여 중인 프로젝트 목록 |
| `projectInfoDtoList[].id` | Long | 프로젝트 ID |
| `projectInfoDtoList[].title` | String | 프로젝트 제목 |
| `projectInfoDtoList[].status` | String | `ACTIVE` / `DORMANT` / `TERMINATED` |
| `projectInfoDtoList[].taskStatusList` | List\<String\> | 프로젝트 내 마일스톤 상태 목록 |
| `taskInfoDtoList` | List | 최근 태스크 목록 |

---

#### 프로젝트 생성

```
POST /task-api/projects
X-Account-Id: {accountId}
Content-Type: application/json
```

**Request Body**

```json
{
  "title": "새 프로젝트",
  "description": "프로젝트 설명",
  "status": "ACTIVE"
}
```

| 필드 | 타입 | 필수 | 기본값 | 설명 |
|------|------|------|--------|------|
| `title` | String | O | - | 프로젝트 제목 |
| `description` | String | X | - | 프로젝트 설명 |
| `status` | String | X | `ACTIVE` | `ACTIVE` / `DORMANT` / `TERMINATED` |

**Response**: `201 Created` (body 없음)

---

#### 프로젝트 수정

```
PUT /task-api/projects/{projectId}
X-Account-Id: {accountId}
Content-Type: application/json
```

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `projectId` | Long | 수정할 프로젝트 ID |

**Request Body**: 프로젝트 생성과 동일

**Response**: `200 OK` (body 없음)

---

#### 프로젝트 삭제 (소프트 삭제)

```
DELETE /task-api/projects/{projectId}
X-Account-Id: {accountId}
```

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `projectId` | Long | 삭제할 프로젝트 ID |

**Response**: `200 OK` (body 없음)

---

### 2-2. 멤버 (Member)

#### 멤버 목록 조회

```
GET /task-api/projects/{projectId}/members
X-Account-Id: {accountId}
```

**Response Body**

```json
{
  "memberInfoDtoList": [
    {
      "accountId": 1,
      "memberId": 1,
      "userId": "testuser",
      "auth": "ADMIN",
      "joinedAt": "2024-01-01T10:00:00"
    }
  ]
}
```

| 필드 | 타입 | 설명 |
|------|------|------|
| `accountId` | Long | 계정 ID |
| `memberId` | Long | 멤버 ID (프로젝트 내) |
| `userId` | String | 사용자 ID |
| `auth` | String | `MEMBER` / `ADMIN` |
| `joinedAt` | LocalDateTime | 프로젝트 참여 일시 |

---

#### 멤버 추가

```
POST /task-api/projects/{projectId}/members
X-Account-Id: {accountId}
Content-Type: application/json
```

**Request Body**

```json
{
  "accountId": 2,
  "userId": "newmember",
  "auth": "MEMBER"
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `accountId` | Long | O | 추가할 계정 ID |
| `userId` | String | O | 추가할 사용자 ID |
| `auth` | String | O | `MEMBER` / `ADMIN` |

**Response**: `201 Created` (body 없음)

---

#### 멤버 권한 변경

```
PUT /task-api/projects/{projectId}/members/{memberId}
X-Account-Id: {accountId}
Content-Type: application/json
```

**Path Variables**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `projectId` | Long | 프로젝트 ID |
| `memberId` | Long | 변경할 멤버 ID |

**Request Body**: 멤버 추가와 동일

**Response**: `200 OK` (body 없음)

---

#### 멤버 삭제

```
DELETE /task-api/projects/{projectId}/members/{memberId}
X-Account-Id: {accountId}
```

**Path Variables**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `projectId` | Long | 프로젝트 ID |
| `memberId` | Long | 삭제할 멤버 ID |

**Response**: `200 OK` (body 없음)

---

### 2-3. 태스크 (Task)

#### 태스크 목록 조회

```
GET /task-api/projects/{projectId}/tasks
X-Account-Id: {accountId}
```

**Response Body**

```json
{
  "taskInfoDtoList": [
    {
      "id": 1,
      "title": "태스크 제목",
      "status": "ACTIVE"
    }
  ]
}
```

---

#### 태스크 상세 조회

```
GET /task-api/projects/{projectId}/tasks/{taskId}
X-Account-Id: {accountId}
```

**Path Variables**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `projectId` | Long | 프로젝트 ID |
| `taskId` | Long | 태스크 ID |

**Response Body**

```json
{
  "taskResponseDto": {
    "taskId": 1,
    "title": "태스크 제목",
    "content": "태스크 내용",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-02T10:00:00",
    "tagResponseDtoList": [
      { "id": 1, "name": "bug" }
    ],
    "milestoneResponseDto": {
      "id": 1,
      "title": "마일스톤 제목",
      "description": "마일스톤 설명",
      "status": "IN_PROGRESS",
      "dueDate": "2024-12-31T00:00:00",
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-02T10:00:00"
    }
  },
  "taskInfoListDto": { "taskInfoDtoList": [] },
  "projectInfoDto": {
    "id": 1,
    "title": "프로젝트명",
    "status": "ACTIVE",
    "taskStatusList": []
  },
  "commentResponseDtoList": [
    {
      "id": 1,
      "accountId": 1,
      "userId": "testuser",
      "content": "댓글 내용",
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
    }
  ]
}
```

---

#### 태스크 생성

```
POST /task-api/projects/{projectId}/tasks
X-Account-Id: {accountId}
Content-Type: application/json
```

**Request Body**

```json
{
  "title": "새 태스크",
  "content": "태스크 내용",
  "tagNameList": ["bug", "feature"]
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `title` | String | O | 태스크 제목 |
| `content` | String | X | 태스크 내용 |
| `tagNameList` | List\<String\> | X | 태그 이름 목록 |

**Response**: `201 Created` (body 없음)

---

#### 태스크 수정

```
PUT /task-api/projects/{projectId}/tasks/{taskId}
X-Account-Id: {accountId}
Content-Type: application/json
```

**Path Variables**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `projectId` | Long | 프로젝트 ID |
| `taskId` | Long | 수정할 태스크 ID |

**Request Body**: 태스크 생성과 동일

**Response**: `200 OK` (body 없음)

---

#### 태스크 삭제 (소프트 삭제)

```
DELETE /task-api/projects/{projectId}/tasks/{taskId}
X-Account-Id: {accountId}
```

**Path Variables**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `projectId` | Long | 프로젝트 ID |
| `taskId` | Long | 삭제할 태스크 ID |

**Response**: `200 OK` (body 없음)

---

#### 내 태스크 목록 조회 (마이페이지)

```
GET /task-api/mypage/tasks
X-Account-Id: {accountId}
```

**Response Body**: 태스크 목록 조회와 동일 (`TaskInfoListDto`)

---

### 2-4. 댓글 (Comment)

#### 댓글 생성

```
POST /task-api/tasks/{taskId}/comments
X-Account-Id: {accountId}
Content-Type: application/json
```

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `taskId` | Long | 댓글을 작성할 태스크 ID |

**Request Body**

```json
{
  "content": "댓글 내용"
}
```

**Response**: `201 Created` (body 없음)

---

#### 댓글 수정

```
PUT /task-api/tasks/{taskId}/comments/{commentId}
X-Account-Id: {accountId}
Content-Type: application/json
```

**Path Variables**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `taskId` | Long | 태스크 ID |
| `commentId` | Long | 수정할 댓글 ID |

**Request Body**: 댓글 생성과 동일

**Response**: `200 OK` (body 없음)

---

#### 댓글 삭제

```
DELETE /task-api/tasks/{taskId}/comments/{commentId}
X-Account-Id: {accountId}
```

**Path Variables**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `taskId` | Long | 태스크 ID |
| `commentId` | Long | 삭제할 댓글 ID |

**Response**: `200 OK` (body 없음)

---

#### 내 댓글 목록 조회 (마이페이지)

```
GET /task-api/mypage/comments
X-Account-Id: {accountId}
```

**Response Body**

```json
{
  "commentResponseList": [
    {
      "id": 1,
      "accountId": 1,
      "userId": "testuser",
      "content": "댓글 내용",
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
    }
  ]
}
```

> JSON 키 이름은 `commentResponseList` (Jackson `@JsonProperty` 설정)

---

### 2-5. 마일스톤 (Milestone)

마일스톤은 태스크 1개에 최대 1개 연결됩니다. 조회는 태스크 상세 조회(`GET /task-api/projects/{projectId}/tasks/{taskId}`) 응답의 `milestoneResponseDto` 필드로 포함됩니다.

#### 마일스톤 생성

```
POST /task-api/tasks/{taskId}/milestones
X-Account-Id: {accountId}
Content-Type: application/json
```

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `taskId` | Long | 마일스톤을 연결할 태스크 ID |

**Request Body**

```json
{
  "title": "마일스톤 제목",
  "description": "마일스톤 설명",
  "status": "PLANNED",
  "dueDate": "2024-12-31T00:00"
}
```

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `title` | String | O | 마일스톤 제목 |
| `description` | String | X | 마일스톤 설명 |
| `status` | String | X | `PLANNED` / `IN_PROGRESS` / `COMPLETED` / `CANCELLED` |
| `dueDate` | String | X | 마감일시 (형식: `yyyy-MM-dd'T'HH:mm`) |

**Response**: `201 Created` (body 없음)

---

#### 마일스톤 수정

```
PUT /task-api/tasks/{taskId}/milestones
X-Account-Id: {accountId}
Content-Type: application/json
```

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `taskId` | Long | 태스크 ID |

**Request Body**: 마일스톤 생성과 동일

**Response**: `200 OK` (body 없음)

---

#### 마일스톤 삭제

```
DELETE /task-api/tasks/{taskId}/milestones
X-Account-Id: {accountId}
```

**Path Variable**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `taskId` | Long | 태스크 ID |

**Response**: `200 OK` (body 없음)

---

## 3. Enum 값 정의

### ProjectStatus

| 값 | 설명 |
|----|------|
| `ACTIVE` | 활성 프로젝트 |
| `DORMANT` | 휴면 프로젝트 |
| `TERMINATED` | 종료된 프로젝트 |

### MembersAuth

| 값 | 설명 |
|----|------|
| `MEMBER` | 일반 멤버 |
| `ADMIN` | 관리자 |

### MilestoneStatus

| 값 | 설명 |
|----|------|
| `PLANNED` | 계획됨 |
| `IN_PROGRESS` | 진행 중 |
| `COMPLETED` | 완료됨 |
| `CANCELLED` | 취소됨 |

---

## 4. API 호출 흐름 요약

```
[FE Browser]
     │
     ▼ CSRF 쿠키 + Session 쿠키
[miniDooray-FE: localhost:8080]
     │
     │  X-Account-Id 헤더 (Task API)
     │  Content-Type: application/json
     ▼
[API Gateway: localhost:8000]
     │
     ├─► /account-api/** → Account API 서버
     └─► /task-api/**    → Task API 서버
```

1. 사용자가 로그인하면 FE 서버가 `GET /account-api/v1/accounts/login` 으로 계정 검증
2. 인증 성공 시 `accountId`를 Redis 세션에 저장
3. 이후 모든 Task API 요청에 `X-Account-Id: {accountId}` 헤더를 자동 첨부