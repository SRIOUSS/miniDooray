-- miniDooray TaskAPI DDL
-- MySQL 8.0 / MariaDB 10.5+
--
-- 주의: tasks ↔ milestones 는 양방향 OneToOne FK 구조입니다.
-- 순환 참조를 피하기 위해 tasks 테이블을 먼저 생성하고
-- milestones 생성 후 tasks.milestone_id FK를 ALTER TABLE로 추가합니다.

-- ============================================================
-- projects
-- ============================================================
CREATE TABLE IF NOT EXISTS projects
(
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    title             VARCHAR(30)  NOT NULL,
    description       TEXT,
    status            VARCHAR(15)  NOT NULL DEFAULT 'ACTIVE',
    created_at        DATETIME     NOT NULL,
    create_account_id BIGINT       NOT NULL,
    is_deleted        TINYINT(1)   NOT NULL DEFAULT 0,

    PRIMARY KEY (id),
    CONSTRAINT chk_projects_status CHECK (status IN ('ACTIVE', 'DORMANT', 'TERMINATED'))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ============================================================
-- members  (ProjectMember)
-- ============================================================
CREATE TABLE IF NOT EXISTS members
(
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    project_id BIGINT      NOT NULL,
    account_id BIGINT      NOT NULL,
    auth       VARCHAR(10) NOT NULL DEFAULT 'MEMBER',
    joined_at  DATETIME    NOT NULL,
    is_deleted TINYINT(1)  NOT NULL DEFAULT 0,

    PRIMARY KEY (id),
    CONSTRAINT fk_members_project FOREIGN KEY (project_id) REFERENCES projects (id),
    CONSTRAINT chk_members_auth CHECK (auth IN ('ADMIN', 'MEMBER'))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ============================================================
-- tasks  (milestone_id FK는 milestones 생성 후 ALTER로 추가)
-- ============================================================
CREATE TABLE IF NOT EXISTS tasks
(
    id               BIGINT      NOT NULL AUTO_INCREMENT,
    project_id       BIGINT      NOT NULL,
    create_member_id BIGINT      NOT NULL,
    title            VARCHAR(30) NOT NULL,
    content          TEXT,
    created_at       DATETIME,
    updated_at       DATETIME,
    milestone_id     BIGINT               DEFAULT NULL,
    is_deleted       TINYINT(1)  NOT NULL DEFAULT 0,

    PRIMARY KEY (id),
    CONSTRAINT fk_tasks_project FOREIGN KEY (project_id) REFERENCES projects (id),
    CONSTRAINT fk_tasks_member  FOREIGN KEY (create_member_id) REFERENCES members (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ============================================================
-- milestones
-- ============================================================
CREATE TABLE IF NOT EXISTS milestones
(
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    task_id     BIGINT      NOT NULL,
    title       VARCHAR(30) NOT NULL,
    description TEXT        NOT NULL,
    status      VARCHAR(15) NOT NULL DEFAULT 'PLANNED',
    due_date    DATETIME,
    created_at  DATETIME    NOT NULL,
    updated_at  DATETIME,

    PRIMARY KEY (id),
    UNIQUE KEY uk_milestones_task_id (task_id),
    CONSTRAINT fk_milestones_task FOREIGN KEY (task_id) REFERENCES tasks (id),
    CONSTRAINT chk_milestones_status CHECK (status IN ('PLANNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- tasks.milestone_id FK 추가 (순환 참조 해결)
ALTER TABLE tasks
    ADD CONSTRAINT fk_tasks_milestone FOREIGN KEY (milestone_id) REFERENCES milestones (id);

-- ============================================================
-- comments
-- ============================================================
CREATE TABLE IF NOT EXISTS comments
(
    id         BIGINT     NOT NULL AUTO_INCREMENT,
    task_id    BIGINT     NOT NULL,
    member_id  BIGINT     NOT NULL,
    content    TEXT       NOT NULL,
    created_at DATETIME   NOT NULL,
    updated_at DATETIME,

    PRIMARY KEY (id),
    CONSTRAINT fk_comments_task   FOREIGN KEY (task_id)   REFERENCES tasks (id),
    CONSTRAINT fk_comments_member FOREIGN KEY (member_id) REFERENCES members (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ============================================================
-- tagResponseDtoList  (Tag 엔티티 — 테이블명 주의)
-- ============================================================
CREATE TABLE IF NOT EXISTS tagResponseDtoList
(
    id   BIGINT      NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,

    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ============================================================
-- taskTags  (Task ↔ Tag 연결 테이블)
-- ============================================================
CREATE TABLE IF NOT EXISTS taskTags
(
    id      BIGINT NOT NULL AUTO_INCREMENT,
    task_id BIGINT,
    tag_id  BIGINT,

    PRIMARY KEY (id),
    CONSTRAINT fk_tasktags_task FOREIGN KEY (task_id) REFERENCES tasks (id),
    CONSTRAINT fk_tasktags_tag  FOREIGN KEY (tag_id)  REFERENCES tagResponseDtoList (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;