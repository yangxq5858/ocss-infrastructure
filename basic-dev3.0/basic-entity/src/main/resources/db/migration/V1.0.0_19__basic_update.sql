/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/7/24 11:09:57                           */
/*==============================================================*/


/*==============================================================*/
/* Table: user_email_alert                                      */
/*==============================================================*/
create table user_email_alert
(
   id                   varchar(36) not null,
   user_id              varchar(36) not null,
   to_do_amount         int(11),
   hours                int(11),
   last_time            datetime,
   tenant_code          varchar(10),
   creator_id           varchar(36),
   creator_account      varchar(50),
   creator_name         varchar(50),
   created_date         datetime,
   last_editor_id       varchar(36),
   last_editor_account  varchar(50),
   last_editor_name     varchar(50),
   last_edited_date     datetime,
   key idx_email_alert_tenant_code (tenant_code)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户邮件提醒';

