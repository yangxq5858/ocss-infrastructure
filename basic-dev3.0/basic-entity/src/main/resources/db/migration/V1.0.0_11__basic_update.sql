/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/11/30 14:18:44                          */
/*==============================================================*/


alter table corporation
   add default_trade_partner varchar(10) comment '默认贸易伙伴代码';

alter table corporation
   add related_trade_partner varchar(10) comment '关联交易贸易伙伴';

