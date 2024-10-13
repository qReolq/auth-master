create table if not exists roles
(
    id          bigserial primary key,
    name        varchar(255) not null unique,
    description varchar(255)
);