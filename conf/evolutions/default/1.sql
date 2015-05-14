# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table attribute_db (
  attribute_id              bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_attribute_db primary key (attribute_id))
;

create table business (
  business_id               varchar(255) not null,
  full_address              varchar(255),
  name                      varchar(255),
  city                      varchar(255),
  state                     varchar(255),
  latitude                  double,
  longitude                 double,
  stars                     double,
  review_count              integer,
  open                      tinyint(1) default 0,
  opentimes_string          varchar(255),
  constraint pk_business primary key (business_id))
;

create table category (
  category_id               bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_category primary key (category_id))
;

create table item_content (
  itemlong_id               bigint auto_increment not null,
  feature_id                bigint,
  rating                    float,
  item_id                   varchar(255),
  constraint pk_item_content primary key (itemlong_id))
;

create table review (
  business_id               varchar(255),
  user_id                   varchar(255),
  stars                     double,
  text                      varchar(255),
  date                      datetime,
  sentiment                 integer)
;

create table tip (
  id                        bigint auto_increment not null,
  user_user_id              varchar(255),
  business_business_id      varchar(255),
  text                      varchar(255),
  date                      datetime,
  likes                     integer,
  sentiment                 integer,
  constraint pk_tip primary key (id))
;

create table user (
  user_id                   varchar(255) not null,
  name                      varchar(255),
  review_count              integer,
  average_stars             double,
  yelping_since             varchar(255),
  fans                      integer,
  constraint pk_user primary key (user_id))
;


create table businesscategories (
  business_business_id           varchar(255) not null,
  category_category_id           bigint not null,
  constraint pk_businesscategories primary key (business_business_id, category_category_id))
;

create table businessattributes (
  business_business_id           varchar(255) not null,
  attribute_db_attribute_id      bigint not null,
  constraint pk_businessattributes primary key (business_business_id, attribute_db_attribute_id))
;

create table usercategories (
  user_user_id                   varchar(255) not null,
  category_category_id           bigint not null,
  constraint pk_usercategories primary key (user_user_id, category_category_id))
;

create table userattributes (
  user_user_id                   varchar(255) not null,
  attribute_db_attribute_id      bigint not null,
  constraint pk_userattributes primary key (user_user_id, attribute_db_attribute_id))
;
alter table tip add constraint fk_tip_user_1 foreign key (user_user_id) references user (user_id) on delete restrict on update restrict;
create index ix_tip_user_1 on tip (user_user_id);
alter table tip add constraint fk_tip_business_2 foreign key (business_business_id) references business (business_id) on delete restrict on update restrict;
create index ix_tip_business_2 on tip (business_business_id);



alter table businesscategories add constraint fk_businesscategories_business_01 foreign key (business_business_id) references business (business_id) on delete restrict on update restrict;

alter table businesscategories add constraint fk_businesscategories_category_02 foreign key (category_category_id) references category (category_id) on delete restrict on update restrict;

alter table businessattributes add constraint fk_businessattributes_business_01 foreign key (business_business_id) references business (business_id) on delete restrict on update restrict;

alter table businessattributes add constraint fk_businessattributes_attribute_db_02 foreign key (attribute_db_attribute_id) references attribute_db (attribute_id) on delete restrict on update restrict;

alter table usercategories add constraint fk_usercategories_user_01 foreign key (user_user_id) references user (user_id) on delete restrict on update restrict;

alter table usercategories add constraint fk_usercategories_category_02 foreign key (category_category_id) references category (category_id) on delete restrict on update restrict;

alter table userattributes add constraint fk_userattributes_user_01 foreign key (user_user_id) references user (user_id) on delete restrict on update restrict;

alter table userattributes add constraint fk_userattributes_attribute_db_02 foreign key (attribute_db_attribute_id) references attribute_db (attribute_id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table attribute_db;

drop table business;

drop table businesscategories;

drop table businessattributes;

drop table category;

drop table item_content;

drop table review;

drop table tip;

drop table user;

drop table usercategories;

drop table userattributes;

SET FOREIGN_KEY_CHECKS=1;

