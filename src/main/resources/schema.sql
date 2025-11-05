-- Таблица с пользователями
create table if not exists post(
                                    id bigserial primary key,
                                    title varchar(256) not null,
                                    text varchar(256) not null,
                                    likes_count integer not null,
                                    comments_count integer not null,
                                    tags varchar(256) );

create table if not exists comment(
                                   id bigserial primary key,
                                   text varchar(256) not null,
                                   post_id integer not null);

