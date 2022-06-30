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

alter table challenge add photo_description varchar(255) NOT NULL;
alter table challenge add challenge_rule varchar(255) NOT NULL;
alter table challenge change check_frequency check_frequency_type int;
alter table challenge add total_star_rating float NOT NULL;
alter table challenge add review_count int NOT NULL;
alter table challenge change check_times_per_week check_times_per_round int;
alter table challenge add round int;
alter table challenge add status int;
alter table challenge add created_date datetime;
alter table challenge add updated_date datetime;

alter table achievement add constraint DeleteUserCascade foreign key (user_id) references users (user_id) ON DELETE CASCADE;


CREATE TABLE challenge_photo(
  `challenge_photo_id` bigint NOT NULL AUTO_INCREMENT,
  `challenge_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `photo_url` varchar(255) NOT NULL,
  PRIMARY KEY (`challenge_photo_id`)
);

alter table challenge_photo add foreign key(challenge_id) references challenge(challenge_id);
alter table challenge_photo add foreign key(user_id) references users(user_id);

CREATE TABLE cart(
  `cart_id` bigint NOT NULL AUTO_INCREMENT,
  `challenge_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`cart_id`),
  FOREIGN KEY (challenge_id) references challenge(challenge_id),
  FOREIGN KEY (user_id) references users(user_id)
);

CREATE TABLE `photo_check`(
  `photo_check_id` bigint NOT NULL AUTO_INCREMENT,
  `user_challenge_id` bigint NOT NULL,
  `challenge_photo_id` bigint NOT NULL,
  `round` int,
  `status` int NOT NULL,
  PRIMARY KEY (`photo_check_id`),
  FOREIGN KEY (user_challenge_id) references user_challenge(user_challenge_id),
  FOREIGN KEY (challenge_photo_id) references challenge_photo(challenge_photo_id)
);
