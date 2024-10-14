create table if not exists users
(
    id         bigserial primary key,
    username   varchar(255) unique not null,
    email      varchar(255) unique not null,
    password   varchar(255)        not null,
    create_at  timestamp           not null,
    updated_at timestamp
);

CREATE TABLE IF NOT EXISTS users_roles
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT user_role_unique UNIQUE (user_id, role_id)
);