/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/12/12 15:25:26                          */
/*==============================================================*/


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
   primary key (id),
   unique key uk_country_code (code)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';

alter table country comment '国家';

/*==============================================================*/
/* Index: idx_country_rank                                      */
/*==============================================================*/
create index idx_country_rank on country
(
   rank
);

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
   erp_code             varchar(10) comment 'ERP代码',
   short_name           varchar(30) comment '缩写',
   pin_yin              varchar(200) comment '拼音',
   primary key (id),
   unique key uk_region_code (code)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统菜单';

alter table region comment '行政区域';

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

