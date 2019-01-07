package com.ecmp.basic.dao;

import com.ecmp.basic.entity.ExpertUser;
import com.ecmp.basic.entity.ExpertUserProfessionalDomain;
import com.ecmp.basic.entity.ProfessionalDomain;
import com.ecmp.core.dao.BaseRelationDao;
import org.springframework.stereotype.Repository;

/**
 * 专家分配领域DAO
 * Author:jamson
 * date:2018/3/13
 */
@Repository
public interface ExpertUserProfessionalDomainDao extends BaseRelationDao<ExpertUserProfessionalDomain, ExpertUser, ProfessionalDomain> {
}
