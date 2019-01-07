--
-- Table structure for table `app_module`
--


CREATE TABLE `app_module` (
  `id` varchar(36) NOT NULL COMMENT 'Id',
  `code` varchar(20) NOT NULL COMMENT '代码',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `web_base_address` varchar(255) DEFAULT NULL COMMENT 'web基地址',
  `api_base_address` varchar(255) DEFAULT NULL COMMENT 'api基地址',
  `rank` int(11) NOT NULL COMMENT '排序号',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_app_module_code` (`code`),
  KEY `idx_app_module_rank` (`rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用模块';


--
-- Table structure for table `authorize_entity_type`
--
CREATE TABLE `authorize_entity_type` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `entity_class_name` varchar(100) NOT NULL COMMENT '实体类名',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `app_module_id` varchar(36) NOT NULL COMMENT '应用模块Id',
  `api_path` varchar(100) NOT NULL COMMENT 'API服务路径',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index_entity_class_name` (`entity_class_name`),
  KEY `fk_a_entity_type_app_module_id` (`app_module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限对象类型';

--
-- Table structure for table `corporation`
--
CREATE TABLE `corporation` (
  `id` varchar(36) NOT NULL COMMENT 'ID标识',
  `code` varchar(10) NOT NULL COMMENT '代码',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `tenant_code` varchar(10) NOT NULL COMMENT '租户代码',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_corporation_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公司';


--
-- Table structure for table `data_authorize_type`
--
CREATE TABLE `data_authorize_type` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `code` varchar(50) NOT NULL COMMENT '代码',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `authorize_entity_type_id` varchar(36) NOT NULL COMMENT '权限对象类型Id',
  `feature_id` varchar(36) DEFAULT NULL COMMENT '功能项Id',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`),
  UNIQUE KEY `idx_a_e_type_id_feature_id` (`authorize_entity_type_id`,`feature_id`),
  KEY `fk_data_a_type_feature_id` (`feature_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据权限类型';

--
-- Table structure for table `data_role`
--
CREATE TABLE `data_role` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `tenant_code` varchar(10) NOT NULL COMMENT '租户代码',
  `code` varchar(50) NOT NULL COMMENT '代码',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `data_role_group_id` varchar(36) NOT NULL COMMENT '角色组Id',
  `public_user_type` smallint(6) DEFAULT NULL COMMENT '公共角色的用户类型',
  `public_org_id` varchar(36) DEFAULT NULL,
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index_code` (`code`),
  KEY `fk_d_role_data_role_group_id` (`data_role_group_id`),
  KEY `fk_data_role_tenant_code` (`tenant_code`),
  KEY `fk_data_role_public_org_id` (`public_org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据角色';

--
-- Table structure for table `data_role_auth_type_value`
--
CREATE TABLE `data_role_auth_type_value` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `data_role_id` varchar(36) NOT NULL COMMENT '数据角色Id',
  `data_authorize_type_id` varchar(36) NOT NULL COMMENT '数据权限类型Id',
  `entity_id` varchar(36) NOT NULL COMMENT '权限对象实体Id',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_d_r_id_d_a_t_id_entity_id` (`data_role_id`,`data_authorize_type_id`,`entity_id`),
  KEY `fk_d_r_a_t_v_data_au_type` (`data_authorize_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据角色分配权限类型的值';

--
-- Table structure for table `data_role_group`
--
CREATE TABLE `data_role_group` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `tenant_code` varchar(10) NOT NULL COMMENT '租户代码',
  `code` varchar(20) NOT NULL COMMENT '代码',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index_code` (`code`),
  KEY `fk_data_role_group_tenant` (`tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据角色组';

--
-- Table structure for table `employee`
--
CREATE TABLE `employee` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `code` varchar(10) NOT NULL COMMENT '员工编号',
  `tenant_code` varchar(10) NOT NULL COMMENT '代码',
  `organization_id` varchar(36) NOT NULL COMMENT '组织机构Id',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_code_tenant_code` (`code`,`tenant_code`),
  KEY `idx_employee_org_id` (`organization_id`),
  KEY `fk_tenant` (`tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企业员工';

--
-- Table structure for table `employee_position`
--
CREATE TABLE `employee_position` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `employee_id` varchar(36) NOT NULL COMMENT '企业员工id',
  `position_id` varchar(36) NOT NULL COMMENT '岗位id',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_id_position_id` (`employee_id`,`position_id`),
  KEY `fk_emp_position_position_id` (`position_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工的岗位';

--
-- Table structure for table `feature`
--
CREATE TABLE `feature` (
  `id` varchar(36) NOT NULL COMMENT '功能项Id',
  `code` varchar(50) NOT NULL COMMENT '代码',
  `feature_group_id` varchar(36) NOT NULL COMMENT '关联功能项组Id',
  `name` varchar(36) NOT NULL COMMENT '名称',
  `feature_type` smallint(6) NOT NULL COMMENT '功能项枚举',
  `url` varchar(400) DEFAULT NULL COMMENT '资源地址',
  `can_menu` bit(1) DEFAULT NULL COMMENT '是菜单项',
  `tenant_can_use` bit(1) NOT NULL COMMENT '租户可用',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_feature_code` (`code`),
  KEY `fk_feature_feature_group_id` (`feature_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='功能项';

--
-- Table structure for table `feature_group`
--
CREATE TABLE `feature_group` (
  `id` varchar(36) NOT NULL COMMENT 'Id',
  `code` varchar(30) NOT NULL COMMENT '代码',
  `app_module_id` varchar(36) NOT NULL COMMENT '关联应用模块Id',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_feature_group_code` (`code`),
  KEY `fk_feature_g_app_module_id` (`app_module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='功能项组';

--
-- Table structure for table `feature_role`
--
CREATE TABLE `feature_role` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `tenant_code` varchar(10) NOT NULL COMMENT '租户代码',
  `code` varchar(50) NOT NULL COMMENT '代码',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `feature_role_group_id` varchar(36) NOT NULL COMMENT '角色组Id',
  `role_type` smallint(6) NOT NULL COMMENT '角色类型',
  `public_user_type` smallint(6) DEFAULT NULL COMMENT '公共角色的用户类型',
  `public_org_id` varchar(36) DEFAULT NULL COMMENT '公共角色的组织机构Id',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index_code` (`code`),
  KEY `fk_f_role_f_role_group_id` (`feature_role_group_id`),
  KEY `fk_f_role_public_org_id` (`public_org_id`),
  KEY `fk_f_role_tenant_code` (`tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='功能角色';

--
-- Table structure for table `feature_role_feature`
--
CREATE TABLE `feature_role_feature` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `feature_role_id` varchar(36) NOT NULL COMMENT '功能角色Id',
  `feature_id` varchar(36) NOT NULL COMMENT '功能项Id',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_feature_role_id_feature_id` (`feature_role_id`,`feature_id`),
  KEY `fk_f_role_feature_feature_id` (`feature_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='功能角色分配的功能项';

--
-- Table structure for table `feature_role_group`
--
CREATE TABLE `feature_role_group` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `tenant_code` varchar(10) NOT NULL COMMENT '租户代码',
  `code` varchar(20) NOT NULL COMMENT '代码',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`),
  KEY `idx_tenant_code` (`tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='功能角色组';

--
-- Table structure for table `flyway_version`
--
CREATE TABLE `flyway_version` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_version_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `menu`
--
CREATE TABLE `menu` (
  `id` varchar(36) NOT NULL COMMENT 'Id',
  `code` varchar(10) NOT NULL COMMENT '代码',
  `name` varchar(20) NOT NULL COMMENT '名称',
  `code_path` varchar(500) NOT NULL COMMENT '代码路径',
  `name_path` varchar(500) NOT NULL COMMENT '名称路径',
  `feature_id` varchar(36) DEFAULT NULL COMMENT '功能项Id',
  `parent_id` varchar(36) DEFAULT NULL COMMENT '父节点Id',
  `node_level` smallint(6) NOT NULL COMMENT '层级',
  `rank` int(11) NOT NULL COMMENT '排序号',
  `icon_cls` varchar(30) DEFAULT NULL COMMENT '图标样式',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_menu_code` (`code`),
  KEY `idx_menu_parent_id` (`parent_id`),
  KEY `idx_menu_rank` (`rank`),
  KEY `fk_menu_feature_id` (`feature_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统菜单';

--
-- Table structure for table `organization`
--
CREATE TABLE `organization` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `code` varchar(6) NOT NULL COMMENT '代码',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `parent_id` varchar(36) DEFAULT NULL COMMENT '父节点Id',
  `node_level` smallint(6) NOT NULL COMMENT '层级',
  `tenant_code` varchar(10) NOT NULL COMMENT '代码',
  `code_path` varchar(100) DEFAULT NULL COMMENT '代码路径',
  `name_path` varchar(1000) DEFAULT NULL COMMENT '名称路径',
  `frozen` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否冻结',
  `rank` int(11) NOT NULL COMMENT '排序',
  `ref_code` varchar(50) DEFAULT NULL COMMENT '参考码',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_organization_tenant_code` (`code`,`tenant_code`),
  UNIQUE KEY `uk_organization_code_path` (`code_path`),
  KEY `fk_organization_parent_id` (`parent_id`),
  KEY `fk_organization_tenant_code` (`tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织机构';

--
-- Table structure for table `position`
--
CREATE TABLE `position` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `code` varchar(8) NOT NULL COMMENT '代码',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `category_id` varchar(36) NOT NULL COMMENT '岗位类别Id',
  `category_name` varchar(50) NOT NULL COMMENT '岗位类别名称',
  `tenant_code` varchar(10) NOT NULL COMMENT '租户代码',
  `organization_id` varchar(36) NOT NULL COMMENT '组织机构Id',
  `organization_name` varchar(100) NOT NULL COMMENT '组织机构名称',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_position_code` (`code`,`tenant_code`),
  UNIQUE KEY `uk_position_org_id_name` (`name`,`organization_id`),
  KEY `idx_position_organization_id` (`organization_id`),
  KEY `fk_position_tenant_code` (`tenant_code`),
  KEY `fk_position_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='岗位';


--
-- Table structure for table `position_category`
--
CREATE TABLE `position_category` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `code` varchar(6) NOT NULL COMMENT '代码',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `tenant_code` varchar(10) NOT NULL COMMENT '代码',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_position_category_code` (`code`,`tenant_code`),
  KEY `fk_p_category_tenant_code` (`tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='岗位类别';

--
-- Table structure for table `position_data_role`
--
CREATE TABLE `position_data_role` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `position_id` varchar(36) NOT NULL COMMENT '岗位Id',
  `data_role_id` varchar(36) NOT NULL COMMENT '数据角色Id',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index_position_id_data_role_id` (`position_id`,`data_role_id`),
  KEY `fk_p_data_role_data_role_id` (`data_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='岗位分配数据角色';

--
-- Table structure for table `position_feature_role`
--
CREATE TABLE `position_feature_role` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `position_id` varchar(36) NOT NULL COMMENT '岗位Id',
  `feature_role_id` varchar(36) NOT NULL COMMENT '功能角色Id',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index_position_id_feature_role_id` (`position_id`,`feature_role_id`),
  KEY `fk_p_f_role_feature_role_id` (`feature_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='岗位分配功能角色';

--
-- Table structure for table `tenant`
--
CREATE TABLE `tenant` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `code` varchar(10) NOT NULL COMMENT '租户代码',
  `name` varchar(200) NOT NULL COMMENT '租户名称',
  `frozen` bit(1) NOT NULL,
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tanant_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='租户';


--
-- Table structure for table `tenant_app_module`
--
CREATE TABLE `tenant_app_module` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `tenant_id` varchar(36) NOT NULL COMMENT '租户Id',
  `app_module_id` varchar(36) NOT NULL COMMENT '应用模块Id',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tenant_id_app_module_id` (`tenant_id`,`app_module_id`),
  KEY `fk_t_app_module_app_module_id` (`app_module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='租户分配应用模块';

--
-- Table structure for table `user`
--
CREATE TABLE `user` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `tenant_code` varchar(10) NOT NULL COMMENT '租户代码',
  `user_type` smallint(6) NOT NULL COMMENT '用户类型',
  `user_name` varchar(50) NOT NULL COMMENT '用户名称',
  `user_authority_policy` smallint(6) NOT NULL DEFAULT '0' COMMENT '用户权限策略',
  `frozen` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否冻结',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_code` (`tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';

--
-- Table structure for table `user_account`
--
CREATE TABLE `user_account` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `user_id` varchar(36) NOT NULL COMMENT '用户Id',
  `nickname` varchar(50) NOT NULL COMMENT '用户昵称',
  `account` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL COMMENT '密码',
  `salt` varchar(36) NOT NULL COMMENT '加密盐值',
  `tenant_code` varchar(10) NOT NULL COMMENT '租户代码',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_code_account` (`account`,`tenant_code`),
  KEY `idx_user_account_user_id` (`user_id`),
  KEY `fk_user_account_tenant_code` (`tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户账户';

--
-- Table structure for table `user_data_role`
--
CREATE TABLE `user_data_role` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `user_id` varchar(36) NOT NULL COMMENT '用户Id',
  `data_role_id` varchar(36) NOT NULL COMMENT '数据角色Id',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index_1` (`user_id`,`data_role_id`),
  KEY `fk_user_data_role_data_role_id` (`data_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户分配数据角色';

--
-- Table structure for table `user_feature_role`
--
CREATE TABLE `user_feature_role` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `user_id` varchar(36) NOT NULL COMMENT '用户Id',
  `feature_role_id` varchar(36) NOT NULL COMMENT '功能角色Id',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Index_user_id_feature_role_id` (`user_id`,`feature_role_id`),
  KEY `fk_user_f_role_f_role_id` (`feature_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户分配功能角色';

--
-- Table structure for table `user_profile`
--
CREATE TABLE `user_profile` (
  `id` varchar(36) NOT NULL COMMENT 'Id标识',
  `user_id` varchar(36) NOT NULL COMMENT '用户Id',
  `email` varchar(100) DEFAULT NULL COMMENT '邮件',
  `gender` bit(1) DEFAULT NULL COMMENT '性别',
  `language_code` varchar(10) DEFAULT NULL COMMENT '语言代码',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `creator_id` varchar(36) DEFAULT NULL COMMENT '创建人Id',
  `creator_account` varchar(50) DEFAULT NULL COMMENT '创建人账号',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_editor_id` varchar(36) DEFAULT NULL COMMENT '最后修改人Id',
  `last_editor_account` varchar(50) DEFAULT NULL COMMENT '最后修改人账号',
  `last_editor_name` varchar(50) DEFAULT NULL COMMENT '最后修改人姓名',
  `last_edited_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_profile_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户配置信息';

