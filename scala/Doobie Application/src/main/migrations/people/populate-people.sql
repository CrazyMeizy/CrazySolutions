--liquibase formatted sql

--changeset runInTransaction:true failOnError:true
insert into people(id, age, name) values
(2, 19,'Dima Chuprov'),
(4, 23, 'Vasya Pupkin');
--rollback delete from people
