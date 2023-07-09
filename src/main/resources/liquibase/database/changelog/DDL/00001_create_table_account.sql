--liquibase formatted sql logicalFilePath:/src/main/resources/database/changelog/DDL/changelog-1.yml
--changeset Amira.zrelli:create-table-account-01
CREATE TABLE IF NOT EXISTS account
(
    id                    uuid         not null primary key,
    balance               bigint
    );
-- rollback DROP TABLE account;

