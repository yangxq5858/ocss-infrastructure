package com.ecmp.basic.service;

import com.ecmp.basic.dao.ExpertUserProfessionalDomainDao;
import com.ecmp.basic.entity.ExpertUser;
import com.ecmp.basic.entity.ExpertUserProfessionalDomain;
import com.ecmp.basic.entity.ProfessionalDomain;
import com.ecmp.core.api.IBaseRelationService;
import com.ecmp.core.dao.BaseRelationDao;
import com.ecmp.core.service.BaseRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 专家分配领域业务层
 * Author:jamson
 * date:2018/3/13
 */
@Service
public class ExpertUserProfessionalDomainService extends BaseRelationService<ExpertUserProfessionalDomain, ExpertUser, ProfessionalDomain> {
    @Autowired
    private ExpertUserProfessionalDomainDao expertUserProfessionalDomainDao;

    @Override
    protected BaseRelationDao<ExpertUserProfessionalDomain, ExpertUser, ProfessionalDomain> getDao() {
        return expertUserProfessionalDomainDao;
    }

    @Override
    protected List<ProfessionalDomain> getCanAssignedChildren(String parentId) {
        return expertUserProfessionalDomainDao.getChildrenFromParentId(parentId);
    }
}
