# miniDooray-TaskAPI 명세서

- **Base URL**: `http://localhost:8082`
- **Content-Type**: `application/json`
- **공통 요청 헤더**: 모든 API에 `X-Account-Id: {accountId}` (Long) 필요

---

## 공통 응답 코드

| HTTP Status | 설명 |
|-------------|------|
| 200 OK | 조회 성공 |
| 201 Created | 리소스 생성 성공 |
| 204 No Content | 수정/삭제 성공 (응답 본문 없음) |
| 400 Bad Request | 잘못된 입력값 |
| 403 Forbidden | 권한 없음 (작성자 불일치, 관리자 아님) |
| 404 Not Found | 존재하지 않는 리소스 |
| 409 Conflict | 이미 존재하는 리소스 |
| 500 Internal Server Error | 서버 내부 오류 |

## 공통 에러 응답 본문

```json
{
  "message": "에러 메시지",
  "httpStatusCode": 404,
  "errorTime": "2026-05-22T10:00:00"
}
```

---

# 1. 프로젝트 (Project)

## 1-1. 내 프로젝트 목록 조회

```
GET /task-api/projects
X-Account-Id: {accountId}
```

### 응답 (200 OK)

```json
{
  "projectInfoDtoList": [
    {
      "id": 1,
      "title": "miniDooray",
      "status": "ACTIVE",
      "mileStoneStatusList": ["IN_PROGRESS", "PLANNED"]
    }
  ],
  "taskInfoDtoList": [
    {
      "id": 1,
      "title": "로그인 구현",
      "status": "IN_PROGRESS"
    }
  ]
}
```

| 필드 | 타입 | 설명 |
|------|------|------|
| projectInfoDtoList | List | 참여 중인 프로젝트 목록 |
| taskInfoDtoList | List | 내가 담당한 태스크 목록 |

---

## 1-2. 프로젝트 생성

```
POST /task-api/projects
X-Account-Id: {accountId}
```

### Request Body

```json
{
  "title": "miniDooray",
  "description": "프로젝트 설명",
  "status": "ACTIVE"
}
```

| 필드 | 필수 | 타입 | 설명 |
|------|------|------|------|
| title | O | String | 프로젝트 제목 |
| description | X | String | 프로젝트 설명 |
| status | X | ProjectStatus | 프로젝트 상태 (기본값: `ACTIVE`) |

### 응답 (201 Created)

응답 본문 없음  
생성 시 요청자는 자동으로 `ADMIN` 권한의 멤버로 등록됩니다.

---

## 1-3. 프로젝트 수정

```
PUT /task-api/projects/{projectId}
X-Account-Id: {accountId}
```

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| projectId | Long | 프로젝트 고유 번호 |

### Request Body

```json
{
  "title": "수정된 제목",
  "description": "수정된 설명",
  "status": "DORMANT"
}
```

### 응답 (204 No Content)

응답 본문 없음

### 에러

| 상황 | Status |
|------|--------|
| 존재하지 않는 projectId | 404 |

---

## 1-4. 프로젝트 삭제

소프트 삭제 방식으로 처리됩니다 (`isDeleted = true`).

```
DELETE /task-api/projects/{projectId}
X-Account-Id: {accountId}
```

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| projectId | Long | 프로젝트 고유 번호 |

### 응답 (204 No Content)

응답 본문 없음

### 에러

| 상황 | Status |
|------|--------|
| 존재하지 않는 projectId | 404 |
| 프로젝트 생성자가 아님 | 403 |

---

# 2. 태스크 (Task)

## 2-1. 태스크 목록 조회

```
GET /task-api/projects/{projectId}/tasks
X-Account-Id: {accountId}
```

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| projectId | Long | 프로젝트 고유 번호 |

### 응답 (200 OK)

```json
{
  "taskInfoDtoList": [
    {
      "id": 1,
      "title": "로그인 구현",
      "status": "IN_PROGRESS"
    }
  ]
}
```

### 에러

| 상황 | Status |
|------|--------|
| 프로젝트 멤버가 아님 | 404 |

---

## 2-2. 태스크 상세 조회

```
GET /task-api/projects/{projectId}/tasks/{taskId}
X-Account-Id: {accountId}
```

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| projectId | Long | 프로젝트 고유 번호 |
| taskId | Long | 태스크 고유 번호 |

### 응답 (200 OK)

```json
{
  "taskResponseDto": {
    "taskId": 1,
    "projectId": 1,
    "createMemberId": 2,
    "title": "로그인 구현",
    "content": "JWT 기반 로그인 구현",
    "createdAt": "2026-05-22T10:00:00",
    "updatedAt": "2026-05-22T10:00:00",
    "milestoneResponseDto": {
      "id": 1,
      "title": "1차 스프린트",
      "description": "기본 기능 구현",
      "status": "IN_PROGRESS",
      "dueDate": "2026-06-01T00:00:00",
      "createdAt": "2026-05-22T10:00:00",
      "updatedAt": "2026-05-22T10:00:00"
    },
    "tagResponseDtoList": [
      { "id": 1, "name": "backend" }
    ]
  },
  "taskInfoListDto": { "taskInfoDtoList": [] },
  "projectInfoDto": {
    "id": 1,
    "title": "miniDooray",
    "status": "ACTIVE",
    "mileStoneStatusList": ["IN_PROGRESS"]
  },
  "commentResponseDtoList": [
    {
      "id": 1,
      "accountId": 3,
      "userId": "jane_doe",
      "content": "리뷰 완료했습니다.",
      "createdAt": "2026-05-22T11:00:00",
      "updatedAt": "2026-05-22T11:00:00"
    }
  ]
}
```

