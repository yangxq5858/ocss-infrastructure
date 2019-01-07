/*==============================================================*/
/* DBMS name:       MySQL 5.0                                    */
/* Created on:     2017/6/12 17:15:38                           */
/*==============================================================*/


drop table if exists tmp_corporation;

rename table corporation to tmp_corporation;

/*==============================================================*/
/* Table: corporation                                           */
/*==============================================================*/
create table corporation
(
   id                   varchar(36) not null comment 'ID标识',
   code                 varchar(20) not null comment '代码',
   name                 varchar(100) not null comment '名称',
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
   unique key uk_corporation_code (code)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公司';

insert into corporation (id, code, name, tenant_code, creator_id, creator_account, creator_name, created_date, last_editor_id, last_editor_account, last_editor_name, last_edited_date)
select id, code, name, tenant_code, creator_id, creator_account, creator_name, created_date, last_editor_id, last_editor_account, last_editor_name, last_edited_date
from tmp_corporation;

