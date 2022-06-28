create table follow (
    from_user bigint not null,
    to_user bigint not null,
    primary key (from_user, to_user)
);

create table achievement (
    achievement_id bigint not null auto_increment,
    award varchar(255),
    user_id bigint,
    primary key (achievement_id)
);

create table comment (
    comment_id bigint not null auto_increment,
    challenge_photo_id bigint not null,
    content TEXT not null,
    user_id bigint not null,
    primary key (comment_id)
);

create table likes (
    like_id bigint not null auto_increment,
    challenge_photo_id bigint not null,
    user_id bigint not null,
    primary key (like_id)
);

update user set attendance_count

alter table users add attendance_count bigint(20);
alter table users add challenge_count bigint(20);
alter table users add visit_time date;

alter table achievement add constraint DeleteUserCascade foreign key (user_id) references users (user_id) ON DELETE CASCADE;

