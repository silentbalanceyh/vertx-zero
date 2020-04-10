-- liquibase formatted sql

-- changeset Lang:h-company-customer-1
-- 关联表：R_COMPANY_CUSTOMER
DROP TABLE IF EXISTS R_COMPANY_CUSTOMER;
CREATE TABLE IF NOT EXISTS R_COMPANY_CUSTOMER
(
    `COMPANY_ID`  VARCHAR(36) COMMENT '「companyId」- 企业的ID',
    `CUSTOMER_ID` VARCHAR(36) COMMENT '「customerId」- 客户信息的ID',
    `COMMENT`     TEXT COMMENT '「comment」- 关系备注',
    PRIMARY KEY (`COMPANY_ID`, `CUSTOMER_ID`)
);
