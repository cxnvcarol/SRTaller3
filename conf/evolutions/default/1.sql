# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table feature (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  type                      varchar(255),
  constraint pk_feature primary key (id))
;

create table item_content (
  itemlong_id               bigint,
  feature_id                bigint,
  rating                    float,
  item_id                   varchar(255))
;

create table movie (
  id                        bigint auto_increment not null,
  imdb_id                   bigint,
  dbpedia_uri               varchar(255),
  title                     varchar(255),
  num_ratings               integer,
  average_rating            double,
  constraint pk_movie primary key (id))
;

create table rating (
  userid                    bigint,
  movieid                   bigint,
  rating                    double,
  timestamp                 bigint)
;

create table user (
  user_id                   bigint auto_increment not null,
  constraint pk_user primary key (user_id))
;


create table movie_feature (
  movie_id                       bigint not null,
  feature_id                     bigint not null,
  constraint pk_movie_feature primary key (movie_id, feature_id))
;



alter table movie_feature add constraint fk_movie_feature_movie_01 foreign key (movie_id) references movie (id) on delete restrict on update restrict;

alter table movie_feature add constraint fk_movie_feature_feature_02 foreign key (feature_id) references feature (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table feature;

drop table item_content;

drop table movie;

drop table movie_feature;

drop table rating;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

