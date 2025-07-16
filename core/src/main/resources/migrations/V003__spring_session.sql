CREATE TABLE _SPRING_SESSION
(
    primary_id            CHAR(36) NOT NULL,
    session_id            CHAR(36) NOT NULL,
    creation_time         BIGINT   NOT NULL,
    last_access_time      BIGINT   NOT NULL,
    max_inactive_interval INT      NOT NULL,
    expiry_time           BIGINT   NOT NULL,
    principal_name        VARCHAR(100),
    CONSTRAINT _SPRING_SESSION_pk PRIMARY KEY (primary_id)
)
    ENGINE = InnoDB
ROW_FORMAT = DYNAMIC;

CREATE UNIQUE INDEX _SPRING_SESSION_ix1 ON _SPRING_SESSION (session_id);
CREATE INDEX _SPRING_SESSION_ix2 ON _SPRING_SESSION (expiry_time);
CREATE INDEX _SPRING_SESSION_ix3 ON _SPRING_SESSION (principal_name);

CREATE TABLE _SPRING_SESSION_ATTRIBUTES
(
    session_primary_id CHAR(36)     NOT NULL,
    attribute_name     VARCHAR(200) NOT NULL,
    attribute_bytes    BLOB         NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_pk PRIMARY KEY (session_primary_id, attribute_name),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_fk FOREIGN KEY (session_primary_id)
        REFERENCES _SPRING_SESSION (primary_id) ON DELETE CASCADE
)
    ENGINE = InnoDB
ROW_FORMAT = DYNAMIC;
