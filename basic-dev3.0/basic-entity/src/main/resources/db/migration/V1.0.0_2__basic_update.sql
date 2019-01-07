/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017-06-12 10:01:42                          */
/*==============================================================*/


alter table authorize_entity_type
   add be_tree bool not null default 0 comment '是树形结构';

