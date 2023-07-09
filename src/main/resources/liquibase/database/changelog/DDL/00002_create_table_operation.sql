--liquibase formatted sql logicalFilePath:/src/main/resources/database/changelog/DDL/changelog-1.yml
--changeset Amira.zrelli:create-table-operation-02
CREATE TABLE IF NOT EXISTS operation
(
    id                    uuid         not null primary key,
    amount                bigint,
    date                  timestamp,
    type                  varchar(255),
    account_id            bigint         foreign key references account(id)
);
-- rollback DROP TABLE operation;

