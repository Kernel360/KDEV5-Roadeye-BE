CREATE TABLE platform_admin
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    status     VARCHAR(255) NULL,
    name       VARCHAR(255) NOT NULL,
    username   VARCHAR(30)  NOT NULL,
    password   VARCHAR(255) NOT NULL,
    deleted    BIT          NOT NULL,
    created_at DATETIME(6)  NULL,
    updated_at DATETIME(6)  NULL,

    CONSTRAINT uk_platform_admin_on_username UNIQUE (username)
);

CREATE TABLE company_quote
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    status           VARCHAR(255) NOT NULL,
    quote_status     INT          NOT NULL,
    company_name     VARCHAR(255) NOT NULL,
    company_email    VARCHAR(255) NOT NULL,
    company_bizno    VARCHAR(255) NOT NULL,
    username         VARCHAR(30)  NOT NULL,
    password         VARCHAR(255) NOT NULL,
    requested_at     DATETIME(6)  NOT NULL,
    approver_id      BIGINT NULL,
    approved_at      DATETIME(6)  NULL,
    rejected_at      DATETIME(6)  NULL,
    rejection_reason VARCHAR(255) NULL,
    deleted          BIT          NOT NULL,
    created_at       DATETIME(6)  NULL,
    updated_at       DATETIME(6)  NULL,

    CONSTRAINT fk_company_quote_platform_admin_on_id FOREIGN KEY (approver_id)
        REFERENCES platform_admin (id)
);

CREATE TABLE company
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    status     VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    bizno      VARCHAR(255) NOT NULL,
    deleted    BIT          NOT NULL,
    created_at DATETIME(6)  NULL,
    updated_at DATETIME(6)  NULL
);

CREATE TABLE employee
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    status     VARCHAR(255) NOT NULL,
    company_id BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    role       VARCHAR(255) NOT NULL,
    position   VARCHAR(255) NULL,
    username   VARCHAR(30)  NOT NULL,
    password   VARCHAR(255) NOT NULL,
    deleted    BIT          NOT NULL,
    created_at DATETIME(6)  NULL,
    updated_at DATETIME(6)  NULL,

    CONSTRAINT fk_employee_company_on_company_id FOREIGN KEY (company_id)
        REFERENCES company (id),
    CONSTRAINT uk_employee_on_company_id_and_username UNIQUE (company_id, username)
);

CREATE TABLE car
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    status            VARCHAR(255) NULL,
    company_id        BIGINT       NOT NULL,
    name              VARCHAR(255) NOT NULL,
    license_number    VARCHAR(255) NOT NULL,
    image_url         VARCHAR(512) NULL,
    ignition          INT          NOT NULL,
    active_tuid       BINARY(16)     NULL,
    gps_condition     VARCHAR(255) NOT NULL,
    gps_lat           DECIMAL(10, 6) NULL,
    gps_lon           DECIMAL(9, 6) NULL,
    ignition_on_time  DATETIME(6)    NULL,
    ignition_off_time DATETIME(6)    NULL,
    mileage_initial   INT          NOT NULL,
    mileage_sum       INT          NOT NULL,
    angle             INT          NOT NULL,
    speed             INT          NOT NULL,
    battery_voltage   INT          NOT NULL,
    disable_reason    VARCHAR(512) NULL,
    deleted           BIT          NOT NULL,
    created_at        DATETIME(6)    NULL,
    updated_at        DATETIME(6)    NULL,

    CONSTRAINT fk_car_company_on_company_id FOREIGN KEY (company_id)
        REFERENCES company (id)
);

-- 로그성 테이블
CREATE TABLE car_location_history
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    driving_id       BIGINT NOT NULL,
    gps_lat          DECIMAL(10, 6) NULL,
    gps_lon          DECIMAL(9, 6) NULL,
    speed            INT NULL,
    driving_datetime DATETIME(6)    NULL,
    created_at       DATETIME(6)    NULL
);

CREATE TABLE car_driving_history
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    status           VARCHAR(255) NOT NULL,
    car_id           BIGINT       NOT NULL,
    tx_uid           BINARY(16)     NOT NULL,
    prev_mileage_sum INT NULL,
    prev_latitude    DECIMAL(38, 2) NULL,
    prev_longitude   DECIMAL(38, 2) NULL,
    next_mileage_sum INT NULL,
    next_latitude    DECIMAL(38, 2) NULL,
    next_longitude   DECIMAL(38, 2) NULL,
    drive_ended_at   DATETIME(6)    NULL,
    drive_started_at DATETIME(6)    NULL,

    CONSTRAINT fk_car_driving_history_car_on_car_id FOREIGN KEY (car_id)
        REFERENCES car (id)
);

CREATE TABLE mdt_log
(
    log_id                BINARY(16)         NOT NULL PRIMARY KEY,
    event_type            VARCHAR(255)   NOT NULL,
    packet_ver            SMALLINT UNSIGNED  NOT NULL,
    tx_uid                BINARY(16)         NOT NULL,
    car_id                BIGINT         NOT NULL,
    device_id             VARCHAR(16)    NOT NULL,
    terminal_id           VARCHAR(16)    NOT NULL,
    manufacture_id        VARCHAR(16)    NOT NULL,
    gps_cond              CHAR           NOT NULL,
    gps_lat               DECIMAL(9, 6)  NOT NULL,
    gps_lon               DECIMAL(10, 6) NOT NULL,
    mdt_mileage_sum       MEDIUMINT UNSIGNED NOT NULL,
    mdt_ignition_on_time  DATETIME(6)        NULL,
    mdt_ignition_off_time DATETIME(6)        NULL,
    mdt_angle             SMALLINT UNSIGNED  NOT NULL,
    mdt_speed             TINYINT UNSIGNED   NOT NULL,
    mdt_battery_voltage   SMALLINT UNSIGNED  NULL,
    occurred_at           DATETIME(6)        NOT NULL,
    sent_at               DATETIME(6)        NOT NULL,
    received_at           DATETIME(6)        NOT NULL
);

-- 통계성 테이블
CREATE TABLE daily_driving_statistics
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    date             DATETIME(6) NULL,
    distance         INT NOT NULL,
    duration         INT NOT NULL,
    total_trip_count INT NOT NULL
);

CREATE TABLE hourly_driving_statistics
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    date          DATETIME(6) NULL,
    hour          INT NOT NULL,
    vehicle_count INT NOT NULL
);
