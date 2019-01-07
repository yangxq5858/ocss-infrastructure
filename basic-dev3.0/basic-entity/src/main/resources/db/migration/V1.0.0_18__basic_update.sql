/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/3/6 9:25:02                             */
/*==============================================================*/


alter table corporation
   add weixin_appid varchar(50) comment '微信用户凭证';

alter table corporation
   add weixin_secret varchar(100) comment '微信用户凭证密钥';

