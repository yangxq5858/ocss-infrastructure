/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017-06-13 18:55:27                          */
/*==============================================================*/


alter table corporation
   add erp_code varchar(10) comment 'ERP公司代码';

alter table corporation
   add rank int not null default 0 comment '排序';

alter table corporation
   add frozen bool not null default 0 comment '冻结标志';

