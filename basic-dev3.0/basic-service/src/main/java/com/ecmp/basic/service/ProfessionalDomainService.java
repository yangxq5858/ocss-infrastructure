package com.ecmp.basic.service;

import com.ecmp.basic.api.IProfessionalDomainService;
import com.ecmp.basic.dao.ProfessionalDomainDao;
import com.ecmp.basic.entity.ProfessionalDomain;
import com.ecmp.core.dao.BaseTreeDao;
import com.ecmp.core.service.BaseTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 领域业务层
 * Author:jamson
 * date:2018/3/13
 */
@Service
public class ProfessionalDomainService extends BaseTreeService<ProfessionalDomain> implements IProfessionalDomainService {
    @Autowired
    private ProfessionalDomainDao professionalDomainDao;

    @Override
    protected BaseTreeDao<ProfessionalDomain> getDao() {
        return professionalDomainDao;
    }

    /**
     * 获取整个领域树
     *
     * @return 领域树形对象集合
     */
    @Override
    public List<ProfessionalDomain> getDomainTree() {
        List<ProfessionalDomain> rootTree = getAllRootNode();
        List<ProfessionalDomain> rootDomainTree = new ArrayList<>();
        for (ProfessionalDomain aRootTree : rootTree) {
            ProfessionalDomain professionalDomain = getTree(aRootTree.getId());
            rootDomainTree.add(professionalDomain);
        }
        return rootDomainTree;
    }
}
