create table article (article_date date, now date, scrap bit not null, id bigint not null, image_id bigint, author varchar(255), category varchar(255), context varchar(255), title varchar(255), url varchar(255), user_id varchar(255), primary key (id)) engine=InnoDB

create table article_keyword (article_id bigint not null, keyword varchar(255)) engine=InnoDB

create sequence article_seq (start with 1, increment by 1,maxvalue 999999)

create table image (id bigint not null, path varchar(255), primary key (id)) engine=InnoDB

create table user_table (id varchar(255) not null, password varchar(255), primary key (id)) engine=InnoDB

alter table article add constraint UK_dpkp9yv3y4yovhfm96mwgt3ml unique (image_id)
alter table article add constraint FKa8st57l43fmam691umn5bw37u foreign key (image_id) references image (id)
alter table article_keyword add constraint FK71143jtrpbywwr8ys2eppe7c2 foreign key (article_id) references article (id)

