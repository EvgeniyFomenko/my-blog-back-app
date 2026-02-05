-- Таблица с постами
create table if not exists "POST"
(
    id             bigserial primary key,
    title          varchar(256) not null,
    text           varchar(256) not null,
    likes_count    integer      not null,
    comments_count integer      not null,
    tags           varchar(256)
);
-- Таблица с комментами
create table if not exists "COMMENT"
(
    id      bigserial primary key,
    text    varchar(256) not null,
    post_id integer      not null
);
-- Таблица с файлами
create table if not exists "IMAGE"
(
    id      bigserial primary key,
    name    varchar(256) not null,
    post_id integer      not null
);

