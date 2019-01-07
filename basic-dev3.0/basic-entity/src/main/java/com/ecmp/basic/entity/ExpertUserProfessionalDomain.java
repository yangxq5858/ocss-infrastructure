package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;
import com.ecmp.core.entity.RelationEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 专家用户分配领域
 * Author:jamson
 * date:2018/3/13
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "expert_user_pro_domain")
@DynamicUpdate
@DynamicInsert
public class ExpertUserProfessionalDomain extends BaseAuditableEntity implements RelationEntity<ExpertUser, ProfessionalDomain> {
    /**
     * 专家
     */
    @ManyToOne
    @JoinColumn(name = "expert_user_id", nullable = false)
    private ExpertUser parent;
    /**
     * 领域
     */
    @ManyToOne
    @JoinColumn(name = "professional_domain_id", nullable = false)
    private ProfessionalDomain child;

    @Override
    public ExpertUser getParent() {
        return parent;
    }

    @Override
    public void setParent(ExpertUser parent) {
        this.parent = parent;
    }

    @Override
    public ProfessionalDomain getChild() {
        return child;
    }

    @Override
    public void setChild(ProfessionalDomain child) {
        this.child = child;
    }
}
