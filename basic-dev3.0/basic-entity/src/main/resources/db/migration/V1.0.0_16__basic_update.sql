/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/12/19 14:55:58                          */
/*==============================================================*/


drop table if exists tmp_region;

rename table region to tmp_region;

/*==============================================================*/
/* Table: region                                                */
/*==============================================================*/
create table region
(
   id                   varchar(36) not null comment 'Id',
   code                 varchar(10) not null comment '代码',
   name                 varchar(90) not null comment '名称',
   code_path            varchar(500) not null comment '代码路径',
   name_path            varchar(500) not null comment '名称路径',
   parent_id            varchar(36) default NULL comment '父节点Id',
   node_level           smallint not null comment '层级',
   country_id           varchar(36) not null comment '国家Id',
   rank                 int not null comment '排序号',
   short_name           varchar(30) comment '缩写',
   pin_yin              varchar(200) comment '拼音',
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
   unique key uk_region_code_tenant (code, tenant_code)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统菜单';

alter table region comment '行政区域';

insert into region (id, code, name, code_path, name_path, parent_id, node_level, country_id, rank, short_name, pin_yin, tenant_code, creator_id, creator_account, creator_name, created_date, last_editor_id, last_editor_account, last_editor_name, last_edited_date)
select id, code, name, code_path, name_path, parent_id, node_level, country_id, rank, short_name, pin_yin, tenant_code, creator_id, creator_account, creator_name, created_date, last_editor_id, last_editor_account, last_editor_name, last_edited_date
from tmp_region;

/*==============================================================*/
/* Index: idx_region_parent_id                                  */
/*==============================================================*/
create index idx_region_parent_id on region
(
   parent_id
);

/*==============================================================*/
/* Index: fk_region_country_id                                  */
/*==============================================================*/
create index fk_region_country_id on region
(
   country_id
);

/*==============================================================*/
/* Index: idx_region_short_name                                 */
/*==============================================================*/
create index idx_region_short_name on region
(
   short_name
);

/*==============================================================*/
/* Index: idx_region_pin_yin                                    */
/*==============================================================*/
create index idx_region_pin_yin on region
(
   pin_yin
);