### 에러

| 상황 | Status |
|------|--------|
| 프로젝트 멤버가 아님 | 404 |
| 존재하지 않는 taskId | 404 |

---

## 2-3. 태스크 생성

```
POST /task-api/projects/{projectId}/tasks
X-Account-Id: {accountId}
```

### Request Body

```json
{
  "title": "로그인 구현",
  "content": "JWT 기반 로그인 구현",
  "tagNameList": ["backend", "auth"]
}
```

| 필드 | 필수 | 타입 | 설명 |
|------|------|------|------|
| title | O | String | 태스크 제목 |
| content | O | String | 태스크 내용 |
| tagNameList | X | List\<String\> | 태그 이름 목록 (없으면 빈 배열) |

### 응답 (201 Created)

응답 본문 없음

### 에러

| 상황 | Status |
|------|--------|
| 존재하지 않는 projectId | 404 |
| 프로젝트 멤버가 아님 | 404 |

---

## 2-4. 태스크 수정

```
PUT /task-api/projects/{projectId}/tasks/{taskId}
X-Account-Id: {accountId}
```

### Request Body

```json
{
  "title": "수정된 제목",
  "content": "수정된 내용",
  "tagNameList": ["backend"]
}
```

### 응답 (200 OK)

[2-2 태스크 상세 조회의 `taskResponseDto` 구조와 동일](#응답-200-ok-3)

### 에러

| 상황 | Status |
|------|--------|
| 프로젝트 멤버가 아님 | 404 |
| 태스크 작성자가 아님 | 403 |

---

## 2-5. 태스크 삭제

소프트 삭제 방식으로 처리됩니다 (`isDeleted = true`).

```
DELETE /task-api/projects/{projectId}/tasks/{taskId}
X-Account-Id: {accountId}
```

### 응답 (204 No Content)

응답 본문 없음

### 에러

| 상황 | Status |
|------|--------|
| 프로젝트 멤버가 아님 | 404 |
| 태스크 작성자가 아님 | 403 |

---

# 3. 댓글 (Comment)

## 3-1. 댓글 생성

```
POST /task-api/tasks/{taskId}/comments
X-Account-Id: {accountId}
```

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| taskId | Long | 태스크 고유 번호 |

### Request Body

```json
{
  "content": "댓글 내용입니다."
}
```

| 필드 | 필수 | 타입 | 설명 |
|------|------|------|------|
| content | O | String | 댓글 내용 |

### 응답 (201 Created)

응답 본문 없음

### 에러

| 상황 | Status |
|------|--------|
| 프로젝트 멤버가 아님 | 404 |

---

## 3-2. 댓글 수정

```
PUT /task-api/tasks/{taskId}/comments/{commentId}
X-Account-Id: {accountId}
```

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| taskId | Long | 태스크 고유 번호 |
| commentId | Long | 댓글 고유 번호 |

### Request Body

```json
{
  "content": "수정된 댓글 내용입니다."
}
```

### 응답 (204 No Content)

응답 본문 없음

### 에러

| 상황 | Status |
|------|--------|
| 존재하지 않는 댓글 | 404 |
| 댓글 작성자가 아님 | 403 |

---

## 3-3. 댓글 삭제

```
DELETE /task-api/tasks/{taskId}/comments/{commentId}
X-Account-Id: {accountId}
```

### 응답 (204 No Content)

응답 본문 없음

### 에러

| 상황 | Status |
|------|--------|
| 존재하지 않는 댓글 | 404 |
| 댓글 작성자가 아님 | 403 |

---

# 4. 마일스톤 (MileStone)

태스크 1개당 마일스톤 1개를 가질 수 있습니다. 마일스톤 생성·수정·삭제는 **태스크 작성자만** 가능합니다.

## 4-1. 마일스톤 생성

```
POST /task-api/tasks/{taskId}/milestones
X-Account-Id: {accountId}
```

### Request Body

```json
{
  "title": "1차 스프린트",
  "description": "기본 기능 구현",
  "status": "PLANNED",
  "dueDate": "2026-06-01T00:00:00"
}
```

| 필드 | 필수 | 타입 | 설명 |
|------|------|------|------|
| title | O | String | 마일스톤 제목 |
| description | X | String | 마일스톤 설명 |
| status | O | MileStoneStatus | 마일스톤 상태 |
| dueDate | X | LocalDateTime | 마감일 |

### 응답 (201 Created)

응답 본문 없음

### 에러

| 상황 | Status |
|------|--------|
| 태스크 작성자가 아님 | 403 |

---

## 4-2. 마일스톤 수정

```
PUT /task-api/tasks/{taskId}/milestones
X-Account-Id: {accountId}
```

### Request Body

[4-1 마일스톤 생성 요청 본문과 동일](#request-body-6)

### 응답 (204 No Content)

응답 본문 없음

### 에러

| 상황 | Status |
|------|--------|
| 태스크 작성자가 아님 | 403 |
| 마일스톤이 존재하지 않음 | 404 |

---

## 4-3. 마일스톤 삭제

```
DELETE /task-api/tasks/{taskId}/milestones
X-Account-Id: {accountId}
```

### 응답 (204 No Content)

응답 본문 없음

### 에러

| 상황 | Status |
|------|--------|
| 태스크 작성자가 아님 | 403 |
| 마일스톤이 존재하지 않음 | 404 |

---

# 5. 프로젝트 멤버 (ProjectMember)

## 5-1. 멤버 목록 조회

```
GET /task-api/projects/{projectId}/members
X-Account-Id: {accountId}
```

### 응답 (200 OK)

```json
{
  "memberInfoDtoList": [
    {
      "accountId": 1,
      "memberId": 1,
      "userId": "john_doe",
      "auth": "ADMIN",
      "joinedAt": "2026-05-22T10:00:00"
    },
    {
      "accountId": 2,
      "memberId": 2,
      "userId": "jane_doe",
      "auth": "MEMBER",
      "joinedAt": "2026-05-22T11:00:00"
    }
  ]
}
```

| 필드 | 타입 | 설명 |
|------|------|------|
| accountId | Long | 계정 고유 번호 |
| memberId | Long | 프로젝트 멤버 고유 번호 |
| userId | String | 로그인 ID |
| auth | MembersAuth | 권한 (`ADMIN` / `MEMBER`) |
| joinedAt | LocalDateTime | 참여일시 |

### 에러

| 상황 | Status |
|------|--------|
| 프로젝트 멤버가 아님 | 404 |

---

## 5-2. 멤버 추가

`ADMIN` 권한을 가진 멤버만 호출 가능합니다.

```
POST /task-api/projects/{projectId}/members
X-Account-Id: {accountId}
```

### Request Body

```json
{
  "userId": "jane_doe",
  "auth": "MEMBER"
}
```

| 필드 | 필수 | 타입 | 설명 |
|------|------|------|------|
| userId | O | String | 추가할 계정의 로그인 ID |
| auth | O | MembersAuth | 부여할 권한 (`ADMIN` / `MEMBER`) |

### 응답 (201 Created)

응답 본문 없음  
이미 탈퇴한 멤버라면 재활성화 처리됩니다.

### 에러

| 상황 | Status |
|------|--------|
| 관리자 권한 없음 | 403 |
| 이미 존재하는 멤버 | 409 |

---

## 5-3. 멤버 권한 변경

`ADMIN` 권한을 가진 멤버만 호출 가능합니다.

```
PUT /task-api/projects/{projectId}/members/{memberId}
X-Account-Id: {accountId}
```

### Path Parameters

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| projectId | Long | 프로젝트 고유 번호 |
| memberId | Long | 대상 멤버 고유 번호 |

### Request Body

```json
{
  "auth": "ADMIN"
}
```

### 응답 (204 No Content)

응답 본문 없음

### 에러

| 상황 | Status |
|------|--------|
| 관리자 권한 없음 | 403 |

---

## 5-4. 멤버 삭제

본인 탈퇴 또는 `ADMIN`의 강제 추방이 가능합니다.

```
DELETE /task-api/projects/{projectId}/members/{memberId}
X-Account-Id: {accountId}
```

### 응답 (204 No Content)

응답 본문 없음

### 에러

| 상황 | Status |
|------|--------|
| 존재하지 않는 멤버 | 404 |
| 권한 없음 | 403 |

---

# 6. 마이페이지 (MyPage)

## 6-1. 내 태스크 목록 조회

```
GET /task-api/mypage/tasks
X-Account-Id: {accountId}
```

### 응답 (200 OK)

```json
{
  "taskInfoDtoList": [
    {
      "id": 1,
      "title": "로그인 구현",
      "status": "IN_PROGRESS"
    }
  ]
}
```

---

## 6-2. 내 댓글 목록 조회

```
GET /task-api/mypage/comments
X-Account-Id: {accountId}
```

### 응답 (200 OK)

```json
{
  "commentResponseDtoList": [
    {
      "id": 1,
      "accountId": 1,
      "userId": "john_doe",
      "content": "리뷰 완료했습니다.",
      "createdAt": "2026-05-22T11:00:00",
      "updatedAt": "2026-05-22T11:00:00"
    }
  ]
}
```

---

# 데이터 타입 참고

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
| `ADMIN` | 관리자 (프로젝트 생성자 또는 권한 부여 받은 멤버) |
| `MEMBER` | 일반 멤버 |