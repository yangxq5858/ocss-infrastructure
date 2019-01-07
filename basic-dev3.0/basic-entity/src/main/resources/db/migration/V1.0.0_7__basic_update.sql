/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/9/4 10:56:44                            */
/*==============================================================*/


alter table corporation
   add base_currency_code varchar(5) comment '本位币货币代码';

alter table corporation
   add base_currency_name varchar(40) comment '本位币货币名称';

