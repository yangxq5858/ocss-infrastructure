/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/11/1 14:27:29                           */
/*==============================================================*/


alter table user_profile
   add accountor varchar(20) default NULL comment '记账用户';

