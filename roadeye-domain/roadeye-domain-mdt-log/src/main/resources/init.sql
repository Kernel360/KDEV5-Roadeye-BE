CREATE TABLE `car_event_logs` (
    `log_id` BINARY(64) NOT NULL,
    `packet_ver` UNSIGNED SMALLINT NOT NULL,
    `event_type` VARCHAR(10) NOT NULL,
    `tx_uid` BINARY(36) NOT NULL,
    `car_id` VARCHAR(32) NOT NULL,
    `terminal_id` VARCHAR(16) NOT NULL,
    `manufacture_id` VARCHAR(16) NOT NULL,
    `device_id` VARCHAR(16) NOT NULL,
    `gps_cond` CHAR(1) NOT NULL,
    `gps_lat` DECIMAL(9, 6) NOT NULL,
    `gps_lon` DECIMAL(10, 6) NOT NULL,
    `mdt_angle` UNSIGNED SMALLINT NOT NULL,
    `mdt_speed` UNSIGNED TINYINT NOT NULL,
    `mdt_mileage_sum` UNSIGNED MEDIUMINT NOT NULL,
    `mdt_battery_voltage` UNSIGNED SMALLINT NULL,
    `mdt_ignition_onTime` DATETIME NULL,
    `mdt_ignition_offTime` DATETIME NULL,
    `occurred_at` DATETIME NOT NULL,
    `sent_at` DATETIME NOT NULL,
    `received_at` DATETIME NOT NULL
);

ALTER TABLE `car_event_logs` ADD CONSTRAINT `PK_CAR_EVENT_LOGS` PRIMARY KEY (`log_id`);
