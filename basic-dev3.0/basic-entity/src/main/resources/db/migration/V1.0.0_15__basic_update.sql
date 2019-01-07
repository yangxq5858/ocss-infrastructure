/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/12/13 17:25:24                          */
/*==============================================================*/


drop table if exists tmp_country;

rename table country to tmp_country;

/*==============================================================*/
/* Table: country                                               */
/*==============================================================*/
create table country
(
   id                   varchar(36) not null comment 'Id标识',
   code                 varchar(4) not null comment '代码',
   name                 varchar(60) not null comment '名称',
   currency_code        varchar(5) not null comment '国家货币代码',
   currency_name        varchar(150) not null comment '国家货币名称',
   to_foreign           bit not null default 0 comment '是否国外',
   rank                 int not null comment '排序',
   tenant_code          varchar(10) not null comment '租户代码',
   creator_id           varchar(36) default NULL comment '创建人Id',
   creator_account      varchar(50) default NULL comment '创建人账号',
   creator_name         varchar(50) default NULL comment '创建人姓名',
   created_date         datetime default NULL comment '创建时间',
   last_editor_id       varchar(36) default NULL comment '最后修改人Id',
   last_editor_account  varchar(50) default NULL comment '最后修改人账号',
   last_editor_name     varchar(50) default NULL comment '最后修改人姓名',
   last_edited_date     datetime default NULL comment '最后修改时间',
   primary key (id),
   unique key uk_country_code (code)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';

alter table country comment '国家';

insert into country (id, code, name, currency_code, currency_name, to_foreign, rank, tenant_code, creator_id, creator_account, creator_name, created_date, last_editor_id, last_editor_account, last_editor_name, last_edited_date)
select id, code, name, currency_code, currency_name, to_foreign, rank, tenant_code, creator_id, creator_account, creator_name, created_date, last_editor_id, last_editor_account, last_editor_name, last_edited_date
from tmp_country;

/*==============================================================*/
/* Index: idx_country_rank                                      */
/*==============================================================*/
create index idx_country_rank on country
(
   rank
);

