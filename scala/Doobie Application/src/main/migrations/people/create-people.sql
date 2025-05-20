--liquibase formatted sql

--changeset runInTransaction:true failOnError:true
create table people (
    id bigint auto_increment primary key,
    age int not null,
    name varchar not null
);
--rollback drop table people
