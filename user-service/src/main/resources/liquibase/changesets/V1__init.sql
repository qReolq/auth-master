create table if not exists users
(
    id         bigserial primary key,
    username   varchar(255) unique not null,
    email      varchar(255) unique not null,
    password   varchar(255)        not null,
    create_at  timestamp           not null,
    updated_at timestamp
);

create table if not exists roles
(
    id          bigserial primary key,
    name        varchar(255) not null unique,
    description varchar(255)
);

create table if not exists users_roles
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    constraint fk_users_roles_users foreign key (user_id) references users (id) on delete cascade on update no action,
    constraint fk_users_roles_roles foreign key (role_id) references roles (id) on delete cascade on update no action
);