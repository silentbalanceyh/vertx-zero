-- liquibase formatted sql

-- changeset Lang:h-team-employee-1
-- 关联表：R_TEAM_EMPLOYEE
DROP TABLE IF EXISTS R_TEAM_EMPLOYEE;
CREATE TABLE IF NOT EXISTS R_TEAM_EMPLOYEE
(
    `TEAM_ID`     VARCHAR(36) COMMENT '「teamId」- 组的ID',
    `EMPLOYEE_ID` VARCHAR(36) COMMENT '「employeeId」- 员工ID',
    `COMMENT`     TEXT COMMENT '「comment」- 关系备注',
    PRIMARY KEY (`TEAM_ID`, `EMPLOYEE_ID`)
);
